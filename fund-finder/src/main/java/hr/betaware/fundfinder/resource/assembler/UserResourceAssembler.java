package hr.betaware.fundfinder.resource.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import hr.betaware.fundfinder.controller.UserController;
import hr.betaware.fundfinder.domain.User;
import hr.betaware.fundfinder.resource.UserResource;

@Component
public class UserResourceAssembler extends ResourceAssemblerSupport<User, UserResource> {

	@Autowired
	private CompanyResourceAssembler companyResourceAssembler;

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
		resource.setCompanyName(entity.getCompanyName());
		if (entity.getCompany() != null) {
			resource.setCompany(companyResourceAssembler.toResource(entity.getCompany()));
		}
		resource.setTimeCreated(entity.getTimeCreated());
		resource.setLastModified(entity.getLastModified());
		return resource;
	}

	public User updateEntity(User entity, UserResource resource) {
		entity.setFirstName(resource.getFirstName());
		entity.setLastName(resource.getLastName());
		if (!StringUtils.isEmpty(resource.getPassword())) {
			entity.setPassword(new MessageDigestPasswordEncoder("SHA-1").encodePassword(resource.getPassword(), null));
		}
		return entity;
	}

}
