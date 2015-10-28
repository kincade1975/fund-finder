package hr.betaware.fundfinder.enums;

public enum StatisticsPeriod {

	LAST_7_DAYS("Zadnjih 7 dana"),
	LAST_6_MONTHS("Zadnjih 6 mjeseci");

	private final String label;

	private StatisticsPeriod(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}