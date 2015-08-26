package hr.betaware.fundfinder.resource;

import java.util.Date;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class TenderResource extends ResourceSupport {

	@JsonProperty("id")
	private Integer identificator;

	@JsonProperty("name")
	private String name;

	@JsonProperty("active")
	private Boolean active = Boolean.FALSE;

	@JsonProperty("questions")
	private List<QuestionResource> questions;

	@JsonProperty("timeCreated")
	private Date timeCreated;

	@JsonProperty("lastModified")
	private Date lastModified;

	public Integer getIdentificator() {
		return identificator;
	}

	public void setIdentificator(Integer identificator) {
		this.identificator = identificator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<QuestionResource> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionResource> questions) {
		this.questions = questions;
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

	@Override
	public String toString() {
		return "TenderResource [identificator=" + identificator + ", name=" + name + ", active=" + active + ", questions=" + questions + ", timeCreated=" + timeCreated + ", lastModified=" + lastModified + "]";
	}

}
