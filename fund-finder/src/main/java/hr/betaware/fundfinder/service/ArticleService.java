package hr.betaware.fundfinder.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.Article;
import hr.betaware.fundfinder.resource.ArticleResource;
import hr.betaware.fundfinder.resource.assembler.ArticleResourceAssembler;
import hr.betaware.fundfinder.resource.uigrid.PageableResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridFilterResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridSortResource;

@Service
public class ArticleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleService.class);

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private ArticleResourceAssembler articleResourceAssembler;

	public List<ArticleResource> findAll() {
		return articleResourceAssembler.toResources(mongoOperations.findAll(Article.class));
	}

	public ArticleResource findArticle(Integer id) {
		if (id == 0) {
			// new article
			return new ArticleResource();
		}
		return articleResourceAssembler.toResource(mongoOperations.findById(id, Article.class));
	}

	public ArticleResource saveArticle(ArticleResource resource) {
		Article entity = null;
		if (resource.getIdentificator() == null) {
			entity = articleResourceAssembler.createEntity(resource);
		} else {
			entity = mongoOperations.findById(resource.getIdentificator(), Article.class);
			entity = articleResourceAssembler.updateEntity(entity, resource);
		}

		mongoOperations.save(entity);

		return articleResourceAssembler.toResource(entity);
	}

	public void deleteArticle(Integer id) {
		Article entity = mongoOperations.findById(id, Article.class);
		mongoOperations.remove(entity);
	}

	public PageableResource<ArticleResource> getPage(UiGridResource resource) {
		Query query = new Query();

		// sorting
		if (resource.getSort() == null || resource.getSort().isEmpty()) {
			query.with(new Sort(Direction.ASC, "title"));
		} else {
			for (UiGridSortResource uiGridSortResource : resource.getSort()) {
				query.with(new Sort(Direction.valueOf(uiGridSortResource.getDirection().toUpperCase()), uiGridSortResource.getName()));
			}
		}

		// filtering
		if (resource.getFilter() != null) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				query.addCriteria(Criteria.where(uiGridFilterResource.getName()).regex(uiGridFilterResource.getTerm(), "i"));
			}
		}

		// get total number of records that match query
		long total = mongoOperations.count(query, Article.class);

		// add paging to query
		LOGGER.debug("Querying database: {}", query);
		query.with(new PageRequest(resource.getPagination().getPage(), resource.getPagination().getSize()));

		// get records that match query
		List<Article> entities = mongoOperations.find(query, Article.class);

		return new PageableResource<>(total, articleResourceAssembler.toResources(entities));
	}

}
