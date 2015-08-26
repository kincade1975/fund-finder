package hr.betaware.fundfinder.resource.assembler;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import hr.betaware.fundfinder.controller.ConfigurationController;
import hr.betaware.fundfinder.domain.Answer;
import hr.betaware.fundfinder.resource.AnswerResource;

@Component
public class AnswerResourceAssembler extends ResourceAssemblerSupport<Answer, AnswerResource> {

	public AnswerResourceAssembler() {
		super(ConfigurationController.class, AnswerResource.class);
	}

	@Override
	public AnswerResource toResource(Answer entity) {
		AnswerResource resource = new AnswerResource();
		resource.setValue(entity.getValue());
		resource.setValueInternal(entity.getValueInternal());
		return resource;
	}

}
