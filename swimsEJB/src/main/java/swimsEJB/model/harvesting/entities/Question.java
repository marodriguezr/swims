package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the questions database table.
 * 
 */
@Entity
@Table(name="questions", schema="harvesting")
@NamedQuery(name="Question.findAll", query="SELECT q FROM Question q")
public class Question implements Serializable {
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

	//bi-directional many-to-one association to ExpectedAnswer
	@OneToMany(mappedBy="question")
	private List<ExpectedAnswer> expectedAnswers;

	//bi-directional many-to-one association to StudyVariable
	@ManyToOne
	@JoinColumn(name="study_variable_id")
	private StudyVariable studyVariable;

	//bi-directional many-to-one association to UnexpectedAnswer
	@OneToMany(mappedBy="question")
	private List<UnexpectedAnswer> unexpectedAnswers;

	public Question() {
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

	public List<ExpectedAnswer> getExpectedAnswers() {
		return this.expectedAnswers;
	}

	public void setExpectedAnswers(List<ExpectedAnswer> expectedAnswers) {
		this.expectedAnswers = expectedAnswers;
	}

	public ExpectedAnswer addExpectedAnswer(ExpectedAnswer expectedAnswer) {
		getExpectedAnswers().add(expectedAnswer);
		expectedAnswer.setQuestion(this);

		return expectedAnswer;
	}

	public ExpectedAnswer removeExpectedAnswer(ExpectedAnswer expectedAnswer) {
		getExpectedAnswers().remove(expectedAnswer);
		expectedAnswer.setQuestion(null);

		return expectedAnswer;
	}

	public StudyVariable getStudyVariable() {
		return this.studyVariable;
	}

	public void setStudyVariable(StudyVariable studyVariable) {
		this.studyVariable = studyVariable;
	}

	public List<UnexpectedAnswer> getUnexpectedAnswers() {
		return this.unexpectedAnswers;
	}

	public void setUnexpectedAnswers(List<UnexpectedAnswer> unexpectedAnswers) {
		this.unexpectedAnswers = unexpectedAnswers;
	}

	public UnexpectedAnswer addUnexpectedAnswer(UnexpectedAnswer unexpectedAnswer) {
		getUnexpectedAnswers().add(unexpectedAnswer);
		unexpectedAnswer.setQuestion(this);

		return unexpectedAnswer;
	}

	public UnexpectedAnswer removeUnexpectedAnswer(UnexpectedAnswer unexpectedAnswer) {
		getUnexpectedAnswers().remove(unexpectedAnswer);
		unexpectedAnswer.setQuestion(null);

		return unexpectedAnswer;
	}

}