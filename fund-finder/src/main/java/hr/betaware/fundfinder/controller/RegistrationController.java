package hr.betaware.fundfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.betaware.fundfinder.resource.RegistrationResource;
import hr.betaware.fundfinder.resource.UserResource;
import hr.betaware.fundfinder.service.RegistrationService;

@RestController
@RequestMapping(value = { "/registration" })
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;

	@RequestMapping(method = RequestMethod.GET)
	public RegistrationResource getRegistrationResource() {
		RegistrationResource resource = new RegistrationResource();
		return resource;
	}

	@RequestMapping(value = "/validate-email", method = RequestMethod.POST)
	public boolean validateEmail(@RequestBody String email) {
		return registrationService.validateEmail(email);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public UserResource register(@RequestBody RegistrationResource resource) {
		return registrationService.register(resource);
	}

}
