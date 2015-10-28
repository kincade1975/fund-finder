package hr.betaware.fundfinder.resource;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ImpressionStatisticsResource {

	@JsonProperty("name")
	private String name;

	@JsonProperty("total")
	private Long total;

	@JsonProperty("unique")
	private Long unique;

	@JsonProperty("labels")
	private List<String> labels = new ArrayList<>();

	@JsonProperty("series")
	private List<String> series = new ArrayList<>();

	@JsonProperty("data")
	private List<List<Number>> data = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getUnique() {
		return unique;
	}

	public void setUnique(Long unique) {
		this.unique = unique;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<String> getSeries() {
		return series;
	}

	public void setSeries(List<String> series) {
		this.series = series;
	}

	public List<List<Number>> getData() {
		return data;
	}

	public void setData(List<List<Number>> data) {
		this.data = data;
	}

}
