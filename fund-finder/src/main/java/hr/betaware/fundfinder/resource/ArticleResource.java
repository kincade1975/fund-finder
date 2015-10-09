package hr.betaware.fundfinder.resource;

import java.util.Date;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ArticleResource extends ResourceSupport {

	@JsonProperty("id")
	private Integer identificator;

	@JsonProperty("title")
	private String title;

	@JsonProperty("text")
	private String text;

	@JsonProperty("strippedText")
	private String strippedText;

	@JsonProperty("image")
	private String image;

	@JsonProperty("base64")
	private String base64;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getStrippedText() {
		return strippedText;
	}

	public void setStrippedText(String strippedText) {
		this.strippedText = strippedText;
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
