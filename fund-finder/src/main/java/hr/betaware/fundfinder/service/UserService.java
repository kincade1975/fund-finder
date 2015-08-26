package hr.betaware.fundfinder.service;

import java.security.Principal;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.User;
import hr.betaware.fundfinder.domain.User.Role;
import hr.betaware.fundfinder.resource.UserResource;
import hr.betaware.fundfinder.resource.assembler.UserResourceAssembler;
import hr.betaware.fundfinder.resource.uigrid.PageableResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridFilterResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridSortResource;
import hr.betaware.fundfinder.security.UserDetails;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Value(value = "${default.user.username}")
	private String deafultUsername;

	@Value(value = "${default.user.first-name}")
	private String deafultFirstName;

	@Value(value = "${default.user.last-name}")
	private String deafultLastName;

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private UserResourceAssembler userResourceAssembler;

	@PostConstruct
	public void init() {
		createDefaultUser();
		//		createTestData();
	}

	public List<UserResource> findAll() {
		return userResourceAssembler.toResources(mongoOperations.findAll(User.class));
	}

	public void deleteUser(Integer id) {
		User entity = mongoOperations.findById(id, User.class);
		mongoOperations.remove(entity);
	}

	public PageableResource<UserResource> getPage(UiGridResource resource) {
		Query query = new Query();

		query.addCriteria(Criteria.where("role").ne(Role.ROLE_ADMINISTRATOR));

		// sorting
		if (resource.getSort() == null || resource.getSort().isEmpty()) {
			query.with(new Sort(Direction.ASC, "first_name"));
		} else {
			for (UiGridSortResource uiGridSortResource : resource.getSort()) {
				query.with(new Sort(Direction.valueOf(uiGridSortResource.getDirection().toUpperCase()), uiGridSortResource.getName()));
			}
		}

		// filtering
		if (resource.getFilter() != null) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				query.addCriteria(Criteria.where(uiGridFilterResource.getName()).regex(uiGridFilterResource.getTerm(), "i"));
			}
		}

		// get total number of records that match query
		long total = mongoOperations.count(query, User.class);

		// add paging to query
		query.with(new PageRequest(resource.getPagination().getPage(), resource.getPagination().getSize()));

		// get records that match query
		LOGGER.debug("Querying database: {}", query);
		List<User> entities = mongoOperations.find(query, User.class);

		return new PageableResource<>(total, userResourceAssembler.toResources(entities));
	}

	/**
	 * Convenience method that returns user for given principal object.
	 * @param principal
	 * @return
	 */
	public User getUser4Principal(Principal principal) {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
		UserDetails userDetails = (UserDetails) token.getPrincipal();
		return mongoOperations.findById(userDetails.getUser().getId(), User.class);
	}

	/**
	 * Method creates default user (if it does not exist).
	 */
	private void createDefaultUser() {
		// check if default user exists
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(deafultUsername));
		if (mongoOperations.findOne(query, User.class) != null) {
			return;
		}

		// create default user
		User user = new User();
		user.setId(sequenceService.getNextSequence(SequenceService.SEQUENCE_USER));
		user.setUsername(deafultUsername);
		user.setPassword(new MessageDigestPasswordEncoder("SHA-1").encodePassword("admin", null));
		user.setFirstName(deafultFirstName);
		user.setLastName(deafultLastName);
		user.setRole(Role.ROLE_ADMINISTRATOR);
		mongoOperations.save(user);

		LOGGER.info("Default user created...");
	}

	void createTestData() {
		for (int i = 10; i < 100; i++) {
			User user = new User();
			user.setId(sequenceService.getNextSequence(SequenceService.SEQUENCE_USER));
			user.setUsername(i + "@test.net");
			user.setPassword(new MessageDigestPasswordEncoder("SHA-1").encodePassword("admin", null));
			user.setFirstName("FirstName " + i);
			user.setLastName("LastName " + i);
			user.setRole(Role.ROLE_USER);
			mongoOperations.save(user);
		}
	}

}
