package hr.betaware.fundfinder.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import hr.betaware.fundfinder.domain.Investment;
import hr.betaware.fundfinder.domain.User;
import hr.betaware.fundfinder.resource.InvestmentResource;
import hr.betaware.fundfinder.resource.assembler.InvestmentResourceAssembler;
import hr.betaware.fundfinder.resource.uigrid.PageableResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridFilterResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridSortResource;

@Service
public class InvestmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentService.class);

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private UserService userService;

	@Autowired
	private InvestmentResourceAssembler investmentResourceAssembler;

	public List<InvestmentResource> findInvestments() {
		Query query = new Query();
		query.with(new Sort(Direction.ASC, "name"));
		return investmentResourceAssembler.toResources(mongoOperations.find(query, Investment.class));
	}

	public InvestmentResource findInvestment(Integer id) {
		if (id == 0) {
			// new investment
			return new InvestmentResource();
		}
		return investmentResourceAssembler.toResource(mongoOperations.findById(id, Investment.class));
	}

	public InvestmentResource saveInvestment(InvestmentResource resource) {
		Investment entity = null;
		if (resource.getIdentificator() == null) {
			entity = investmentResourceAssembler.createEntity(resource);
		} else {
			entity = mongoOperations.findById(resource.getIdentificator(), Investment.class);
			entity = investmentResourceAssembler.updateEntity(entity, resource);
		}

		mongoOperations.save(entity);

		return investmentResourceAssembler.toResource(entity);
	}

	public void deleteInvestment(Integer id) {
		Investment entity = mongoOperations.findById(id, Investment.class);
		mongoOperations.remove(entity);
	}

	public PageableResource<InvestmentResource> getPage(UiGridResource resource) {
		Query query = new Query();

		// sorting
		if (resource.getSort() == null || resource.getSort().isEmpty()) {
			query.with(new Sort(Direction.ASC, "name"));
		} else {
			for (UiGridSortResource uiGridSortResource : resource.getSort()) {
				query.with(new Sort(Direction.valueOf(uiGridSortResource.getDirection().toUpperCase()), uiGridSortResource.getName()));
			}
		}

		// filtering
		if (resource.getFilter() != null) {
			for (UiGridFilterResource uiGridFilterResource : resource.getFilter()) {
				query.addCriteria(Criteria.where(uiGridFilterResource.getName()).regex(uiGridFilterResource.getTerm(), "i"));
			}
		}

		// get total number of records that match query
		long total = mongoOperations.count(query, Investment.class);

		// add paging to query
		LOGGER.debug("Querying database: {}", query);
		query.with(new PageRequest(resource.getPagination().getPage(), resource.getPagination().getSize()));

		// get records that match query
		List<Investment> entities = mongoOperations.find(query, Investment.class);

		return new PageableResource<>(total, investmentResourceAssembler.toResources(entities));
	}

	public List<InvestmentResource> findInvestments4User(Principal principal) {
		User user = userService.getUser4Principal(principal);
		return findInvestments4User(user.getId());
	}

	public List<InvestmentResource> findInvestments4User(Integer userId) {
		User user = mongoOperations.findById(userId, User.class);

		List<InvestmentResource> investments = findInvestments();
		if (user.getInvestments() != null) {
			for (Integer id : user.getInvestments()) {
				for (InvestmentResource investment : investments) {
					if (id.equals(investment.getIdentificator())) {
						investment.setSelected(Boolean.TRUE);
						break;
					}
				}
			}
		}
		return investments;
	}

	public void saveInvestments4User(List<InvestmentResource> resources, Principal principal) {
		User user = userService.getUser4Principal(principal);
		List<Integer> investments = new ArrayList<>();
		for (InvestmentResource resource : resources) {
			if (resource.getSelected()) {
				investments.add(resource.getIdentificator());
			}
		}
		user.setInvestments(investments);
		mongoOperations.save(user);
	}

}
