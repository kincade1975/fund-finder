package hr.betaware.fundfinder.resource;

import java.util.ArrayList;
import java.util.List;

import hr.betaware.fundfinder.enums.StatisticsType;

public class StatisticsResource {

	private StatisticsType type;
	private List<String> labels = new ArrayList<>();
	private List<Number> data = new ArrayList<>();

	public StatisticsResource() {
		super();
	}

	public StatisticsResource(StatisticsType type) {
		super();
		this.type = type;
	}

	public StatisticsType getType() {
		return type;
	}

	public void setType(StatisticsType type) {
		this.type = type;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<Number> getData() {
		return data;
	}

	public void setData(List<Number> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "StatisticsResource [type=" + type + ", labels=" + labels + ", data=" + data + "]";
	}

}
