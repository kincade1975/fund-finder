package hr.betaware.fundfinder.resource.assembler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import hr.betaware.fundfinder.controller.CompanyController;
import hr.betaware.fundfinder.domain.Answer;
import hr.betaware.fundfinder.domain.Company;
import hr.betaware.fundfinder.domain.Question;
import hr.betaware.fundfinder.resource.CompanyResource;
import hr.betaware.fundfinder.resource.QuestionResource;
import hr.betaware.fundfinder.service.SequenceService;

@Component
public class CompanyResourceAssembler extends ResourceAssemblerSupport<Company, CompanyResource> {

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private QuestionResourceAssembler questionResourceAssembler;

	public CompanyResourceAssembler() {
		super(CompanyController.class, CompanyResource.class);
	}

	@Override
	public CompanyResource toResource(Company entity) {
		CompanyResource resource = new CompanyResource();
		resource.setIdentificator(entity.getId());
		resource.setName(entity.getName());
		resource.setOib(entity.getOib());
		resource.setTimeCreated(entity.getTimeCreated());
		resource.setLastModified(entity.getLastModified());
		return resource;
	}

	public Company createEntity(CompanyResource resource) {
		Company entity = new Company();
		entity.setId(sequenceService.getNextSequence(SequenceService.SEQUENCE_COMPANY));
		entity.setName(resource.getName());
		entity.setOib(resource.getOib());

		if (resource.getQuestions() != null) {
			List<Answer> answers = new ArrayList<>();
			for (QuestionResource questionResource : resource.getQuestions()) {
				Question question = questionResourceAssembler.toEntity(questionResource);
				answers.add(new Answer(question.getId(), question.getAnswer().getValue(), question.getAnswer().getValueInternal()));
			}
			entity.setAnswers(answers);
		}

		return entity;
	}

	public Company updateEntity(Company entity, CompanyResource resource) {
		entity.setName(resource.getName());
		entity.setOib(resource.getOib());
		if (resource.getQuestions() != null) {
			List<Answer> answers = new ArrayList<>();
			for (QuestionResource questionResource : resource.getQuestions()) {
				Question question = questionResourceAssembler.toEntity(questionResource);
				answers.add(new Answer(question.getId(), question.getAnswer().getValue(), question.getAnswer().getValueInternal()));
			}
			entity.setAnswers(answers);
		}
		return entity;
	}

}
