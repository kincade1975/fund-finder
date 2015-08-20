package hr.betaware.fundfinder.batch;

import org.springframework.batch.item.ItemProcessor;

import hr.betaware.fundfinder.domain.Nkd;

public class NkdItemProcessor implements ItemProcessor<NkdItem, Nkd> {

	@Override
	public Nkd process(NkdItem item) throws Exception {
		Nkd nkd = new Nkd();
		nkd.setId(item.getId());
		nkd.setSector(item.getSector());
		nkd.setSectorName(item.getSectorName());
		nkd.setArea(item.getArea());
		nkd.setActivity(item.getActivity());
		nkd.setActivityName(item.getActivityName());
		return nkd;
	}

}
