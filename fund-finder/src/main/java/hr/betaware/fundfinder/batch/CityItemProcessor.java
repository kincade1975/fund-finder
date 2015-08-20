package hr.betaware.fundfinder.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import hr.betaware.fundfinder.domain.City;
import hr.betaware.fundfinder.domain.County;
import hr.betaware.fundfinder.service.SequenceService;

public class CityItemProcessor implements ItemProcessor<CityItem, City> {

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private SequenceService sequenceService;

	@Override
	public City process(CityItem item) throws Exception {
		City city = new City();
		city.setId(item.getId());
		city.setName(item.getName());
		city.setGroup(item.getGroup());
		city.setCounty(getCounty(item));

		return city;
	}

	private County getCounty(CityItem item) throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(item.getCounty()));

		County county = mongoOperations.findOne(query, County.class);

		if (county == null) {
			county = new County();
			county.setId(sequenceService.getNextSequence(SequenceService.SEQUENCE_COUNTY));
			county.setName(item.getCounty());
			mongoOperations.save(county);
		}

		return county;
	}

}
