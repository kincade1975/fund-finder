package hr.betaware.fundfinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.betaware.fundfinder.resource.QuestionResource;
import hr.betaware.fundfinder.service.ConfigurationService;

@RestController
@RequestMapping(value = { "/api/v1/configuration" })
public class ConfigurationController {

	@Autowired
	private ConfigurationService configurationService;

	@RequestMapping(method = RequestMethod.GET, value = "/company")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
	public List<QuestionResource> getCompanyQuestions() {
		return configurationService.getCompanyQuestions();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/company")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public QuestionResource saveInvestment(@RequestBody QuestionResource resource) {
		return configurationService.saveCompanyQuestions(resource);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/company/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public void deleteArticle(@PathVariable Integer id) {
		configurationService.deleteCompanyQuestions(id);
	}

}
