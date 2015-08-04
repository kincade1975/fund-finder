package hr.betaware.fundfinder.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.User;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsService.class);

	@Autowired
	private MongoOperations mongoOperations;

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