package hr.betaware.fundfinder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.Question;
import hr.betaware.fundfinder.resource.QuestionResource;
import hr.betaware.fundfinder.resource.QuestionResourceAssembler;

@Service
public class ConfigurationService {

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private QuestionResourceAssembler questionResourceAssembler;

	public List<QuestionResource> getCompanyQuestions() {
		Query query = new Query();
		query.with(new Sort(Direction.ASC, "index"));
		query.addCriteria(Criteria.where("entity_type").is(Question.EntityType.COMPANY));

		List<Question> entities = mongoOperations.find(query, Question.class);

		return questionResourceAssembler.toResources(entities);
	}

	public QuestionResource saveCompanyQuestions(QuestionResource resource) {
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

	public void deleteCompanyQuestions(Integer id) {
		Question entity = mongoOperations.findById(id, Question.class);
		mongoOperations.remove(entity);
	}

}
