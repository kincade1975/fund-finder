package hr.betaware.fundfinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.betaware.fundfinder.resource.TenderResource;
import hr.betaware.fundfinder.resource.UserResource;
import hr.betaware.fundfinder.service.DashboardService;

@RestController
@RequestMapping(value = { "/api/v1/dashboard" })
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@RequestMapping(method = RequestMethod.GET, value = "/users")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR')")
	public List<UserResource> findLatestUsers() {
		return dashboardService.findLatestUsers();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/tenders")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR')")
	public List<TenderResource> findLatestTenders() {
		return dashboardService.findLatestTenders();
	}

}
