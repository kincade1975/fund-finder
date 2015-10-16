package hr.betaware.fundfinder.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import hr.betaware.fundfinder.resource.TenderResource;
import hr.betaware.fundfinder.service.EtmService;
import hr.betaware.fundfinder.service.OpportunityService;

@RestController
@RequestMapping(value = { "/api/v1/opportunity" })
public class OpportunityController {

	@Autowired
	private OpportunityService opportunityService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public List<TenderResource> findTenders(Principal principal) {
		EtmPoint point = etmService.createPoint("OpportunityController.findTenders");
		try {
			return opportunityService.findTenders(principal);
		} finally {
			etmService.collect(point);
		}
	}

}
