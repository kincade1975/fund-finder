package hr.betaware.fundfinder.security;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.User;
import hr.betaware.fundfinder.domain.User.Role;
import hr.betaware.fundfinder.service.SequenceService;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsService.class);

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

	@PostConstruct
	public void init() {
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
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = null;

		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("username").is(username));
			user = mongoOperations.findOne(query, User.class);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		if (user == null) {
			throw new UsernameNotFoundException("User not found!");
		}

		return new UserDetails(user);
	}

}