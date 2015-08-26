package hr.betaware.fundfinder.resource;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnswerResource extends ResourceSupport {

	@JsonProperty("value")
	private Object value;

	@JsonProperty("valueInternal")
	private Object valueInternal;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValueInternal() {
		return valueInternal;
	}

	public void setValueInternal(Object valueInternal) {
		this.valueInternal = valueInternal;
	}

	@Override
	public String toString() {
		return "AnswerResource [value=" + value + ", valueInternal=" + valueInternal + "]";
	}

}
