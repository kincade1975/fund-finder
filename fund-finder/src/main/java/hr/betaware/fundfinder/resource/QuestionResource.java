package hr.betaware.fundfinder.resource;

import java.util.Date;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import hr.betaware.fundfinder.domain.Question.EntityType;
import hr.betaware.fundfinder.domain.Question.Type;

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

	@JsonProperty("type")
	private Type type;

	@JsonProperty("options")
	private List<OptionResource> options;

	@JsonProperty("timeCreated")
	private Date timeCreated;

	@JsonProperty("lastModified")
	private Date lastModified;

	@JsonProperty("answer")
	private Object answer;

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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<OptionResource> getOptions() {
		return options;
	}

	public void setOptions(List<OptionResource> options) {
		this.options = options;
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

	public Object getAnswer() {
		return answer;
	}

	public void setAnswer(Object answer) {
		this.answer = answer;
	}

}
