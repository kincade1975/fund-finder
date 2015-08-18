package hr.betaware.fundfinder.batch;

import org.springframework.batch.item.ItemProcessor;

import hr.betaware.fundfinder.domain.City;

public class CityItemProcessor implements ItemProcessor<City, City> {

	@Override
	public City process(City city) throws Exception {
		return city;
	}

}
