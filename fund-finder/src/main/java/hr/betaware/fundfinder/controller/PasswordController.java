package hr.betaware.fundfinder.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import hr.betaware.fundfinder.service.EtmService;
import hr.betaware.fundfinder.service.PasswordService;

@RestController
@RequestMapping(value = { "/password" })
public class PasswordController {

	@Autowired
	private PasswordService passwordService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(value = "/validateEmail", method = RequestMethod.POST)
	public boolean validateEmail(@RequestBody String email) {
		EtmPoint point = etmService.createPoint("PasswordController.validateEmail");
		try {
			return passwordService.validateEmail(email);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseEntity<Void> forgotPassword(@RequestBody String email) {
		EtmPoint point = etmService.createPoint("PasswordController.forgotPassword");
		try {
			passwordService.forgotPassword(email);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(value = "/validateUuid", method = RequestMethod.POST)
	public boolean validateUuid(@RequestBody String uuid) {
		EtmPoint point = etmService.createPoint("PasswordController.validateUuid");
		try {
			return passwordService.validateUuid(uuid);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public ResponseEntity<Void> resetPassword(@RequestBody HashMap<String, String> data) {
		EtmPoint point = etmService.createPoint("PasswordController.resetPassword");
		try {
			passwordService.resetPassword(data);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} finally {
			etmService.collect(point);
		}
	}

}
