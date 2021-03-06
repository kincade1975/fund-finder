package hr.betaware.fundfinder.service;

import java.io.File;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import hr.betaware.fundfinder.domain.Article;
import hr.betaware.fundfinder.domain.Impression;
import hr.betaware.fundfinder.domain.Tender;

@Component
public class CleanerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CleanerService.class);

	@Value(value = "${file.repository}")
	private String fileRepository;

	@Value(value = "${cleaner.remove-impressions-older-then}")
	private Integer removeImpressionsOlderThen;

	@Autowired
	private MongoOperations mongoOperations;

	@Scheduled(cron = "${cleaner.cron-expression}")
	public void clean() {
		LOGGER.debug("{} started...", CleanerService.class.getSimpleName());

		// clean files (images) that are no longer used (either for Article, either for Tender)
		File repository = new File(fileRepository);
		for (File file : repository.listFiles()) {
			if (count(file.getName(), Article.class) == 0 && count(file.getName(), Tender.class) == 0) {
				LOGGER.debug("Deleting file {}", file.getName());
				file.delete();
			}
		}

		// delete all records older then configured value (in months) from impression collection
		DateTime threshold = new DateTime().minusMonths(removeImpressionsOlderThen);
		LOGGER.debug("Removing all impressions older then {} from database", threshold);

		Query query = new Query();
		query.addCriteria(Criteria.where("time_created").lt(threshold));
		mongoOperations.remove(query, Impression.class);

		LOGGER.debug("{} ended...", CleanerService.class.getSimpleName());
	}

	private long count(String fileName, Class<?> clazz) {
		Query query = new Query();
		query.addCriteria(Criteria.where("image").is(fileName));
		return mongoOperations.count(query, clazz);
	}
}
