package hr.betaware.fundfinder.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import hr.betaware.fundfinder.domain.Answer;
import hr.betaware.fundfinder.domain.Tender;
import hr.betaware.fundfinder.enums.EntityType;
import hr.betaware.fundfinder.resource.QuestionResource;
import hr.betaware.fundfinder.resource.TenderResource;
import hr.betaware.fundfinder.resource.assembler.AnswerResourceAssembler;
import hr.betaware.fundfinder.resource.assembler.TenderResourceAssembler;
import hr.betaware.fundfinder.resource.uigrid.PageableResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridFilterResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridResource;
import hr.betaware.fundfinder.resource.uigrid.UiGridSortResource;

@Service
public class TenderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TenderService.class);

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private TenderResourceAssembler tenderResourceAssembler;

	@Autowired
	private AnswerResourceAssembler answerResourceAssembler;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private StompService stompService;

	@Autowired
	private FileService fileService;

	//	public List<TenderResource> findAll() {
	//		return tenderResourceAssembler.toResources(mongoOperations.findAll(Tender.class));
	//	}

	public TenderResource findTender(Integer id) {
		TenderResource resource = null;
		Map<Integer, Answer> answers = new HashMap<>();

		if (id == 0) {
			resource = new TenderResource();
		} else {
			Tender tender = mongoOperations.findById(id, Tender.class);
			for (Answer answer : tender.getAnswers()) {
				answers.put(answer.getQuestionId(), answer);
			}
			resource = tenderResourceAssembler.toResource(tender);
			resource.setBase64(fileService.getMetadata(resource.getImage()).getBase64());
		}

		List<QuestionResource> questions = new ArrayList<>();
		for (QuestionResource questionResource : configurationService.getQuestions(EntityType.TENDER.toString())) {
			if (answers.containsKey(questionResource.getIdentificator())) {
				questionResource.setAnswer(answerResourceAssembler.toResource(answers.get(questionResource.getIdentificator())));
			}
			questions.add(questionResource);
		}
		resource.setQuestions(questions);

		return resource;
	}

	public TenderResource activateTender(Integer id) {
		Tender tender = mongoOperations.findById(id, Tender.class);
		tender.setActive(Boolean.TRUE);
		mongoOperations.save(tender);
		return tenderResourceAssembler.toResource(tender);
	}

	public TenderResource deactivateTender(Integer id) {
		Tender tender = mongoOperations.findById(id, Tender.class);
		tender.setActive(Boolean.FALSE);
		mongoOperations.save(tender);
		return tenderResourceAssembler.toResource(tender);
	}

	public TenderResource saveTender(TenderResource resource) {
		Tender entity = null;
		if (resource.getIdentificator() == null) {
			entity = tenderResourceAssembler.createEntity(resource);
		} else {
			entity = mongoOperations.findById(resource.getIdentificator(), Tender.class);
			entity = tenderResourceAssembler.updateEntity(entity, resource);
		}

		mongoOperations.save(entity);

		if (resource.getIdentificator() == null) {
			// update total only on insert, not update
			stompService.updateTotal();
		}

		return findTender(entity.getId());
	}

	public void deleteTender(Integer id) {
		Tender entity = mongoOperations.findById(id, Tender.class);
		mongoOperations.remove(entity);
		stompService.updateTotal();
	}

	public PageableResource<TenderResource> getPage(UiGridResource resource) {
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
		long total = mongoOperations.count(query, Tender.class);

		// add paging to query
		LOGGER.debug("Querying database: {}", query);
		query.with(new PageRequest(resource.getPagination().getPage(), resource.getPagination().getSize()));

		// get records that match query
		List<Tender> entities = mongoOperations.find(query, Tender.class);

		return new PageableResource<>(total, tenderResourceAssembler.toResources(entities));
	}

}
