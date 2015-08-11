package hr.betaware.fundfinder.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import hr.betaware.fundfinder.controller.ConfigurationController;
import hr.betaware.fundfinder.domain.Question;
import hr.betaware.fundfinder.service.SequenceService;

@Component
public class QuestionResourceAssembler extends ResourceAssemblerSupport<Question, QuestionResource> {

	@Autowired
	private SequenceService sequenceService;

	public QuestionResourceAssembler() {
		super(ConfigurationController.class, QuestionResource.class);
	}

	@Override
	public QuestionResource toResource(Question entity) {
		QuestionResource resource = new QuestionResource();
		resource.setIdentificator(entity.getId());
		resource.setEntityType(entity.getEntityType());
		resource.setIndex(entity.getIndex());
		resource.setText(entity.getText());
		resource.setType(entity.getType());
		resource.setOptions(entity.getOptions());
		resource.setTimeCreated(entity.getTimeCreated());
		resource.setLastModified(entity.getLastModified());
		return resource;
	}

	public Question createEntity(QuestionResource resource) {
		Question entity = new Question();
		entity.setId(sequenceService.getNextSequence(SequenceService.SEQUENCE_QUESTION));
		entity.setEntityType(resource.getEntityType());
		entity.setIndex(resource.getIndex());
		entity.setText(resource.getText());
		entity.setType(resource.getType());
		entity.setOptions(resource.getOptions());
		return entity;
	}

	public Question updateEntity(Question entity, QuestionResource resource) {
		entity.setEntityType(resource.getEntityType());
		entity.setIndex(resource.getIndex());
		entity.setText(resource.getText());
		entity.setType(resource.getType());
		entity.setOptions(resource.getOptions());
		return entity;
	}

}
