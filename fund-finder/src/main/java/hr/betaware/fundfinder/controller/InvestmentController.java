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

import etm.core.monitor.EtmPoint;
import hr.betaware.fundfinder.resource.InvestmentResource;
import hr.betaware.fundfinder.resource.uigrid.PageableResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridResource;
import hr.betaware.fundfinder.service.EtmService;
import hr.betaware.fundfinder.service.InvestmentService;

@RestController
@RequestMapping(value = { "/api/v1/investment" })
public class InvestmentController {

	@Autowired
	private InvestmentService investmentService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public List<InvestmentResource> findInvestments() {
		EtmPoint point = etmService.createPoint("InvestmentController.findInvestments");
		try {
			return investmentService.findInvestments();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public InvestmentResource findInvestment(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("InvestmentController.findInvestment");
		try {
			return investmentService.findInvestment(id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public InvestmentResource saveInvestment(@RequestBody InvestmentResource resource) {
		EtmPoint point = etmService.createPoint("InvestmentController.saveInvestment");
		try {
			return investmentService.saveInvestment(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public void deleteInvestment(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("InvestmentController.deleteInvestment");
		try {
			investmentService.deleteInvestment(id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public PageableResource<InvestmentResource> getPage(@RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint("InvestmentController.getPage");
		try {
			return investmentService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/user")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public List<InvestmentResource> findInvestments4User(Principal principal) {
		EtmPoint point = etmService.createPoint("InvestmentController.findInvestments4User");
		try {
			return investmentService.findInvestments4User(principal);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/user/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public List<InvestmentResource> findInvestments4User(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("InvestmentController.findInvestments4User");
		try {
			return investmentService.findInvestments4User(id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user")
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public void saveInvestments4User(@RequestBody List<InvestmentResource> resources, Principal principal) {
		EtmPoint point = etmService.createPoint("InvestmentController.saveInvestments4User");
		try {
			investmentService.saveInvestments4User(resources, principal);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/activate/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public InvestmentResource activateInvestment(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("InvestmentController.activateInvestment");
		try {
			return investmentService.activateInvestment(id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/deactivate/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public InvestmentResource deactivateInvestment(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("InvestmentController.deactivateInvestment");
		try {
			return investmentService.deactivateInvestment(id);
		} finally {
			etmService.collect(point);
		}
	}

}
