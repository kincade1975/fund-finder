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

import hr.betaware.fundfinder.domain.Tender;
import hr.betaware.fundfinder.domain.User;
import hr.betaware.fundfinder.domain.User.Role;
import hr.betaware.fundfinder.resource.TenderResource;
import hr.betaware.fundfinder.resource.UserResource;
import hr.betaware.fundfinder.resource.assembler.UserResourceAssembler;

@Service
public class DashboardService {

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private UserResourceAssembler userResourceAssembler;

	@Autowired
	private TenderService tenderService;

	public List<UserResource> findLatestUsers() {
		Query query = new Query();
		query.addCriteria(Criteria.where("role").nin(Role.ROLE_SUPERADMIN, Role.ROLE_ADMINISTRATOR));
		query.with(new Sort(Direction.DESC, "time_created"));
		query.limit(6);
		return userResourceAssembler.toResources(mongoOperations.find(query, User.class));
	}

	public List<TenderResource> findLatestTenders() {
		Query query = new Query();
		query.addCriteria(Criteria.where("active").is(Boolean.TRUE));
		query.with(new Sort(Direction.DESC, "time_created"));
		query.limit(6);

		List<TenderResource> result = new ArrayList<TenderResource>();
		for (Tender tender : mongoOperations.find(query, Tender.class)) {
			result.add(tenderService.findTender(tender.getId()));
		}

		return result;
	}

}
