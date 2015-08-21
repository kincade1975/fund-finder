package hr.betaware.fundfinder.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hr.betaware.fundfinder.enums.LinkOperator;
import hr.betaware.fundfinder.enums.QuestionType;
import hr.betaware.fundfinder.resource.CityResource;
import hr.betaware.fundfinder.resource.CountyResource;
import hr.betaware.fundfinder.resource.NkdResource;
import hr.betaware.fundfinder.resource.QuestionResource;
import hr.betaware.fundfinder.service.ConfigurationService;

@RestController
@RequestMapping(value = { "/api/v1/configuration" })
public class ConfigurationController {

	@Autowired
	private ConfigurationService configurationService;

	@RequestMapping(method = RequestMethod.GET, value="/question")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
	public List<QuestionResource> getQuestions(@RequestParam String entityType) {
		return configurationService.getQuestions(entityType);
	}

	@RequestMapping(method = RequestMethod.GET, value="/question/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
	public QuestionResource getQuestion(@PathVariable Integer id, @RequestParam String entityType) {
		return configurationService.getQuestion(entityType, id);
	}

	@RequestMapping(method = RequestMethod.POST, value="/question")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public QuestionResource saveQuestion(@RequestBody QuestionResource resource) {
		return configurationService.saveQuestion(resource);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/question/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public void deleteQuestion(@PathVariable Integer id) {
		configurationService.deleteQuestion(id);
	}

	@RequestMapping(method = RequestMethod.GET, value="/question/types")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public List<String> getQuestionTypes() {
		List<String> result = new ArrayList<>();
		for (QuestionType type : QuestionType.values()) {
			result.add(type.toString());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value="/cities")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public List<CityResource> getCities() {
		return configurationService.getCities();
	}

	@RequestMapping(method = RequestMethod.GET, value="/counties")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public List<CountyResource> getCounties() {
		return configurationService.getCounties();
	}

	@RequestMapping(method = RequestMethod.GET, value="/nkds")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public List<NkdResource> getNkds() {
		return configurationService.getNkds();
	}

	@RequestMapping(method = RequestMethod.GET, value="/operators/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR')")
	public List<LinkOperator> getOperators(@PathVariable Integer id) {
		return configurationService.getOperators(id);
	}

	@RequestMapping(method = RequestMethod.GET, value="/linkQuestion")
	public void linkQuestion(@RequestParam Integer tenderQuestionId, @RequestParam List<Integer> companyQuestionId, @RequestParam LinkOperator linkOperator) {
		configurationService.linkQuestion(tenderQuestionId, companyQuestionId, linkOperator);
	}

}
