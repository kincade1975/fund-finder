package hr.betaware.fundfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.betaware.fundfinder.resource.TenderResource;
import hr.betaware.fundfinder.resource.uigrid.PageableResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridResource;
import hr.betaware.fundfinder.service.TenderService;

@RestController
@RequestMapping(value = { "/api/v1/tender" })
public class TenderController {

	@Autowired
	private TenderService tenderService;

	//	@RequestMapping(method = RequestMethod.GET)
	//	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	//	public List<TenderResource> findAll() {
	//		return tenderService.findAll();
	//	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public TenderResource findTender(@PathVariable Integer id) {
		return tenderService.findTender(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public TenderResource saveTender(@RequestBody TenderResource resource) {
		return tenderService.saveTender(resource);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public void deleteTender(@PathVariable Integer id) {
		tenderService.deleteTender(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public PageableResource<TenderResource> getPage(@RequestBody UiGridResource resource) {
		return tenderService.getPage(resource);
	}

	@RequestMapping(method = RequestMethod.GET, value="/activate/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public TenderResource activateTender(@PathVariable Integer id) {
		return tenderService.activateTender(id);
	}

	@RequestMapping(method = RequestMethod.GET, value="/deactivate/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public TenderResource deactivateTender(@PathVariable Integer id) {
		return tenderService.deactivateTender(id);
	}

}
