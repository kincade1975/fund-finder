package hr.betaware.fundfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.Company;
import hr.betaware.fundfinder.domain.User;
import hr.betaware.fundfinder.domain.User.Role;
import hr.betaware.fundfinder.resource.RegistrationResource;
import hr.betaware.fundfinder.resource.UserResource;
import hr.betaware.fundfinder.resource.assembler.UserResourceAssembler;

@Service
public class RegistrationService {

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private UserResourceAssembler userResourceAssembler;

	public boolean validateEmail(String email) {
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(email));

		if (mongoOperations.count(query, User.class) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public UserResource register(RegistrationResource resource) {
		User user = new User();
		user.setId(sequenceService.getNextSequence(SequenceService.SEQUENCE_USER));
		user.setUsername(resource.getEmail());
		user.setFirstName(resource.getFirstName());
		user.setLastName(resource.getLastName());
		user.setPassword(new MessageDigestPasswordEncoder("SHA-1").encodePassword(resource.getPassword(), null));
		user.setRole(Role.ROLE_USER);

		Company company = new Company();
		company.setId(sequenceService.getNextSequence(SequenceService.SEQUENCE_COMPANY));
		company.setName(resource.getCompany());

		mongoOperations.save(company);

		user.setCompanyName(company.getName());
		user.setCompany(company);

		mongoOperations.save(user);

		return userResourceAssembler.toResource(user);
	}

}
