package hr.betaware.fundfinder.resource;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import hr.betaware.fundfinder.controller.ConfigurationController;
import hr.betaware.fundfinder.domain.Nkd;

@Component
public class NkdResourceAssembler extends ResourceAssemblerSupport<Nkd, NkdResource> {

	public NkdResourceAssembler() {
		super(ConfigurationController.class, NkdResource.class);
	}

	@Override
	public NkdResource toResource(Nkd entity) {
		NkdResource resource = new NkdResource();
		resource.setIdentificator(entity.getId());
		resource.setSector(entity.getSector());
		resource.setSectorName(entity.getSectorName());
		resource.setArea(entity.getArea());
		resource.setActivity(entity.getActivity());
		resource.setActivityName(entity.getActivityName());
		return resource;
	}

}
