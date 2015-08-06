package hr.betaware.fundfinder.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class TotalResource {

	@JsonProperty("totalUsers")
	private Long totalUsers;

	@JsonProperty("totalTenders")
	private Long totalTenders;

	@JsonProperty("totalInvestments")
	private Long totalInvestments;

	@JsonProperty("totalArticles")
	private Long totalArticles;

	public Long getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(Long totalUsers) {
		this.totalUsers = totalUsers;
	}

	public Long getTotalTenders() {
		return totalTenders;
	}

	public void setTotalTenders(Long totalTenders) {
		this.totalTenders = totalTenders;
	}

	public Long getTotalInvestments() {
		return totalInvestments;
	}

	public void setTotalInvestments(Long totalInvestments) {
		this.totalInvestments = totalInvestments;
	}

	public Long getTotalArticles() {
		return totalArticles;
	}

	public void setTotalArticles(Long totalArticles) {
		this.totalArticles = totalArticles;
	}

}
