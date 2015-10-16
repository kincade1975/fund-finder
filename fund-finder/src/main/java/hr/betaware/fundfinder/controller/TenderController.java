package hr.betaware.fundfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import hr.betaware.fundfinder.resource.TenderResource;
import hr.betaware.fundfinder.resource.uigrid.PageableResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridResource;
import hr.betaware.fundfinder.service.EtmService;
import hr.betaware.fundfinder.service.TenderService;

@RestController
@RequestMapping(value = { "/api/v1/tender" })
public class TenderController {

	@Autowired
	private TenderService tenderService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public TenderResource findTender(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("TenderController.findTender");
		try {
			return tenderService.findTender(id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public TenderResource saveTender(@RequestBody TenderResource resource) {
		EtmPoint point = etmService.createPoint("TenderController.saveTender");
		try {
			return tenderService.saveTender(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public void deleteTender(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("TenderController.deleteTender");
		try {
			tenderService.deleteTender(id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public PageableResource<TenderResource> getPage(@RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint("TenderController.getPage");
		try {
			return tenderService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/activate/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public TenderResource activateTender(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("TenderController.activateTender");
		try {
			return tenderService.activateTender(id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/deactivate/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public TenderResource deactivateTender(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("TenderController.deactivateTender");
		try {
			return tenderService.deactivateTender(id);
		} finally {
			etmService.collect(point);
		}
	}

}
