package hr.betaware.fundfinder.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.betaware.fundfinder.resource.UserResource;
import hr.betaware.fundfinder.security.UserDetails;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@RequestMapping(method = RequestMethod.GET, value = "/authenticate")
	public UserResource authenticate() {
		UserResource userResource = new UserResource();

		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication.getPrincipal() instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				userResource.setUsername(userDetails.getUsername());
				userResource.setFullName(userDetails.getUser().getFirstName() + " " + userDetails.getUser().getLastName());
				userResource.setRole(userDetails.getUser().getRole());
			}
		}

		return userResource;
	}

}
