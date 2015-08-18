package hr.betaware.fundfinder.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.betaware.fundfinder.domain.Question;
import hr.betaware.fundfinder.resource.CityResource;
import hr.betaware.fundfinder.resource.NkdResource;
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

	@RequestMapping(method = RequestMethod.GET, value="/company/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
	public QuestionResource getCompanyQuestion(@PathVariable Integer id) {
		return configurationService.getCompanyQuestion(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/company")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public QuestionResource saveCompanyQuestion(@RequestBody QuestionResource resource) {
		return configurationService.saveCompanyQuestion(resource);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/company/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public void deleteCompanyQuestion(@PathVariable Integer id) {
		configurationService.deleteCompanyQuestion(id);
	}

	@RequestMapping(method = RequestMethod.GET, value="/question/types")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public List<String> getQuestionTypes() {
		List<String> result = new ArrayList<>();
		for (Question.Type type : Question.Type.values()) {
			result.add(type.toString());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value="/cities")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public List<CityResource> getCities() {
		return configurationService.getCities();
	}

	@RequestMapping(method = RequestMethod.GET, value="/nkds")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public List<NkdResource> getNkds() {
		return configurationService.getNkds();
	}

}
