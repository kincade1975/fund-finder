package hr.betaware.fundfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import hr.betaware.fundfinder.resource.TotalResource;
import hr.betaware.fundfinder.service.EtmService;
import hr.betaware.fundfinder.service.TotalService;

@RestController
@RequestMapping(value = { "/api/v1/total" })
public class TotalController {

	@Autowired
	private TotalService counterService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public TotalResource getTotal() {
		EtmPoint point = etmService.createPoint("TotalController.getTotal");
		try {
			return counterService.getTotal();
		} finally {
			etmService.collect(point);
		}
	}

}
