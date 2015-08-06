package hr.betaware.fundfinder.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import hr.betaware.fundfinder.controller.InvestmentController;
import hr.betaware.fundfinder.domain.Investment;
import hr.betaware.fundfinder.service.SequenceService;

@Component
public class InvestmentResourceAssembler extends ResourceAssemblerSupport<Investment, InvestmentResource> {

	@Autowired
	private SequenceService sequenceService;

	public InvestmentResourceAssembler() {
		super(InvestmentController.class, InvestmentResource.class);
	}

	@Override
	public InvestmentResource toResource(Investment entity) {
		InvestmentResource resource = new InvestmentResource();
		resource.setIdentificator(entity.getId());
		resource.setName(entity.getName());
		resource.setDescription(entity.getDescription());
		resource.setTimeCreated(entity.getTimeCreated());
		resource.setLastModified(entity.getLastModified());
		return resource;
	}

	public Investment createEntity(InvestmentResource resource) {
		Investment entity = new Investment();
		entity.setId(sequenceService.getNextSequence(SequenceService.SEQUENCE_INVESTMENT));
		entity.setName(resource.getName());
		entity.setDescription(resource.getDescription());
		return entity;
	}

	public Investment updateEntity(Investment entity, InvestmentResource resource) {
		entity.setName(resource.getName());
		entity.setDescription(resource.getDescription());
		return entity;
	}

}
