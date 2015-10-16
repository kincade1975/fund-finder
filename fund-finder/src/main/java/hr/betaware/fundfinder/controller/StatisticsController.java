package hr.betaware.fundfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import hr.betaware.fundfinder.resource.StatisticsResource;
import hr.betaware.fundfinder.service.EtmService;
import hr.betaware.fundfinder.service.StatisticsService;

@RestController
@RequestMapping(value = { "/api/v1/statistics" })
public class StatisticsController {

	@Autowired
	private StatisticsService statisticsService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET, value = "/companiesBySector")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public StatisticsResource getCompaniesBySector() {
		EtmPoint point = etmService.createPoint("StatisticsController.getCompaniesBySector");
		try {
			return statisticsService.getCompaniesBySector(Integer.MAX_VALUE);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/companiesByLocation")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public StatisticsResource getCompaniesByLocation() {
		EtmPoint point = etmService.createPoint("StatisticsController.getCompaniesByLocation");
		try {
			return statisticsService.getCompaniesByLocation(Integer.MAX_VALUE);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/investments")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public StatisticsResource getInvestments() {
		EtmPoint point = etmService.createPoint("StatisticsController.getInvestments");
		try {
			return statisticsService.getInvestments(Integer.MAX_VALUE);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/revenues")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public StatisticsResource getRevenues() {
		EtmPoint point = etmService.createPoint("StatisticsController.getRevenues");
		try {
			return statisticsService.getRevenues(Integer.MAX_VALUE);
		} finally {
			etmService.collect(point);
		}
	}

}
