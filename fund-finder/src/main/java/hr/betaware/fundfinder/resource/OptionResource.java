package hr.betaware.fundfinder.resource;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class OptionResource extends ResourceSupport {

	@JsonProperty("id")
	private String identificator;

	@JsonProperty("value")
	private String value;

	public OptionResource() {
		super();
	}

	public OptionResource(String identificator, String value) {
		super();
		this.identificator = identificator;
		this.value = value;
	}

	public String getIdentificator() {
		return identificator;
	}

	public void setIdentificator(String identificator) {
		this.identificator = identificator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
