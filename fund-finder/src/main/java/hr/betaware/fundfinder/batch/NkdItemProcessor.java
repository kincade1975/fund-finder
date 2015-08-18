package hr.betaware.fundfinder.batch;

import org.springframework.batch.item.ItemProcessor;

import hr.betaware.fundfinder.domain.Nkd;

public class NkdItemProcessor implements ItemProcessor<Nkd, Nkd> {

	@Override
	public Nkd process(Nkd nkd) throws Exception {
		return nkd;
	}

}
