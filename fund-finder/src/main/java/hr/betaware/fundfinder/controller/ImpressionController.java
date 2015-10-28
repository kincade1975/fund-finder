package hr.betaware.fundfinder.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import hr.betaware.fundfinder.domain.Impression.EntityType;
import hr.betaware.fundfinder.enums.StatisticsPeriod;
import hr.betaware.fundfinder.resource.ImpressionResource;
import hr.betaware.fundfinder.resource.ImpressionStatisticsResource;
import hr.betaware.fundfinder.service.EtmService;
import hr.betaware.fundfinder.service.ImpressionService;

@RestController
@RequestMapping(value = { "/api/v1/impression", "/e/api/v1/impression" })
public class ImpressionController {

	@Autowired
	private ImpressionService impressionService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.POST)
	@Async("taskExecutor")
	public void saveImpression(Principal principal, @RequestBody ImpressionResource resource) {
		EtmPoint point = etmService.createPoint("ImpressionController.saveImpression");
		try {
			impressionService.saveImpression(principal, resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/statistics/{entity}/{entityId}/{statisticsPeriod}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public ImpressionStatisticsResource getImpressionStatistics(@PathVariable String entity, @PathVariable Integer entityId, @PathVariable String statisticsPeriod) {
		EtmPoint point = etmService.createPoint("ImpressionController.getImpressionStatistics");
		try {
			return impressionService.getImpressionStatistics(EntityType.valueOf(entity), entityId, StatisticsPeriod.valueOf(statisticsPeriod));
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/statistics/periods")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public Map<String, String> getStatisticsPeriods() {
		EtmPoint point = etmService.createPoint("ImpressionController.getStatisticsPeriods");
		try {
			Map<String, String> result = new HashMap<>();
			for (StatisticsPeriod period : StatisticsPeriod.values()) {
				result.put(period.toString(), period.getLabel());
			}
			return result;
		} finally {
			etmService.collect(point);
		}
	}

}
