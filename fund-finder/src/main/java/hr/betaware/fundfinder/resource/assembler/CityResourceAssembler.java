package hr.betaware.fundfinder.resource.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import hr.betaware.fundfinder.controller.ConfigurationController;
import hr.betaware.fundfinder.domain.City;
import hr.betaware.fundfinder.resource.CityResource;

@Component
public class CityResourceAssembler extends ResourceAssemblerSupport<City, CityResource> {

	@Autowired
	private CountyResourceAssembler countyResourceAssembler;

	public CityResourceAssembler() {
		super(ConfigurationController.class, CityResource.class);
	}

	@Override
	public CityResource toResource(City entity) {
		CityResource resource = new CityResource();
		resource.setIdentificator(entity.getId());
		resource.setName(entity.getName());
		resource.setCounty(countyResourceAssembler.toResource(entity.getCounty()));
		resource.setGroup(entity.getGroup());
		return resource;
	}

}
