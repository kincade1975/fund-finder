package hr.betaware.fundfinder.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.City;
import hr.betaware.fundfinder.domain.County;
import hr.betaware.fundfinder.domain.Nkd;
import hr.betaware.fundfinder.domain.Question;
import hr.betaware.fundfinder.enums.EntityType;
import hr.betaware.fundfinder.enums.LinkOperator;
import hr.betaware.fundfinder.resource.CityResource;
import hr.betaware.fundfinder.resource.CountyResource;
import hr.betaware.fundfinder.resource.NkdResource;
import hr.betaware.fundfinder.resource.QuestionResource;
import hr.betaware.fundfinder.resource.assembler.CityResourceAssembler;
import hr.betaware.fundfinder.resource.assembler.CountyResourceAssembler;
import hr.betaware.fundfinder.resource.assembler.NkdResourceAssembler;
import hr.betaware.fundfinder.resource.assembler.QuestionResourceAssembler;

@Service
public class ConfigurationService {

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private QuestionResourceAssembler questionResourceAssembler;

	@Autowired
	private CityResourceAssembler cityResourceAssembler;

	@Autowired
	private CountyResourceAssembler countyResourceAssembler;

	@Autowired
	private NkdResourceAssembler nkdResourceAssembler;

	public List<QuestionResource> getQuestions(String entityType) {
		Query query = new Query();
		query.with(new Sort(Direction.ASC, "index"));
		query.addCriteria(Criteria.where("entity_type").is(EntityType.valueOf(entityType.toUpperCase())));

		List<Question> entities = mongoOperations.find(query, Question.class);

		return questionResourceAssembler.toResources(entities);
	}

	public QuestionResource getQuestion(String entityType, Integer id) {
		Question entity = mongoOperations.findById(id, Question.class);
		if (entity == null) {
			entity = new Question();
			entity.setEntityType(EntityType.valueOf(entityType.toUpperCase()));
			entity.setIndex(getNextIndex());
		}
		return questionResourceAssembler.toResource(entity);
	}

	private Integer getNextIndex() {
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "index"));
		query.limit(1);

		List<Question> lstQuestion = mongoOperations.find(query, Question.class);
		if (lstQuestion.isEmpty()) {
			return 0;
		} else {
			return lstQuestion.get(0).getIndex() + 1;
		}
	}

	public QuestionResource saveQuestion(QuestionResource resource) {
		Question entity = null;
		if (resource.getIdentificator() == null) {
			entity = questionResourceAssembler.createEntity(resource);
		} else {
			entity = mongoOperations.findById(resource.getIdentificator(), Question.class);
			entity = questionResourceAssembler.updateEntity(entity, resource);
		}

		mongoOperations.save(entity);

		return questionResourceAssembler.toResource(entity);
	}

	public void deleteQuestion(Integer id) {
		Question entity = mongoOperations.findById(id, Question.class);
		mongoOperations.remove(entity);
	}

	public List<CityResource> getCities() {
		Query query = new Query();
		query.with(new Sort(Direction.ASC, "name"));
		return cityResourceAssembler.toResources(mongoOperations.find(query, City.class));
	}

	public List<CountyResource> getCounties() {
		Query query = new Query();
		query.with(new Sort(Direction.ASC, "name"));
		return countyResourceAssembler.toResources(mongoOperations.find(query, County.class));
	}

	public List<NkdResource> getNkds() {
		Query query = new Query();
		query.with(new Sort(Direction.ASC, "sector"));
		query.with(new Sort(Direction.ASC, "area"));
		query.with(new Sort(Direction.ASC, "activity"));
		return nkdResourceAssembler.toResources(mongoOperations.find(query, Nkd.class));
	}

	public List<LinkOperator> getOperators(Integer id) {
		Question question = mongoOperations.findById(id, Question.class);

		List<LinkOperator> result = new ArrayList<>();
		for (LinkOperator operator : question.getType().getOperators()) {
			result.add(operator);
		}

		return result;
	}

	public void linkQuestion(Integer tenderQuestionId, List<Integer> companyQuestionId, LinkOperator linkOperator) {
		Question question = mongoOperations.findById(tenderQuestionId, Question.class);
		question.setLinkQuestionId((companyQuestionId.isEmpty()) ? null : companyQuestionId);
		question.setLinkOperator(linkOperator);
		mongoOperations.save(question);
	}

}
