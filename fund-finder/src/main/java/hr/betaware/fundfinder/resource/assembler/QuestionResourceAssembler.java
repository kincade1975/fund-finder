package hr.betaware.fundfinder.resource.assembler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import hr.betaware.fundfinder.controller.ConfigurationController;
import hr.betaware.fundfinder.domain.County;
import hr.betaware.fundfinder.domain.Investment;
import hr.betaware.fundfinder.domain.Nkd;
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

	@Autowired
	private MongoOperations mongoOperations;

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
			resource.setAnswer((entity.getAnswer() == null) ? resource.getOptions().get(0).getValue() : entity.getAnswer());
			resource.setAnswerInternal((entity.getAnswerInternal() == null) ? resource.getOptions().get(0).getValue() : entity.getAnswerInternal());
		} else {
			resource.setAnswer(entity.getAnswer());
			resource.setAnswerInternal(entity.getAnswerInternal());
		}
		return resource;
	}

	public Question toEntity(QuestionResource resource) {
		Question entity = new Question();
		entity.setId(resource.getIdentificator());
		entity.setEntityType(resource.getEntityType());
		entity.setIndex(resource.getIndex());
		entity.setText(resource.getText());
		entity.setType(resource.getType());
		List<Option> options = new ArrayList<>();
		for (OptionResource optionResource : resource.getOptions()) {
			Option option = new Option();
			option.setId(optionResource.getIdentificator());
			option.setValue(optionResource.getValue());
			options.add(option);
		}
		entity.setOptions(options);
		entity.setLinkQuestionId(resource.getLinkQuestionId());
		entity.setLinkOperator(resource.getLinkOperator());
		if (resource.getType() == QuestionType.MULTI_SELECT) {
			setAnswer4MultiSelect(resource, entity);
		} else if (resource.getType() == QuestionType.SELECT) {
			setAnswer4Select(resource, entity);
		} else if (resource.getType() == QuestionType.CHECKBOX) {
			setAnswer4CheckBox(resource, entity);
		} else if (resource.getType() == QuestionType.COUNTY) {
			setAnswer4County(resource, entity);
		} else if (resource.getType() == QuestionType.INVESTMENT) {
			setAnswer4Investment(resource, entity);
		} else if (resource.getType() == QuestionType.NKD_AUX) {
			setAnswer4NkdAux(resource, entity);
		} else {
			entity.setAnswer(resource.getAnswerInternal());
		}
		entity.setAnswerInternal(resource.getAnswerInternal());
		return entity;
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

	private void setAnswer4MultiSelect(QuestionResource resource, Question entity) {
		if (resource.getAnswerInternal() instanceof List) {
			List<String> tmpAnswers = new ArrayList<>();
			for (Object tmpAnswer : (ArrayList<?>) resource.getAnswerInternal()) {
				for (OptionResource optionResource : resource.getOptions()) {
					if (optionResource.getIdentificator().equals(tmpAnswer.toString())) {
						tmpAnswers.add(optionResource.getValue());
						break;
					}
				}
			}
			entity.setAnswer(tmpAnswers);
		} else {
			entity.setAnswer(resource.getAnswerInternal());
		}
	}

	private void setAnswer4Select(QuestionResource resource, Question entity) {
		if (resource.getAnswerInternal() != null) {
			for (OptionResource optionResource : resource.getOptions()) {
				if (optionResource.getIdentificator().equals(resource.getAnswerInternal().toString())) {
					entity.setAnswer(optionResource.getValue());
					break;
				}
			}
		} else {
			entity.setAnswer(resource.getAnswerInternal());
		}
	}

	private void setAnswer4CheckBox(QuestionResource resource, Question entity) {
		if (resource.getAnswerInternal() instanceof Map) {
			List<String> tmpAnswers = new ArrayList<>();
			for (Entry<?, ?> entry : ((LinkedHashMap<?,?>) resource.getAnswerInternal()).entrySet()) {
				tmpAnswers.add(resource.getOptions().get(Integer.parseInt(entry.getKey().toString())).getValue());
			}
			entity.setAnswer(tmpAnswers);
		} else {
			entity.setAnswer(resource.getAnswerInternal());
		}
	}

	private void setAnswer4County(QuestionResource resource, Question entity) {
		if (resource.getAnswerInternal() != null && resource.getAnswerInternal() instanceof List) {
			List<String> tmpAnswers = new ArrayList<>();
			for (Object tmpAnswer : (ArrayList<?>) resource.getAnswerInternal()) {
				County county = mongoOperations.findById(tmpAnswer, County.class);
				tmpAnswers.add(county.getName());
			}
			entity.setAnswer(tmpAnswers);
		} else {
			entity.setAnswer(resource.getAnswerInternal());
		}
	}

	private void setAnswer4Investment(QuestionResource resource, Question entity) {
		if (resource.getAnswerInternal() != null && resource.getAnswerInternal() instanceof List) {
			List<String> tmpAnswers = new ArrayList<>();
			for (Object tmpAnswer : (ArrayList<?>) resource.getAnswerInternal()) {
				Investment investment = mongoOperations.findById(tmpAnswer, Investment.class);
				tmpAnswers.add(investment.getName());
			}
			entity.setAnswer(tmpAnswers);
		} else {
			entity.setAnswer(resource.getAnswerInternal());
		}
	}

	private void setAnswer4NkdAux(QuestionResource resource, Question entity) {
		if (resource.getAnswerInternal() != null && resource.getAnswerInternal() instanceof List) {
			List<String> tmpAnswers = new ArrayList<>();
			for (Object tmpAnswer : (ArrayList<?>) resource.getAnswerInternal()) {
				Nkd nkd = mongoOperations.findById(tmpAnswer, Nkd.class);
				tmpAnswers.add(nkd.getArea() + "." + nkd.getActivity() + " - " + nkd.getActivityName());
			}
			entity.setAnswer(tmpAnswers);
		} else {
			entity.setAnswer(resource.getAnswerInternal());
		}
	}

}
