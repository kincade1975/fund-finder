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

import etm.core.monitor.EtmPoint;
import hr.betaware.fundfinder.enums.LinkOperator;
import hr.betaware.fundfinder.enums.QuestionMetadata;
import hr.betaware.fundfinder.enums.QuestionType;
import hr.betaware.fundfinder.resource.CityResource;
import hr.betaware.fundfinder.resource.CountyResource;
import hr.betaware.fundfinder.resource.NkdResource;
import hr.betaware.fundfinder.resource.QuestionResource;
import hr.betaware.fundfinder.service.ConfigurationService;
import hr.betaware.fundfinder.service.EtmService;

@RestController
@RequestMapping(value = { "/api/v1/configuration", "/e/api/v1/configuration" })
public class ConfigurationController {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private EtmService etmService;

	@RequestMapping(method = RequestMethod.GET, value="/question")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public List<QuestionResource> getQuestions(@RequestParam String entityType) {
		EtmPoint point = etmService.createPoint("ConfigurationController.getQuestions");
		try {
			return configurationService.getQuestions(entityType);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/question/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public QuestionResource getQuestion(@PathVariable Integer id, @RequestParam String entityType) {
		EtmPoint point = etmService.createPoint("ConfigurationController.getQuestion");
		try {
			return configurationService.getQuestion(entityType, id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value="/question")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public QuestionResource saveQuestion(@RequestBody QuestionResource resource) {
		EtmPoint point = etmService.createPoint("ConfigurationController.saveQuestion");
		try {
			return configurationService.saveQuestion(resource);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/question/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public void deleteQuestion(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("ConfigurationController.deleteQuestion");
		try {
			configurationService.deleteQuestion(id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/question/types")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public List<String> getQuestionTypes() {
		EtmPoint point = etmService.createPoint("ConfigurationController.getQuestionTypes");
		try {
			List<String> result = new ArrayList<>();
			for (QuestionType type : QuestionType.values()) {
				result.add(type.toString());
			}
			return result;
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/question/metadata")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public List<String> getQuestionMetadata() {
		EtmPoint point = etmService.createPoint("ConfigurationController.getQuestionMetadata");
		try {
			List<String> result = new ArrayList<>();
			for (QuestionMetadata metadata : QuestionMetadata.values()) {
				result.add(metadata.toString());
			}
			return result;
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/cities")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public List<CityResource> getCities() {
		EtmPoint point = etmService.createPoint("ConfigurationController.getCities");
		try {
			return configurationService.getCities();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/counties")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public List<CountyResource> getCounties() {
		EtmPoint point = etmService.createPoint("ConfigurationController.getCounties");
		try {
			return configurationService.getCounties();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/nkds")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO','ROLE_USER')")
	public List<NkdResource> getNkds() {
		EtmPoint point = etmService.createPoint("ConfigurationController.getNkds");
		try {
			return configurationService.getNkds();
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/operators/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public List<LinkOperator> getOperators(@PathVariable Integer id) {
		EtmPoint point = etmService.createPoint("ConfigurationController.getOperators");
		try {
			return configurationService.getOperators(id);
		} finally {
			etmService.collect(point);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/linkQuestion")
	@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMINISTRATOR','ROLE_ADMINISTRATOR_RO')")
	public void linkQuestion(@RequestParam Integer tenderQuestionId, @RequestParam List<Integer> companyQuestionId, @RequestParam LinkOperator linkOperator) {
		EtmPoint point = etmService.createPoint("ConfigurationController.linkQuestion");
		try {
			configurationService.linkQuestion(tenderQuestionId, companyQuestionId, linkOperator);
		} finally {
			etmService.collect(point);
		}
	}

}
