package hr.betaware.fundfinder.resource;

import java.util.Date;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import hr.betaware.fundfinder.domain.User.Role;

@JsonInclude(Include.NON_NULL)
public class UserResource extends ResourceSupport {

	@JsonProperty("id")
	private Integer identificator;

	@JsonProperty("username")
	private String username;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("role")
	private Role role;

	@JsonProperty("password")
	private String password;

	@JsonProperty("confirmPassword")
	private String confirmPassword;

	@JsonProperty("companyName")
	private String companyName;

	@JsonProperty("company")
	private CompanyResource company;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public CompanyResource getCompany() {
		return company;
	}

	public void setCompany(CompanyResource company) {
		this.company = company;
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
