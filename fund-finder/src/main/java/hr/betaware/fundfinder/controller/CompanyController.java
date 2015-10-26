package hr.betaware.fundfinder.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import hr.betaware.fundfinder.resource.CompanyResource;
import hr.betaware.fundfinder.resource.ValidationResource;
import hr.betaware.fundfinder.service.CompanyService;
import hr.betaware.fundfinder.service.EtmService;

@RestController
@RequestMapping(value = { "/api/v1/company", "/e/api/v1/company" })
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public CompanyResource findCompany(Principal principal) {
		EtmPoint point = etmService.createPoint("CompanyController.findCompany");
		try {
			return companyService.findCompany(principal);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public CompanyResource saveCompany(Principal principal, @RequestBody CompanyResource resource) {
		EtmPoint point = etmService.createPoint("CompanyController.saveCompany");
		try {
			return companyService.saveCompany(principal, resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/validate")
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public ValidationResource validateCompany(@RequestBody CompanyResource resource) {
		EtmPoint point = etmService.createPoint("CompanyController.validateCompany");
		try {
			return companyService.validateCompany(resource);
		} finally {
			etmService.collect(point);
		}
	}

}
