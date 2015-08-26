package hr.betaware.fundfinder.domain;

import org.springframework.data.mongodb.core.mapping.Field;

public class Answer {

	@Field("question_id")
	private Integer questionId;

	@Field("value")
	private Object value;

	@Field("value_internal")
	private Object valueInternal;

	public Answer() {
		super();
	}

	public Answer(Integer questionId, Object value, Object valueInternal) {
		super();
		this.questionId = questionId;
		this.value = value;
		this.valueInternal = valueInternal;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValueInternal() {
		return valueInternal;
	}

	public void setValueInternal(Object valueInternal) {
		this.valueInternal = valueInternal;
	}

}
