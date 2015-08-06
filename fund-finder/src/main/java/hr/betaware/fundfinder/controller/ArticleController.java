package hr.betaware.fundfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.betaware.fundfinder.resource.ArticleResource;
import hr.betaware.fundfinder.resource.uigrid.PageableResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridResource;
import hr.betaware.fundfinder.service.ArticleService;

@RestController
@RequestMapping(value = { "/api/v1/article" })
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public ArticleResource findArticle(@PathVariable Integer id) {
		return articleService.findArticle(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public ArticleResource saveArticle(@RequestBody ArticleResource resource) {
		return articleService.saveArticle(resource);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public void deleteArticle(@PathVariable Integer id) {
		articleService.deleteArticle(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/page")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public PageableResource<ArticleResource> getPage(@RequestBody UiGridResource resource) {
		return articleService.getPage(resource);
	}

}
