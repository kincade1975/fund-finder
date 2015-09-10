package hr.betaware.fundfinder.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.betaware.fundfinder.service.PasswordService;

@RestController
@RequestMapping(value = { "/password" })
public class PasswordController {

	@Autowired
	private PasswordService passwordService;

	@RequestMapping(value = "/validateEmail", method = RequestMethod.POST)
	public boolean validateEmail(@RequestBody String email) {
		return passwordService.validateEmail(email);
	}

	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseEntity<Void> forgotPassword(@RequestBody String email) {
		passwordService.forgotPassword(email);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/validateUuid", method = RequestMethod.POST)
	public boolean validateUuid(@RequestBody String uuid) {
		return passwordService.validateUuid(uuid);
	}

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public ResponseEntity<Void> resetPassword(@RequestBody HashMap<String, String> data) {
		passwordService.resetPassword(data);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
