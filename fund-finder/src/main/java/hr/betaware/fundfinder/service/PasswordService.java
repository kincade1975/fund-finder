package hr.betaware.fundfinder.service;

import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.ForgotPassword;
import hr.betaware.fundfinder.domain.User;

@Service
public class PasswordService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordService.class);

	@Value(value = "${forgot-password.endpoint}")
	private String forgotPasswordEndpoint;

	@Value(value = "${forgot-password.uuid-valid-for}")
	private long uuidValidFor;

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private JavaMailSender mailSender;

	public boolean validateEmail(String email) {
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(email));
		return (mongoOperations.findOne(query, User.class) == null) ? false : true;
	}

	public boolean validateUuid(String uuid) {
		ForgotPassword forgotPassword = mongoOperations.findById(uuid, ForgotPassword.class);

		if ((forgotPassword.getTimeCreated().getTime() + (uuidValidFor * 60 * 1000)) < System.currentTimeMillis()) {
			return false;
		}

		return (forgotPassword == null) ? false : true;
	}

	public void forgotPassword(String email) {
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(email));
		User user = mongoOperations.findOne(query, User.class);

		ForgotPassword forgotPassword = new ForgotPassword();
		forgotPassword.setId(UUID.randomUUID().toString());
		forgotPassword.setUser(user);
		mongoOperations.save(forgotPassword);

		LOGGER.debug("Sending forgot password email to {} - UUID: {}", email, forgotPassword.getId());

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("fundfinder@google.com");
		message.setTo(email);
		message.setSubject("Fund Finder - Resetiranje zaporke");
		message.setText("Molimo kliknite na sljedeći link da bi resetirali vašu zaporku: " + forgotPasswordEndpoint + forgotPassword.getId() + ". Ovaj link je validan sljedećih " + uuidValidFor + " minuta.");
		mailSender.send(message);
	}

	public void resetPassword(HashMap<String, String> data) {
		String uuid = data.get("uuid");
		String password = data.get("password");

		LOGGER.debug("Resetting password for UUID {}", uuid);

		ForgotPassword forgotPassword = mongoOperations.findById(uuid, ForgotPassword.class);
		User user = forgotPassword.getUser();

		LOGGER.debug("Resetting password for user {}", user.getUsername());

		user.setPassword(new MessageDigestPasswordEncoder("SHA-1").encodePassword(password, null));
		mongoOperations.save(user);

		mongoOperations.remove(forgotPassword);
	}

}
