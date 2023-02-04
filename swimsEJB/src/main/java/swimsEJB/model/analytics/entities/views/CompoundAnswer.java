package swimsEJB.model.analytics.entities.views;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the compound_answers database table.
 * 
 */
@Entity
@Table(name="compound_answers", schema="analytics")
@NamedQuery(name="CompoundAnswer.findAll", query="SELECT c FROM CompoundAnswer c")
public class CompoundAnswer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(length=2147483647)
	private String answer;

	@Id
	@Column(length=2147483647)
	private String id;

	@Column(name="is_categorical_nominal")
	private Boolean isCategoricalNominal;

	@Column(name="is_categorical_ordinal")
	private Boolean isCategoricalOrdinal;

	@Column(name="is_numeric_continuous")
	private Boolean isNumericContinuous;

	@Column(name="is_numeric_discrete")
	private Boolean isNumericDiscrete;

	@Column(name="limesurvey_question_title", length=20)
	private String limesurveyQuestionTitle;

	@Column(name="study_variable_class_id", length=32)
	private String studyVariableClassId;

	@Column(name="study_variable_id", length=32)
	private String studyVariableId;

	@Column(name="thesis_record_id", length=2147483647)
	private String thesisRecordId;

	public CompoundAnswer() {
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getIsCategoricalNominal() {
		return this.isCategoricalNominal;
	}

	public void setIsCategoricalNominal(Boolean isCategoricalNominal) {
		this.isCategoricalNominal = isCategoricalNominal;
	}

	public Boolean getIsCategoricalOrdinal() {
		return this.isCategoricalOrdinal;
	}

	public void setIsCategoricalOrdinal(Boolean isCategoricalOrdinal) {
		this.isCategoricalOrdinal = isCategoricalOrdinal;
	}

	public Boolean getIsNumericContinuous() {
		return this.isNumericContinuous;
	}

	public void setIsNumericContinuous(Boolean isNumericContinuous) {
		this.isNumericContinuous = isNumericContinuous;
	}

	public Boolean getIsNumericDiscrete() {
		return this.isNumericDiscrete;
	}

	public void setIsNumericDiscrete(Boolean isNumericDiscrete) {
		this.isNumericDiscrete = isNumericDiscrete;
	}

	public String getLimesurveyQuestionTitle() {
		return this.limesurveyQuestionTitle;
	}

	public void setLimesurveyQuestionTitle(String limesurveyQuestionTitle) {
		this.limesurveyQuestionTitle = limesurveyQuestionTitle;
	}

	public String getStudyVariableClassId() {
		return this.studyVariableClassId;
	}

	public void setStudyVariableClassId(String studyVariableClassId) {
		this.studyVariableClassId = studyVariableClassId;
	}

	public String getStudyVariableId() {
		return this.studyVariableId;
	}

	public void setStudyVariableId(String studyVariableId) {
		this.studyVariableId = studyVariableId;
	}

	public String getThesisRecordId() {
		return this.thesisRecordId;
	}

	public void setThesisRecordId(String thesisRecordId) {
		this.thesisRecordId = thesisRecordId;
	}

}