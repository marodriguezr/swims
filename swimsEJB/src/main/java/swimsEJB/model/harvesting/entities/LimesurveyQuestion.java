package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the limesurvey_questions database table.
 * 
 */
@Entity
@Table(name="limesurvey_questions", schema="harvesting")
@NamedQuery(name="LimesurveyQuestion.findAll", query="SELECT l FROM LimesurveyQuestion l")
public class LimesurveyQuestion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="limesurvey_question_id")
	private Integer limesurveyQuestionId;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(name="limesurvey_question_title")
	private String limesurveyQuestionTitle;

	@Column(name="limesurvey_survey_id")
	private Integer limesurveySurveyId;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	//bi-directional many-to-one association to LimesurveyAnswer
	@OneToMany(mappedBy="limesurveyQuestion")
	private List<LimesurveyAnswer> limesurveyAnswers;

	//bi-directional many-to-one association to StudyVariable
	@ManyToOne
	@JoinColumn(name="study_variable_id")
	private StudyVariable studyVariable;

	//bi-directional many-to-one association to LimesurveyUnexpectedAnswer
	@OneToMany(mappedBy="limesurveyQuestion")
	private List<LimesurveyUnexpectedAnswer> limesurveyUnexpectedAnswers;

	public LimesurveyQuestion() {
	}

	public Integer getLimesurveyQuestionId() {
		return this.limesurveyQuestionId;
	}

	public void setLimesurveyQuestionId(Integer limesurveyQuestionId) {
		this.limesurveyQuestionId = limesurveyQuestionId;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getLimesurveyQuestionTitle() {
		return this.limesurveyQuestionTitle;
	}

	public void setLimesurveyQuestionTitle(String limesurveyQuestionTitle) {
		this.limesurveyQuestionTitle = limesurveyQuestionTitle;
	}

	public Integer getLimesurveySurveyId() {
		return this.limesurveySurveyId;
	}

	public void setLimesurveySurveyId(Integer limesurveySurveyId) {
		this.limesurveySurveyId = limesurveySurveyId;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<LimesurveyAnswer> getLimesurveyAnswers() {
		return this.limesurveyAnswers;
	}

	public void setLimesurveyAnswers(List<LimesurveyAnswer> limesurveyAnswers) {
		this.limesurveyAnswers = limesurveyAnswers;
	}

	public LimesurveyAnswer addLimesurveyAnswer(LimesurveyAnswer limesurveyAnswer) {
		getLimesurveyAnswers().add(limesurveyAnswer);
		limesurveyAnswer.setLimesurveyQuestion(this);

		return limesurveyAnswer;
	}

	public LimesurveyAnswer removeLimesurveyAnswer(LimesurveyAnswer limesurveyAnswer) {
		getLimesurveyAnswers().remove(limesurveyAnswer);
		limesurveyAnswer.setLimesurveyQuestion(null);

		return limesurveyAnswer;
	}

	public StudyVariable getStudyVariable() {
		return this.studyVariable;
	}

	public void setStudyVariable(StudyVariable studyVariable) {
		this.studyVariable = studyVariable;
	}

	public List<LimesurveyUnexpectedAnswer> getLimesurveyUnexpectedAnswers() {
		return this.limesurveyUnexpectedAnswers;
	}

	public void setLimesurveyUnexpectedAnswers(List<LimesurveyUnexpectedAnswer> limesurveyUnexpectedAnswers) {
		this.limesurveyUnexpectedAnswers = limesurveyUnexpectedAnswers;
	}

	public LimesurveyUnexpectedAnswer addLimesurveyUnexpectedAnswer(LimesurveyUnexpectedAnswer limesurveyUnexpectedAnswer) {
		getLimesurveyUnexpectedAnswers().add(limesurveyUnexpectedAnswer);
		limesurveyUnexpectedAnswer.setLimesurveyQuestion(this);

		return limesurveyUnexpectedAnswer;
	}

	public LimesurveyUnexpectedAnswer removeLimesurveyUnexpectedAnswer(LimesurveyUnexpectedAnswer limesurveyUnexpectedAnswer) {
		getLimesurveyUnexpectedAnswers().remove(limesurveyUnexpectedAnswer);
		limesurveyUnexpectedAnswer.setLimesurveyQuestion(null);

		return limesurveyUnexpectedAnswer;
	}

}