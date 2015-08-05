package hr.betaware.fundfinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.betaware.fundfinder.resource.UserResource;
import hr.betaware.fundfinder.service.UserService;

@RestController
@RequestMapping(value = { "/api/v1/user" })
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public List<UserResource> findAll() {
		return userService.findAll();
	}

}
