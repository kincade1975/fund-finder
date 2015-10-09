package hr.betaware.fundfinder.service;

import java.io.File;

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
import hr.betaware.fundfinder.domain.Tender;

@Component
public class CleanerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CleanerService.class);

	@Value(value = "${file.repository}")
	private String fileRepository;

	@Autowired
	private MongoOperations mongoOperations;

	@Scheduled(cron = "${cleaner.cron-expression}")
	public void clean() {
		LOGGER.debug("{} started...", CleanerService.class.getSimpleName());

		File repository = new File(fileRepository);
		for (File file : repository.listFiles()) {
			if (count(file.getName(), Article.class) == 0 && count(file.getName(), Tender.class) == 0) {
				LOGGER.debug("Deleting file {}", file.getName());
				file.delete();
			}
		}

		LOGGER.debug("{} ended...", CleanerService.class.getSimpleName());
	}

	private long count(String fileName, Class<?> clazz) {
		Query query = new Query();
		query.addCriteria(Criteria.where("image").is(fileName));
		return mongoOperations.count(query, clazz);
	}
}
