package hr.betaware.fundfinder.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "user")
public class User {

	public enum Role { ROLE_SUPERADMIN, ROLE_ADMINISTRATOR, ROLE_USER }

	@Id
	@Field("id")
	private Integer id;

	@Field("username")
	private String username;

	@Field("password")
	private String password;

	@Field("first_name")
	private String firstName;

	@Field("last_name")
	private String lastName;

	@Field("role")
	private Role role;

	@Field("investments")
	private List<Integer> investments;

	@Field("companyName")
	private String companyName;

	@DBRef
	@Field("company")
	private Company company;

	@Version
	@Field("doc_version")
	private Long docVersion;

	@CreatedDate
	@Field("time_created")
	private Date timeCreated;

	@LastModifiedDate
	@Field("last_modified")
	private Date lastModified;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Integer> getInvestments() {
		return investments;
	}

	public void setInvestments(List<Integer> investments) {
		this.investments = investments;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

}
