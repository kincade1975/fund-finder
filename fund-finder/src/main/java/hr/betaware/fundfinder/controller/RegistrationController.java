package hr.betaware.fundfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import hr.betaware.fundfinder.resource.RegistrationResource;
import hr.betaware.fundfinder.resource.UserResource;
import hr.betaware.fundfinder.service.EtmService;
import hr.betaware.fundfinder.service.RegistrationService;

@RestController
@RequestMapping(value = { "/registration" })
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	public RegistrationResource getRegistrationResource() {
		EtmPoint point = etmService.createPoint("RegistrationController.getRegistrationResource");
		try {
			RegistrationResource resource = new RegistrationResource();
			return resource;
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(value = "/validate-email", method = RequestMethod.POST)
	public boolean validateEmail(@RequestBody String email) {
		EtmPoint point = etmService.createPoint("RegistrationController.validateEmail");
		try {
			return registrationService.validateEmail(email);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public UserResource register(@RequestBody RegistrationResource resource) {
		EtmPoint point = etmService.createPoint("RegistrationController.register");
		try {
			return registrationService.register(resource);
		} finally {
			etmService.collect(point);
		}
	}

}
