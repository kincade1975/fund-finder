package hr.betaware.fundfinder.resource.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import hr.betaware.fundfinder.controller.ConfigurationController;
import hr.betaware.fundfinder.domain.Option;
import hr.betaware.fundfinder.domain.Question;
import hr.betaware.fundfinder.enums.QuestionType;
import hr.betaware.fundfinder.resource.OptionResource;
import hr.betaware.fundfinder.resource.QuestionResource;
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
		List<OptionResource> options = new ArrayList<>();
		if (entity.getOptions() != null && !entity.getOptions().isEmpty()) {
			for (Option option : entity.getOptions()) {
				options.add(new OptionResource(option.getId(), option.getValue()));
			}
		} else {
			options.add(new OptionResource(null, ""));
		}
		resource.setOptions(options);
		resource.setLinkQuestionId(entity.getLinkQuestionId());
		resource.setLinkOperator(entity.getLinkOperator());
		resource.setTimeCreated(entity.getTimeCreated());
		resource.setLastModified(entity.getLastModified());
		if (resource.getType() == QuestionType.RADIO) {
			resource.setAnswer(resource.getOptions().get(0).getIdentificator());
		}
		return resource;
	}

	public Question createEntity(QuestionResource resource) {
		Question entity = new Question();
		entity.setId(sequenceService.getNextSequence(SequenceService.SEQUENCE_QUESTION));
		entity.setEntityType(resource.getEntityType());
		entity.setIndex(resource.getIndex());
		entity.setText(resource.getText());
		entity.setType(resource.getType());
		List<Option> options = new ArrayList<>();
		for (OptionResource optionResource : resource.getOptions()) {
			Option option = new Option();
			option.setId((StringUtils.isEmpty(optionResource.getIdentificator())) ? UUID.randomUUID().toString() : optionResource.getIdentificator());
			option.setValue(optionResource.getValue());
			options.add(option);
		}
		entity.setOptions(options);
		return entity;
	}

	public Question updateEntity(Question entity, QuestionResource resource) {
		entity.setEntityType(resource.getEntityType());
		entity.setIndex(resource.getIndex());
		entity.setText(resource.getText());
		entity.setType(resource.getType());
		List<Option> options = new ArrayList<>();
		for (OptionResource optionResource : resource.getOptions()) {
			Option option = new Option();
			option.setId((StringUtils.isEmpty(optionResource.getIdentificator())) ? UUID.randomUUID().toString() : optionResource.getIdentificator());
			option.setValue(optionResource.getValue());
			options.add(option);
		}
		entity.setOptions(options);
		entity.setLinkQuestionId(resource.getLinkQuestionId());
		entity.setLinkOperator(resource.getLinkOperator());
		return entity;
	}

}
