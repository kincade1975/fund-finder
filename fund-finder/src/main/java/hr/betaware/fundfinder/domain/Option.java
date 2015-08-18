package hr.betaware.fundfinder.domain;

import org.springframework.data.mongodb.core.mapping.Field;

public class Option {

	@Field("id")
	private String id;

	@Field("value")
	private String value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
