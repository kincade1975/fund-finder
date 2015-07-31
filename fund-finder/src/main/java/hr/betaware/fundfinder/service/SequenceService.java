package hr.betaware.fundfinder.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.Sequence;

@Service
public class SequenceService {

	public final static String COLLECTION = "sequence";
	public final static String SEQUENCE_USER = "sequenceUser";

	private final MongoTemplate mongoTemplate;
	private final MongoOperations mongoOperations;

	@Autowired
	public SequenceService(MongoTemplate mongoTemplate, MongoOperations mongoOperations) {
		this.mongoTemplate = mongoTemplate;
		this.mongoOperations = mongoOperations;

		if (!this.mongoTemplate.collectionExists(SequenceService.COLLECTION)) {
			this.mongoTemplate.createCollection(SequenceService.COLLECTION);
		}

		if (mongoTemplate.findById(SEQUENCE_USER, Sequence.class) == null) {
			this.mongoTemplate.insert(new Sequence(SEQUENCE_USER, 0));
		}
	}

	public Integer getNextSequence(String counter) {
		return mongoOperations.findAndModify(query(where("_id").is(counter)), new Update().inc("seq", Integer.valueOf(1)), options().returnNew(true), Sequence.class).getSeq();
	}

}
