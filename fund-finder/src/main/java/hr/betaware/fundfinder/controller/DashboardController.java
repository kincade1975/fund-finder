package hr.betaware.fundfinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import hr.betaware.fundfinder.resource.StatisticsResource;
import hr.betaware.fundfinder.resource.TenderResource;
import hr.betaware.fundfinder.resource.UserResource;
import hr.betaware.fundfinder.service.CompanyService;
import hr.betaware.fundfinder.service.DashboardService;
import hr.betaware.fundfinder.service.EtmService;

@RestController
@RequestMapping(value = { "/api/v1/dashboard" })
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET, value = "/users")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public List<UserResource> findLatestUsers() {
		EtmPoint point = etmService.createPoint("DashboardController.findLatestUsers");
		try {
			return dashboardService.findLatestUsers();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/tenders")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public List<TenderResource> findLatestTenders() {
		EtmPoint point = etmService.createPoint("DashboardController.findLatestTenders");
		try {
			return dashboardService.findLatestTenders();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/statistics")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public List<StatisticsResource> getStatistics() {
		EtmPoint point = etmService.createPoint("DashboardController.getStatistics");
		try {
			return dashboardService.getStatistics();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/companies")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public List<UserResource> getCompanies(@RequestParam String type, @RequestParam String label) {
		EtmPoint point = etmService.createPoint("DashboardController.getCompanies");
		try {
			return companyService.getCompanies(type, label);
		} finally {
			etmService.collect(point);
		}
	}

}
