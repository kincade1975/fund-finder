package hr.betaware.fundfinder.resource;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import hr.betaware.fundfinder.controller.ConfigurationController;
import hr.betaware.fundfinder.domain.County;

@Component
public class CountyResourceAssembler extends ResourceAssemblerSupport<County, CountyResource> {

	public CountyResourceAssembler() {
		super(ConfigurationController.class, CountyResource.class);
	}

	@Override
	public CountyResource toResource(County entity) {
		CountyResource resource = new CountyResource();
		resource.setIdentificator(entity.getId());
		resource.setName(entity.getName());
		return resource;
	}

}
