package hr.betaware.fundfinder.domain;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "forgot_password")
public class ForgotPassword {

	@Id
	@Field("id")
	private String id;

	@DBRef
	@Field("user")
	private User user;

	@Version
	@Field("doc_version")
	private Long docVersion;

	@CreatedDate
	@Field("time_created")
	private Date timeCreated;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

}
