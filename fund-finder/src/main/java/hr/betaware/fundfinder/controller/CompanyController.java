package hr.betaware.fundfinder.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.betaware.fundfinder.resource.CompanyResource;
import hr.betaware.fundfinder.resource.ValidationResource;
import hr.betaware.fundfinder.service.CompanyService;

@RestController
@RequestMapping(value = { "/api/v1/company" })
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public CompanyResource findCompany(Principal principal) {
		return companyService.findCompany(principal);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public CompanyResource saveCompany(Principal principal, @RequestBody CompanyResource resource) {
		return companyService.saveCompany(principal, resource);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/validate")
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public ValidationResource validateCompany(@RequestBody CompanyResource resource) {
		return companyService.validateCompany(resource);
	}

}
