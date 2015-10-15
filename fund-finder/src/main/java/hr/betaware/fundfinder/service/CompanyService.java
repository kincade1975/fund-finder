package hr.betaware.fundfinder.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import hr.betaware.fundfinder.domain.Answer;
import hr.betaware.fundfinder.domain.City;
import hr.betaware.fundfinder.domain.Company;
import hr.betaware.fundfinder.domain.Investment;
import hr.betaware.fundfinder.domain.Nkd;
import hr.betaware.fundfinder.domain.Question;
import hr.betaware.fundfinder.domain.User;
import hr.betaware.fundfinder.domain.User.Role;
import hr.betaware.fundfinder.enums.EntityType;
import hr.betaware.fundfinder.enums.QuestionMetadata;
import hr.betaware.fundfinder.enums.StatisticsType;
import hr.betaware.fundfinder.resource.CompanyResource;
import hr.betaware.fundfinder.resource.QuestionResource;
import hr.betaware.fundfinder.resource.UserResource;
import hr.betaware.fundfinder.resource.ValidationResource;
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

	public ValidationResource validateCompany(CompanyResource companyResource) {
		ValidationResource result = new ValidationResource();

		Query query = new Query();
		query.addCriteria(Criteria.where("oib").is(companyResource.getOib()));
		Company company = mongoOperations.findOne(query, Company.class);
		if (company != null && !company.getId().equals(companyResource.getIdentificator())) {
			result.getMessages().add(new String("Tvrtka sa OIB-om " + company.getOib() + " je već registrirana u Fund Finder sustavu."));
		}

		return result;
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

	public List<UserResource> getCompanies(String type, String label) {
		List<UserResource> result = new ArrayList<>();

		if (StatisticsType.valueOf(type) == StatisticsType.COMPANIES_BY_SECTOR) {
			Map<Integer, Nkd> nkds = new HashMap<>();
			Query query = new Query();
			query.addCriteria(Criteria.where("sector_name").is(label));
			for (Nkd nkd : mongoOperations.find(query, Nkd.class)) {
				nkds.put(nkd.getId(), nkd);
			}

			query = new Query();
			query.addCriteria(Criteria.where("metadata").is(QuestionMetadata.STATISTICS_SECTOR));
			Question question = mongoOperations.findOne(query, Question.class);

			for (Company company : mongoOperations.findAll(Company.class)) {
				for (Answer answer : company.getAnswers()) {
					if (answer.getQuestionId().equals(question.getId()) && answer.getValueInternal() != null && nkds.containsKey((answer.getValueInternal()))) {
						CompanyResource companyResource = new CompanyResource();
						companyResource.setIdentificator(company.getId());
						companyResource.setName(company.getName());
						companyResource.setOib(company.getOib());

						query = new Query();
						query.addCriteria(Criteria.where("company").is(company));
						User user = mongoOperations.findOne(query, User.class);

						UserResource userResource = new UserResource();
						userResource.setIdentificator(user.getId());
						userResource.setFirstName(user.getFirstName());
						userResource.setLastName(user.getLastName());
						userResource.setCompany(companyResource);

						result.add(userResource);
					}
				}
			}
		} else if (StatisticsType.valueOf(type) == StatisticsType.COMPANIES_BY_LOCATION) {
			Map<Integer, City> cities = new HashMap<>();
			for (City city : mongoOperations.findAll(City.class)) {
				cities.put(city.getId(), city);
			}

			Query query = new Query();
			query.addCriteria(Criteria.where("metadata").is(QuestionMetadata.STATISTICS_LOCATION));
			Question question = mongoOperations.findOne(query, Question.class);

			for (Company company : mongoOperations.findAll(Company.class)) {
				for (Answer answer : company.getAnswers()) {
					if (answer.getQuestionId().equals(question.getId()) && answer.getValueInternal() != null) {
						if (cities.containsKey(answer.getValueInternal()) && cities.get(answer.getValueInternal()).getCounty().getName().equals(label)) {
							CompanyResource companyResource = new CompanyResource();
							companyResource.setIdentificator(company.getId());
							companyResource.setName(company.getName());
							companyResource.setOib(company.getOib());

							query = new Query();
							query.addCriteria(Criteria.where("company").is(company));
							User user = mongoOperations.findOne(query, User.class);

							UserResource userResource = new UserResource();
							userResource.setIdentificator(user.getId());
							userResource.setFirstName(user.getFirstName());
							userResource.setLastName(user.getLastName());
							userResource.setCompany(companyResource);

							result.add(userResource);
						}
					}
				}
			}
		} else if (StatisticsType.valueOf(type) == StatisticsType.INVESTMENTS) {
			Map<Integer, String> investments = new HashMap<>();
			for (Investment investment : mongoOperations.findAll(Investment.class)) {
				investments.put(investment.getId(), investment.getName());
			}

			Query query = new Query();
			query.addCriteria(Criteria.where("role").is(Role.ROLE_USER));

			for (User user : mongoOperations.find(query, User.class)) {
				for (Integer investmentId : user.getInvestments()) {
					if (investments.get(investmentId).equals(label)) {
						UserResource userResource = new UserResource();
						userResource.setIdentificator(user.getId());
						userResource.setFirstName(user.getFirstName());
						userResource.setLastName(user.getLastName());

						Company company = user.getCompany();
						CompanyResource companyResource = new CompanyResource();
						companyResource.setIdentificator(company.getId());
						companyResource.setName(company.getName());
						companyResource.setOib(company.getOib());

						userResource.setCompany(companyResource);

						result.add(userResource);
					}
				}
			}
		} else if (StatisticsType.valueOf(type) == StatisticsType.REVENUES) {
			Query query = new Query();
			query.addCriteria(Criteria.where("metadata").is(QuestionMetadata.STATISTICS_REVENUE));
			Question question = mongoOperations.findOne(query, Question.class);

			for (Company company : mongoOperations.findAll(Company.class)) {
				for (Answer answer : company.getAnswers()) {
					if (answer.getQuestionId().equals(question.getId()) && answer.getValueInternal() != null && answer.getValue().equals(label)) {
						CompanyResource companyResource = new CompanyResource();
						companyResource.setIdentificator(company.getId());
						companyResource.setName(company.getName());
						companyResource.setOib(company.getOib());

						query = new Query();
						query.addCriteria(Criteria.where("company").is(company));
						User user = mongoOperations.findOne(query, User.class);

						UserResource userResource = new UserResource();
						userResource.setIdentificator(user.getId());
						userResource.setFirstName(user.getFirstName());
						userResource.setLastName(user.getLastName());
						userResource.setCompany(companyResource);

						result.add(userResource);
					}
				}
			}
		}

		return result;
	}

}
