package hr.betaware.fundfinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.betaware.fundfinder.resource.UserResource;
import hr.betaware.fundfinder.resource.uigrid.PageableResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridResource;
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

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public void deleteUser(@PathVariable Integer id) {
		userService.deleteUser(id);
	}

	@RequestMapping(value = "/page", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public PageableResource<UserResource> getPage(@RequestBody UiGridResource resource) {
		return userService.getPage(resource);
	}

}
