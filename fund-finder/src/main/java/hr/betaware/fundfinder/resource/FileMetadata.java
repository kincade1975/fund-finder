package hr.betaware.fundfinder.resource;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileMetadata extends ResourceSupport {

	@JsonProperty("name")
	private String name;

	@JsonProperty("base64")
	private String base64;

	public FileMetadata() {
		super();
	}

	public FileMetadata(String name) {
		super();
		this.name = name;
	}

	public FileMetadata(String name, String base64) {
		super();
		this.name = name;
		this.base64 = base64;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}


}
