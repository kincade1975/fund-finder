package hr.betaware.fundfinder.resource;

import java.util.Date;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import hr.betaware.fundfinder.enums.EntityType;
import hr.betaware.fundfinder.enums.LinkOperator;
import hr.betaware.fundfinder.enums.QuestionMetadata;
import hr.betaware.fundfinder.enums.QuestionType;

@JsonInclude(Include.NON_NULL)
public class QuestionResource extends ResourceSupport {

	@JsonProperty("id")
	private Integer identificator;

	@JsonProperty("entityType")
	private EntityType entityType;

	@JsonProperty("index")
	private Integer index;

	@JsonProperty("text")
	private String text;

	@JsonProperty("mandatory")
	private Boolean mandatory;

	@JsonProperty("metadata")
	private List<QuestionMetadata> metadata;

	@JsonProperty("type")
	private QuestionType type;

	@JsonProperty("options")
	private List<OptionResource> options;

	@JsonProperty("linkQuestionId")
	private List<Integer> linkQuestionId;

	@JsonProperty("linkOperator")
	private LinkOperator linkOperator;

	@JsonProperty("timeCreated")
	private Date timeCreated;

	@JsonProperty("lastModified")
	private Date lastModified;

	@JsonProperty("answer")
	private AnswerResource answer = new AnswerResource();

	public Integer getIdentificator() {
		return identificator;
	}

	public void setIdentificator(Integer identificator) {
		this.identificator = identificator;
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

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public List<QuestionMetadata> getMetadata() {
		return metadata;
	}

	public void setMetadata(List<QuestionMetadata> metadata) {
		this.metadata = metadata;
	}

	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public List<OptionResource> getOptions() {
		return options;
	}

	public void setOptions(List<OptionResource> options) {
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

	public AnswerResource getAnswer() {
		return answer;
	}

	public void setAnswer(AnswerResource answer) {
		this.answer = answer;
	}

	@Override
	public String toString() {
		return "QuestionResource [identificator=" + identificator + ", entityType=" + entityType + ", index=" + index + ", text=" + text + ", type=" + type + ", options=" + options + ", linkQuestionId=" + linkQuestionId + ", linkOperator=" + linkOperator + ", timeCreated=" + timeCreated + ", lastModified=" + lastModified + ", answer=" + answer + "]";
	}

}
