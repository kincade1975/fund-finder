package hr.betaware.fundfinder.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.betaware.fundfinder.resource.InvestmentResource;
import hr.betaware.fundfinder.resource.uigrid.PageableResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridResource;
import hr.betaware.fundfinder.service.InvestmentService;

@RestController
@RequestMapping(value = { "/api/v1/investment" })
public class InvestmentController {

	@Autowired
	private InvestmentService investmentService;

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
	public List<InvestmentResource> findInvestments() {
		return investmentService.findInvestments();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public InvestmentResource findInvestment(@PathVariable Integer id) {
		return investmentService.findInvestment(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public InvestmentResource saveInvestment(@RequestBody InvestmentResource resource) {
		return investmentService.saveInvestment(resource);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public void deleteInvestment(@PathVariable Integer id) {
		investmentService.deleteInvestment(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public PageableResource<InvestmentResource> getPage(@RequestBody UiGridResource resource) {
		return investmentService.getPage(resource);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/user")
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public List<InvestmentResource> findInvestments4User(Principal principal) {
		return investmentService.findInvestments4User(principal);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user")
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public void saveInvestments4User(@RequestBody List<InvestmentResource> resources, Principal principal) {
		investmentService.saveInvestments4User(resources, principal);
	}

}
