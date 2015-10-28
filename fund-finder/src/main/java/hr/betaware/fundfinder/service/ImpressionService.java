package hr.betaware.fundfinder.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.Article;
import hr.betaware.fundfinder.domain.Impression;
import hr.betaware.fundfinder.domain.Impression.EntityType;
import hr.betaware.fundfinder.domain.Tender;
import hr.betaware.fundfinder.domain.User;
import hr.betaware.fundfinder.enums.StatisticsPeriod;
import hr.betaware.fundfinder.resource.ImpressionResource;
import hr.betaware.fundfinder.resource.ImpressionStatisticsResource;

@Service
public class ImpressionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImpressionService.class);

	@Autowired
	private UserService userService;

	@Autowired
	private MongoOperations mongoOperations;

	private SimpleDateFormat monthDateFormat = new SimpleDateFormat("MM.yyyy");
	private SimpleDateFormat dateDateFormat = new SimpleDateFormat("dd.MM.yyyy");

	public void saveImpression(Principal principal, ImpressionResource resource) {
		try {
			User user = userService.getUser4Principal(principal);
			if (user != null) {
				Impression impression = new Impression();
				impression.setEntityType(EntityType.valueOf(resource.getEntityType()));
				impression.setEntityId(resource.getEntityId());
				impression.setUserId(user.getId());
				mongoOperations.save(impression);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public ImpressionStatisticsResource getImpressionStatistics(EntityType entityType, Integer entityId, StatisticsPeriod statisticsPeriod) {
		ImpressionStatisticsResource result = new ImpressionStatisticsResource();

		if (entityType == EntityType.ARTICLE) {
			result.setName(mongoOperations.findById(entityId, Article.class).getTitle());
		} else if (entityType == EntityType.TENDER) {
			result.setName(mongoOperations.findById(entityId, Tender.class).getName());
		}

		List<String> lstSeries = Arrays.asList("Ukupno pregleda", "Jedinstvenih pregleda");
		List<String> lstLabels = new ArrayList<>();
		List<Number> lstDataTotal = new ArrayList<>();
		List<Number> lstDataUnique = new ArrayList<>();

		List<DateTime> lstDateTime = new ArrayList<>();
		DateTime now = new DateTime();

		long cntTotal = 0;
		long cntUnique = 0;

		if (statisticsPeriod == StatisticsPeriod.LAST_7_DAYS) {
			for (int i=6; i>=0; i--) {
				DateTime dateTime = now.minusDays(i).withTime(0, 0, 0, 0);
				lstDateTime.add(dateTime);
				lstLabels.add(dateDateFormat.format(dateTime.toDate()));

				DateTime start = dateTime;
				DateTime end = dateTime.withTime(23, 59, 59, 999);

				// total
				Query query = new Query();
				query.addCriteria(Criteria.where("entity_type").is(entityType));
				query.addCriteria(Criteria.where("entity_id").is(entityId));
				query.addCriteria(Criteria.where("time_created").gte(start).lte(end));

				long cnt = mongoOperations.count(query, Impression.class);
				lstDataTotal.add(cnt);
				cntTotal = cntTotal + cnt;

				// unique
				Aggregation aggregation = newAggregation(
						match(Criteria.where("entity_type").is(entityType)),
						match(Criteria.where("entity_id").is(entityId)),
						match(Criteria.where("time_created").gte(start).lte(end)),
						group("userId").count().as("total"),
						project("total").and("userId").previousOperation(),
						sort(Sort.Direction.DESC, "total"));

				AggregationResults<AggregateResult> groupResults = mongoOperations.aggregate(aggregation, Impression.class, AggregateResult.class);
				lstDataUnique.add(groupResults.getMappedResults().size());
				cntUnique = cntUnique + groupResults.getMappedResults().size();
			}
		} else if (statisticsPeriod == StatisticsPeriod.LAST_6_MONTHS) {
			for (int i=5; i>=0; i--) {
				DateTime dateTime = (now.minusMonths(i)).withDayOfMonth(1).withTime(0, 0, 0, 0);
				lstDateTime.add(dateTime);
				lstLabels.add(monthDateFormat.format(dateTime.toDate()));

				DateTime start = dateTime;
				DateTime end = start.plusMonths(1).minusDays(1).withTime(23, 59, 59, 999);

				// total
				Query query = new Query();
				query.addCriteria(Criteria.where("entity_type").is(entityType));
				query.addCriteria(Criteria.where("entity_id").is(entityId));
				query.addCriteria(Criteria.where("time_created").gte(start).lte(end));

				long cnt = mongoOperations.count(query, Impression.class);
				lstDataTotal.add(cnt);
				cntTotal = cntTotal + cnt;

				// unique
				Aggregation aggregation = newAggregation(
						match(Criteria.where("entity_type").is(entityType)),
						match(Criteria.where("entity_id").is(entityId)),
						match(Criteria.where("time_created").gte(start).lte(end)),
						group("userId").count().as("total"),
						project("total").and("userId").previousOperation(),
						sort(Sort.Direction.DESC, "total"));

				AggregationResults<AggregateResult> groupResults = mongoOperations.aggregate(aggregation, Impression.class, AggregateResult.class);
				lstDataUnique.add(groupResults.getMappedResults().size());
				cntUnique = cntUnique + groupResults.getMappedResults().size();
			}
		}

		result.setTotal(cntTotal);
		result.setUnique(cntUnique);

		result.setLabels(lstLabels);
		result.setSeries(lstSeries);
		result.setData(Arrays.asList(lstDataTotal, lstDataUnique));

		return result;
	}

	class AggregateResult {

		private int userId;
		private long total;

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public long getTotal() {
			return total;
		}

		public void setTotal(long total) {
			this.total = total;
		}

		@Override
		public String toString() {
			return "AggregateResult [userId=" + userId + ", total=" + total + "]";
		}

	}

}
