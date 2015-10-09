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

	@JsonProperty("image")
	private String image;

	@JsonProperty("base64")
	private String base64;

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
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
