package hr.betaware.fundfinder.resource;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import hr.betaware.fundfinder.controller.ConfigurationController;
import hr.betaware.fundfinder.domain.City;

@Component
public class CityResourceAssembler extends ResourceAssemblerSupport<City, CityResource> {

	public CityResourceAssembler() {
		super(ConfigurationController.class, CityResource.class);
	}

	@Override
	public CityResource toResource(City entity) {
		CityResource resource = new CityResource();
		resource.setIdentificator(entity.getId());
		resource.setName(entity.getName());
		resource.setCounty(entity.getCounty());
		resource.setGroup(entity.getGroup());
		return resource;
	}

}
