package hr.betaware.fundfinder.enums;

import java.util.Arrays;
import java.util.List;

//public enum QuestionType { TEXT, TEXT_AREA, TEXT_EDITOR, NUMBER, DATE, DATE_TIME,
//RADIO, SELECT, MULTI_SELECT, NKD, NKD_AUX, CITY, COUNTY, INVESTMENT }

public enum QuestionType {

	TEXT(Arrays.asList(LinkOperator.EQUAL)),
	TEXT_AREA(null),
	TEXT_EDITOR(null),
	NUMBER(Arrays.asList(LinkOperator.EQUAL, LinkOperator.GREATER_THEN, LinkOperator.GREATER_THEN_OR_EQUAL, LinkOperator.LESS_THEN, LinkOperator.LESS_THEN_OR_EQUAL)),
	DATE(Arrays.asList(LinkOperator.EQUAL, LinkOperator.GREATER_THEN, LinkOperator.GREATER_THEN_OR_EQUAL, LinkOperator.LESS_THEN, LinkOperator.LESS_THEN_OR_EQUAL)),
	DATE_TIME(Arrays.asList(LinkOperator.EQUAL, LinkOperator.GREATER_THEN, LinkOperator.GREATER_THEN_OR_EQUAL, LinkOperator.LESS_THEN, LinkOperator.LESS_THEN_OR_EQUAL)),
	RADIO(Arrays.asList(LinkOperator.EQUAL)),
	SELECT(Arrays.asList(LinkOperator.EQUAL)),
	MULTI_SELECT(Arrays.asList(LinkOperator.IN)),
	NKD(Arrays.asList(LinkOperator.EQUAL)),
	NKD_AUX(Arrays.asList(LinkOperator.IN)),
	CITY(Arrays.asList(LinkOperator.IN)),
	COUNTY(Arrays.asList(LinkOperator.IN)),
	INVESTMENT(Arrays.asList(LinkOperator.IN));

	private final List<LinkOperator> operators;

	private QuestionType(List<LinkOperator> operators) {
		this.operators = operators;
	}

	public List<LinkOperator> getOperators() {
		return operators;
	}

}