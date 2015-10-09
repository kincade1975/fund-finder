package hr.betaware.fundfinder.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.Answer;
import hr.betaware.fundfinder.domain.City;
import hr.betaware.fundfinder.domain.Company;
import hr.betaware.fundfinder.domain.Question;
import hr.betaware.fundfinder.domain.Tender;
import hr.betaware.fundfinder.domain.User;
import hr.betaware.fundfinder.enums.EntityType;
import hr.betaware.fundfinder.enums.LinkOperator;
import hr.betaware.fundfinder.enums.QuestionType;
import hr.betaware.fundfinder.resource.QuestionResource;
import hr.betaware.fundfinder.resource.TenderResource;
import hr.betaware.fundfinder.resource.assembler.AnswerResourceAssembler;
import hr.betaware.fundfinder.resource.assembler.TenderResourceAssembler;

@Service
public class OpportunityService {

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private UserService userService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private TenderResourceAssembler tenderResourceAssembler;

	@Autowired
	private AnswerResourceAssembler answerResourceAssembler;

	@Autowired
	private FileService fileService;

	public List<TenderResource> findTenders(Principal principal) {
		List<TenderResource> result = new ArrayList<>();

		User user = userService.getUser4Principal(principal);
		Company company = user.getCompany();

		Query query = new Query();
		query.addCriteria(Criteria.where("active").is(Boolean.TRUE));
		query.with(new Sort(Direction.DESC, "last_modified"));

		List<Tender> tenders = mongoOperations.find(query, Tender.class);
		for (Tender tender : tenders) {
			if (isAcceptableTender(tender, company, user)) {
				Map<Integer, Answer> answers = new HashMap<>();
				for (Answer answer : tender.getAnswers()) {
					answers.put(answer.getQuestionId(), answer);
				}

				List<QuestionResource> questions = new ArrayList<>();
				for (QuestionResource questionResource : configurationService.getQuestions(EntityType.TENDER.toString())) {
					if (answers.containsKey(questionResource.getIdentificator())) {
						questionResource.setAnswer(answerResourceAssembler.toResource(answers.get(questionResource.getIdentificator())));
					}
					questions.add(questionResource);
				}

				TenderResource tenderResource = tenderResourceAssembler.toResource(tender);
				tenderResource.setBase64(fileService.getMetadata(tenderResource.getImage()).getBase64());
				tenderResource.setQuestions(questions);

				result.add(tenderResource);
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private boolean isAcceptableTender(Tender tender, Company company, User user) {
		boolean result = true;

		for (Answer tenderAnswer : tender.getAnswers()) {
			Question tenderQuestion = mongoOperations.findById(tenderAnswer.getQuestionId(), Question.class);
			if (tenderQuestion.getLinkQuestionId() != null) {
				for (Integer questionId : tenderQuestion.getLinkQuestionId()) {
					Question companyQuestion = mongoOperations.findById(questionId, Question.class);
					Answer companyAnswer = getCompanyAnswer(company, questionId);

					if (tenderQuestion.getType() == QuestionType.COUNTY && companyQuestion.getType() == QuestionType.CITY) {
						if (tenderQuestion.getLinkOperator() == LinkOperator.IN) {
							if (tenderAnswer.getValue() == null || tenderAnswer.getValue() instanceof Set<?>) {
								result = false;
							}

							if (tenderAnswer.getValue() instanceof ArrayList<?>) {
								ArrayList<String> tenderValue = (ArrayList<String>) tenderAnswer.getValue();
								if (!tenderValue.contains(getCity(companyAnswer.getValue()).getCounty().getName())) {
									result = false;
								} else {
									result = true;
								}
							}
						}
					} else if (tenderQuestion.getType() == QuestionType.MULTI_SELECT && companyQuestion.getType() == QuestionType.SELECT) {
						if (tenderQuestion.getLinkOperator() == LinkOperator.IN) {
							if (tenderAnswer.getValue() == null || tenderAnswer.getValue() instanceof Set<?>) {
								result = false;
							}

							if (companyAnswer.getValue() instanceof String && tenderAnswer.getValue() instanceof ArrayList<?>) {
								String companyValue = (String) companyAnswer.getValue();
								ArrayList<String> tenderValue = (ArrayList<String>) tenderAnswer.getValue();
								if (!tenderValue.contains(companyValue)) {
									result = false;
								} else {
									result = true;
								}
							}
						}
					} else if (tenderQuestion.getType() == QuestionType.NKD_AUX && (companyQuestion.getType() == QuestionType.NKD || companyQuestion.getType() == QuestionType.NKD_AUX)) {
						if (tenderQuestion.getLinkOperator() == LinkOperator.IN) {
							if (tenderAnswer.getValue() == null || tenderAnswer.getValue() instanceof Set<?>) {
								result = false;
							}

							if (companyAnswer.getValue() instanceof ArrayList<?> && tenderAnswer.getValue() instanceof ArrayList<?>) {
								ArrayList<String> companyValue = (ArrayList<String>) companyAnswer.getValue();
								ArrayList<String> tenderValue = (ArrayList<String>) tenderAnswer.getValue();
								if (ListUtils.intersection(companyValue, tenderValue).isEmpty()) {
									result = false;
								} else {
									result = true;
								}
							}
						}
					}
				}

				if (!result) {
					return false;
				}
			} else {
				if (tenderQuestion.getType() == QuestionType.INVESTMENT) {
					if (tenderAnswer.getValueInternal() instanceof ArrayList<?>) {
						ArrayList<Integer> tenderValue = (ArrayList<Integer>) tenderAnswer.getValueInternal();
						if (user.getInvestments() == null || ListUtils.intersection(user.getInvestments(), tenderValue).isEmpty()) {
							result = false;
						}
					}
				}
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private City getCity(Object name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").in((ArrayList<String>) name));
		return mongoOperations.findOne(query, City.class);
	}

	private Answer getCompanyAnswer(Company company, Integer questionId) {
		if (company.getAnswers() != null) {
			for (Answer answer : company.getAnswers()) {
				if (answer.getQuestionId().equals(questionId)) {
					return answer;
				}
			}
		}
		return null;
	}

}
