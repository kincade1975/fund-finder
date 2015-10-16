package hr.betaware.fundfinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import hr.betaware.fundfinder.resource.UserResource;
import hr.betaware.fundfinder.resource.uigrid.PageableResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridResource;
import hr.betaware.fundfinder.service.EtmService;
import hr.betaware.fundfinder.service.UserService;

@RestController
@RequestMapping(value = { "/api/v1/user" })
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public List<UserResource> findAll() {
		EtmPoint point = etmService.createPoint("UserController.findAll");
		try {
			return userService.findAll();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public UserResource find(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("UserController.find");
		try {
			return userService.find(id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public UserResource saveUser(@RequestBody UserResource resource) {
		EtmPoint point = etmService.createPoint("UserController.saveUser");
		try {
			return userService.saveUser(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public void deleteUser(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("UserController.deleteUser");
		try {
			userService.deleteUser(id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(value = "/page", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public PageableResource<UserResource> getPage(@RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint("UserController.getPage");
		try {
			return userService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

}
