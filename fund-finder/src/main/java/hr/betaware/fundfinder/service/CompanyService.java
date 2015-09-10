package hr.betaware.fundfinder.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import hr.betaware.fundfinder.domain.Answer;
import hr.betaware.fundfinder.domain.Company;
import hr.betaware.fundfinder.domain.User;
import hr.betaware.fundfinder.enums.EntityType;
import hr.betaware.fundfinder.resource.CompanyResource;
import hr.betaware.fundfinder.resource.QuestionResource;
import hr.betaware.fundfinder.resource.assembler.AnswerResourceAssembler;
import hr.betaware.fundfinder.resource.assembler.CompanyResourceAssembler;

@Service
public class CompanyService {

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private UserService userService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CompanyResourceAssembler companyResourceAssembler;

	@Autowired
	private AnswerResourceAssembler answerResourceAssembler;

	public CompanyResource findCompany(User user) {
		CompanyResource companyResource = null;
		Company company = user.getCompany();
		Map<Integer, Answer> answers = new HashMap<>();

		if (company == null) {
			companyResource = new CompanyResource();
		} else {
			if (company.getAnswers() != null) {
				for (Answer answer : company.getAnswers()) {
					answers.put(answer.getQuestionId(), answer);
				}
			}
			companyResource = companyResourceAssembler.toResource(company);
		}

		List<QuestionResource> questions = new ArrayList<>();
		for (QuestionResource questionResource : configurationService.getQuestions(EntityType.COMPANY.toString())) {
			if (answers.containsKey(questionResource.getIdentificator())) {
				questionResource.setAnswer(answerResourceAssembler.toResource(answers.get(questionResource.getIdentificator())));
			}
			questions.add(questionResource);
		}
		companyResource.setQuestions(questions);

		incompleteCheck(companyResource);

		return companyResource;
	}

	public CompanyResource findCompany(Principal principal) {
		return findCompany(userService.getUser4Principal(principal));
	}

	/**
	 * Method checks if company profile is incomplete and sets appropriate flag.
	 * @param companyResource
	 */
	private void incompleteCheck(CompanyResource companyResource) {
		if (StringUtils.isEmpty(companyResource.getName()) || StringUtils.isEmpty(companyResource.getOib())) {
			companyResource.setIncomplete(Boolean.TRUE);
			return;
		}

		for (QuestionResource questionResource : companyResource.getQuestions()) {
			if (questionResource.getMandatory() && questionResource.getAnswer().getValue() == null) {
				companyResource.setIncomplete(Boolean.TRUE);
				return;
			}
		}
	}

	public CompanyResource saveCompany(Principal principal, CompanyResource companyResource) {
		Company company = null;
		if (companyResource.getIdentificator() == null) {
			company = companyResourceAssembler.createEntity(companyResource);
		} else {
			company = mongoOperations.findById(companyResource.getIdentificator(), Company.class);
			company = companyResourceAssembler.updateEntity(company, companyResource);
		}

		mongoOperations.save(company);

		User user = userService.getUser4Principal(principal);
		user.setCompany(company);
		user.setCompanyName(company.getName());
		mongoOperations.save(user);

		return findCompany(principal);
	}

}
