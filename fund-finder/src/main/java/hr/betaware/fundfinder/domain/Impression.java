package hr.betaware.fundfinder.domain;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "impression")
public class Impression {

	public enum EntityType { TENDER, ARTICLE }

	@Id
	@Field("id")
	private String id;

	@Field("entity_type")
	private EntityType entityType;

	@Field("entity_id")
	private Integer entityId;

	@Field("user_id")
	private Integer userId;

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

	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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
