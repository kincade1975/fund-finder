package hr.betaware.fundfinder.resource;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import hr.betaware.fundfinder.controller.UserController;
import hr.betaware.fundfinder.domain.User;

@Component
public class UserResourceAssembler extends ResourceAssemblerSupport<User, UserResource> {

	public UserResourceAssembler() {
		super(UserController.class, UserResource.class);
	}

	@Override
	public UserResource toResource(User entity) {
		UserResource resource = new UserResource();
		resource.setIdentificator(entity.getId());
		resource.setUsername(entity.getUsername());
		resource.setFirstName(entity.getFirstName());
		resource.setLastName(entity.getLastName());
		resource.setRole(entity.getRole());
		return resource;
	}

}
