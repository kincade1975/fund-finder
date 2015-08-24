package hr.betaware.fundfinder.resource.assembler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import hr.betaware.fundfinder.controller.TenderController;
import hr.betaware.fundfinder.domain.Question;
import hr.betaware.fundfinder.domain.Tender;
import hr.betaware.fundfinder.resource.QuestionResource;
import hr.betaware.fundfinder.resource.TenderResource;
import hr.betaware.fundfinder.service.SequenceService;

@Component
public class TenderResourceAssembler extends ResourceAssemblerSupport<Tender, TenderResource> {

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private QuestionResourceAssembler questionResourceAssembler;

	public TenderResourceAssembler() {
		super(TenderController.class, TenderResource.class);
	}

	@Override
	public TenderResource toResource(Tender entity) {
		TenderResource resource = new TenderResource();
		resource.setIdentificator(entity.getId());
		resource.setName(entity.getName());
		resource.setActive(entity.getActive());
		if (entity.getQuestions() != null) {
			resource.setQuestions(questionResourceAssembler.toResources(entity.getQuestions()));
		}
		resource.setTimeCreated(entity.getTimeCreated());
		resource.setLastModified(entity.getLastModified());
		return resource;
	}

	public Tender createEntity(TenderResource resource) {
		Tender entity = new Tender();
		entity.setId(sequenceService.getNextSequence(SequenceService.SEQUENCE_TENDER));
		entity.setName(resource.getName());
		entity.setActive(resource.getActive());

		if (resource.getQuestions() != null) {
			List<Question> questions = new ArrayList<>();
			for (QuestionResource questionResource : resource.getQuestions()) {
				questions.add(questionResourceAssembler.toEntity(questionResource));
			}
			entity.setQuestions(questions);
		}

		return entity;
	}

	public Tender updateEntity(Tender entity, TenderResource resource) {
		entity.setName(resource.getName());
		entity.setActive(resource.getActive());
		if (resource.getQuestions() != null) {
			List<Question> questions = new ArrayList<>();
			for (QuestionResource questionResource : resource.getQuestions()) {
				questions.add(questionResourceAssembler.toEntity(questionResource));
			}
			entity.setQuestions(questions);
		}
		return entity;
	}

}
