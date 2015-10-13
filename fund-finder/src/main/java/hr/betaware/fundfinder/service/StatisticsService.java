package hr.betaware.fundfinder.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.Answer;
import hr.betaware.fundfinder.domain.City;
import hr.betaware.fundfinder.domain.Company;
import hr.betaware.fundfinder.domain.County;
import hr.betaware.fundfinder.domain.Investment;
import hr.betaware.fundfinder.domain.Nkd;
import hr.betaware.fundfinder.domain.Option;
import hr.betaware.fundfinder.domain.Question;
import hr.betaware.fundfinder.domain.User;
import hr.betaware.fundfinder.domain.User.Role;
import hr.betaware.fundfinder.enums.QuestionMetadata;
import hr.betaware.fundfinder.enums.StatisticsType;
import hr.betaware.fundfinder.resource.StatisticsResource;

@Service
public class StatisticsService {

	@Autowired
	private MongoOperations mongoOperations;

	@SuppressWarnings("unchecked")
	public StatisticsResource getCompaniesBySector() {
		Query query = new Query();
		query.addCriteria(Criteria.where("metadata").is(QuestionMetadata.STATISTICS_SECTOR));

		Question question = mongoOperations.findOne(query, Question.class);

		Map<String,Integer> counters = new HashMap<>();
		for (Nkd nkd : mongoOperations.findAll(Nkd.class)) {
			counters.put(nkd.getSectorName(), 0);
		}

		for (Company company : mongoOperations.findAll(Company.class)) {
			for (Answer answer : company.getAnswers()) {
				if (answer.getQuestionId().equals(question.getId())) {
					Nkd nkd = mongoOperations.findById(answer.getValueInternal(), Nkd.class);
					if (nkd != null) {
						counters.put(nkd.getSectorName(), counters.get(nkd.getSectorName()) + 1);
					}
				}
			}
		}

		counters = sortByValue(counters);

		StatisticsResource result = new StatisticsResource(StatisticsType.COMPANIES_BY_SECTOR);

		int cnt = 0;
		for (String key : counters.keySet()) {
			if (cnt == 5) {
				break;
			}
			if (counters.get(key) > 0) {
				result.getData().add(counters.get(key));
				result.getLabels().add(key);
				cnt++;
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public StatisticsResource getCompaniesByLocation() {
		Query query = new Query();
		query.addCriteria(Criteria.where("metadata").is(QuestionMetadata.STATISTICS_LOCATION));

		Question question = mongoOperations.findOne(query, Question.class);

		Map<String,Integer> counters = new HashMap<>();
		for (County county : mongoOperations.findAll(County.class)) {
			counters.put(county.getName(), 0);
		}

		for (Company company : mongoOperations.findAll(Company.class)) {
			for (Answer answer : company.getAnswers()) {
				if (answer.getQuestionId().equals(question.getId())) {
					City city = mongoOperations.findById(answer.getValueInternal(), City.class);
					if (city != null) {
						County county = city.getCounty();
						counters.put(county.getName(), counters.get(county.getName()) + 1);
					}
				}
			}
		}

		counters = sortByValue(counters);

		StatisticsResource result = new StatisticsResource(StatisticsType.COMPANIES_BY_LOCATION);

		int cnt = 0;
		for (String key : counters.keySet()) {
			if (cnt == 5) {
				break;
			}
			if (counters.get(key) > 0) {
				result.getData().add(counters.get(key));
				result.getLabels().add(key);
				cnt++;
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public StatisticsResource getTopInvestments() {
		Map<Integer, String> investments = new HashMap<>();
		for (Investment investment : mongoOperations.findAll(Investment.class)) {
			investments.put(investment.getId(), investment.getName());
		}

		Query query = new Query();
		query.addCriteria(Criteria.where("role").is(Role.ROLE_USER));

		Map<Integer,Integer> counters = new HashMap<>();
		for (User user : mongoOperations.find(query, User.class)) {
			for (Integer investmentId : user.getInvestments()) {
				if (counters.containsKey(investmentId)) {
					counters.put(investmentId, counters.get(investmentId) + 1);
				} else {
					counters.put(investmentId, 1);
				}
			}
		}

		counters = sortByValue(counters);

		StatisticsResource result = new StatisticsResource(StatisticsType.TOP_INVESTMENTS);

		int cnt = 0;
		for (Integer key : counters.keySet()) {
			if (cnt == 5) {
				break;
			}
			if (counters.get(key) > 0) {
				result.getData().add(counters.get(key));
				result.getLabels().add(investments.get(key));
				cnt++;
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public StatisticsResource getTopRevenues() {
		Query query = new Query();
		query.addCriteria(Criteria.where("metadata").is(QuestionMetadata.STATISTICS_REVENUE));

		Question question = mongoOperations.findOne(query, Question.class);

		Map<String,Integer> counters = new HashMap<>();
		for (Option option : question.getOptions()) {
			counters.put(option.getValue(), 0);
		}

		for (Company company : mongoOperations.findAll(Company.class)) {
			for (Answer answer : company.getAnswers()) {
				if (answer.getQuestionId().equals(question.getId())) {
					counters.put(answer.getValueInternal().toString(), counters.get(answer.getValueInternal()) + 1);
				}
			}
		}

		counters = sortByValue(counters);

		StatisticsResource result = new StatisticsResource(StatisticsType.TOP_REVENUES);

		int cnt = 0;
		for (String key : counters.keySet()) {
			if (cnt == 5) {
				break;
			}
			if (counters.get(key) > 0) {
				result.getData().add(counters.get(key));
				result.getLabels().add(key);
				cnt++;
			}
		}

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map sortByValue(Map unsortMap) {
		List list = new LinkedList(unsortMap.entrySet());

		Collections.sort(list, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

}
