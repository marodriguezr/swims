package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the expected_answers database table.
 * 
 */
@Entity
@Table(name="expected_answers", schema="harvesting")
@NamedQuery(name="ExpectedAnswer.findAll", query="SELECT e FROM ExpectedAnswer e")
public class ExpectedAnswer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Long id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(name="limesurvey_answer_code", nullable=false, length=5)
	private String limesurveyAnswerCode;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	//bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name="limesurvey_question_id", nullable=false)
	private Question question;

	//bi-directional many-to-one association to SurveyAssignment
	@ManyToOne
	@JoinColumn(name="survey_assignment_id", nullable=false)
	private SurveyAssignment surveyAssignment;

	public ExpectedAnswer() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getLimesurveyAnswerCode() {
		return this.limesurveyAnswerCode;
	}

	public void setLimesurveyAnswerCode(String limesurveyAnswerCode) {
		this.limesurveyAnswerCode = limesurveyAnswerCode;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public SurveyAssignment getSurveyAssignment() {
		return this.surveyAssignment;
	}

	public void setSurveyAssignment(SurveyAssignment surveyAssignment) {
		this.surveyAssignment = surveyAssignment;
	}

}