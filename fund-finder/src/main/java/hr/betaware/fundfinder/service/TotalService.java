package hr.betaware.fundfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.Article;
import hr.betaware.fundfinder.domain.Investment;
import hr.betaware.fundfinder.domain.Tender;
import hr.betaware.fundfinder.domain.User;
import hr.betaware.fundfinder.domain.User.Role;
import hr.betaware.fundfinder.resource.TotalResource;

@Service
public class TotalService {

	@Autowired
	private MongoOperations mongoOperations;

	public TotalResource getTotal() {
		TotalResource resource = new TotalResource();

		// users
		Query query = new Query();
		query.addCriteria(Criteria.where("role").is(Role.ROLE_USER));
		resource.setTotalUsers(mongoOperations.count(query, User.class));

		// tenders
		resource.setTotalTenders(mongoOperations.count(new Query(), Tender.class));

		// investments
		resource.setTotalInvestments(mongoOperations.count(new Query(), Investment.class));

		// articles
		resource.setTotalArticles(mongoOperations.count(new Query(), Article.class));

		return resource;
	}

}
