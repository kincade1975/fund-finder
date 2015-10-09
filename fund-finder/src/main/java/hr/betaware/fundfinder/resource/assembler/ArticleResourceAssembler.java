package hr.betaware.fundfinder.resource.assembler;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import hr.betaware.fundfinder.controller.ArticleController;
import hr.betaware.fundfinder.domain.Article;
import hr.betaware.fundfinder.resource.ArticleResource;
import hr.betaware.fundfinder.service.SequenceService;

@Component
public class ArticleResourceAssembler extends ResourceAssemblerSupport<Article, ArticleResource> {

	@Autowired
	private SequenceService sequenceService;

	public ArticleResourceAssembler() {
		super(ArticleController.class, ArticleResource.class);
	}

	@Override
	public ArticleResource toResource(Article entity) {
		ArticleResource resource = new ArticleResource();
		resource.setIdentificator(entity.getId());
		resource.setTitle(entity.getTitle());
		resource.setText(StringEscapeUtils.unescapeHtml4(entity.getText()));
		resource.setStrippedText(new HtmlToPlainText().getPlainText(Jsoup.parse(resource.getText())).substring(0, 300));
		resource.setImage(entity.getImage());
		resource.setTimeCreated(entity.getTimeCreated());
		resource.setLastModified(entity.getLastModified());
		return resource;
	}

	public Article createEntity(ArticleResource resource) {
		Article entity = new Article();
		entity.setId(sequenceService.getNextSequence(SequenceService.SEQUENCE_ARTICLE));
		entity.setTitle(resource.getTitle());
		entity.setText(StringEscapeUtils.escapeHtml4(resource.getText()));
		entity.setImage(resource.getImage());
		return entity;
	}

	public Article updateEntity(Article entity, ArticleResource resource) {
		entity.setTitle(resource.getTitle());
		entity.setText(StringEscapeUtils.escapeHtml4(resource.getText()));
		entity.setImage(resource.getImage());
		return entity;
	}

}
