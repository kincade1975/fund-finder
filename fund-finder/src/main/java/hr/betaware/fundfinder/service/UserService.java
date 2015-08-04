package hr.betaware.fundfinder.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.User;
import hr.betaware.fundfinder.domain.User.Role;
import hr.betaware.fundfinder.resource.UserResource;
import hr.betaware.fundfinder.resource.UserResourceAssembler;

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
		user.setRole(Role.Administrator);
		mongoOperations.save(user);

		LOGGER.info("Default user created...");
	}

	public List<UserResource> findAll() {
		return userResourceAssembler.toResources(mongoOperations.findAll(User.class));
	}

}
