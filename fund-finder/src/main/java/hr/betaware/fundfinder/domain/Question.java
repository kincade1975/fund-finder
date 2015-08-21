package hr.betaware.fundfinder.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import hr.betaware.fundfinder.enums.EntityType;
import hr.betaware.fundfinder.enums.LinkOperator;
import hr.betaware.fundfinder.enums.QuestionType;

@Document(collection = "question")
public class Question {

	@Id
	@Field("id")
	private Integer id;

	@Field("entity_type")
	private EntityType entityType;

	@Field("index")
	private Integer index;

	@Field("text")
	private String text;

	@Field("type")
	private QuestionType type;

	@Field("options")
	private List<Option> options;

	@Field("link_question_id")
	private List<Integer> linkQuestionId;

	@Field("link_operator")
	private LinkOperator linkOperator;

	@Version
	@Field("doc_version")
	private Long docVersion;

	@CreatedDate
	@Field("time_created")
	private Date timeCreated;

	@LastModifiedDate
	@Field("last_modified")
	private Date lastModified;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public List<Integer> getLinkQuestionId() {
		return linkQuestionId;
	}

	public void setLinkQuestionId(List<Integer> linkQuestionId) {
		this.linkQuestionId = linkQuestionId;
	}

	public LinkOperator getLinkOperator() {
		return linkOperator;
	}

	public void setLinkOperator(LinkOperator linkOperator) {
		this.linkOperator = linkOperator;
	}

	public Long getDocVersion() {
		return docVersion;
	}

	public void setDocVersion(Long docVersion) {
		this.docVersion = docVersion;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

}
