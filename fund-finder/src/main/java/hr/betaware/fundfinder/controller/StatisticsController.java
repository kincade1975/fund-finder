package hr.betaware.fundfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.betaware.fundfinder.resource.StatisticsResource;
import hr.betaware.fundfinder.service.StatisticsService;

@RestController
@RequestMapping(value = { "/api/v1/statistics" })
public class StatisticsController {

	@Autowired
	private StatisticsService statisticsService;

	@RequestMapping(method = RequestMethod.GET, value = "/companiesBySector")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public StatisticsResource getCompaniesBySector() {
		return statisticsService.getCompaniesBySector(Integer.MAX_VALUE);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/companiesByLocation")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public StatisticsResource getCompaniesByLocation() {
		return statisticsService.getCompaniesByLocation(Integer.MAX_VALUE);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/investments")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public StatisticsResource getInvestments() {
		return statisticsService.getInvestments(Integer.MAX_VALUE);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/revenues")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public StatisticsResource getRevenues() {
		return statisticsService.getRevenues(Integer.MAX_VALUE);
	}

}
