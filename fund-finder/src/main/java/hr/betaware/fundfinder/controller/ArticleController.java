package hr.betaware.fundfinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import etm.core.monitor.EtmPoint;
import hr.betaware.fundfinder.resource.ArticleResource;
import hr.betaware.fundfinder.resource.uigrid.PageableResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridResource;
import hr.betaware.fundfinder.service.ArticleService;
import hr.betaware.fundfinder.service.EtmService;

@RestController
@RequestMapping(value = { "/api/v1/article" })
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public List<ArticleResource> findAll() {
		EtmPoint point = etmService.createPoint("ArticleController.findAll");
		try {
			return articleService.findAll();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public ArticleResource findArticle(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("ArticleController.findArticle");
		try {
			return articleService.findArticle(id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public ArticleResource saveArticle(@RequestBody ArticleResource resource) {
		EtmPoint point = etmService.createPoint("ArticleController.saveArticle");
		try {
			return articleService.saveArticle(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public void deleteArticle(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("ArticleController.deleteArticle");
		try {
			articleService.deleteArticle(id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public PageableResource<ArticleResource> getPage(@RequestBody UiGridResource resource) {
		EtmPoint point = etmService.createPoint("ArticleController.getPage");
		try {
			return articleService.getPage(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/activate/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public ArticleResource activateArticle(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("ArticleController.activateArticle");
		try {
			return articleService.activateArticle(id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/deactivate/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public ArticleResource deactivateArticle(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("ArticleController.deactivateArticle");
		try {
			return articleService.deactivateArticle(id);
		} finally {
			etmService.collect(point);
		}
	}

}
