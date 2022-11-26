package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the limesurvey_responses database table.
 * 
 */
@Entity
@Table(name="limesurvey_responses", schema="harvesting")
@NamedQuery(name="LimesurveyResponse.findAll", query="SELECT l FROM LimesurveyResponse l")
public class LimesurveyResponse implements Serializable {
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

	//bi-directional many-to-one association to LimesurveyQuestion
	@ManyToOne
	@JoinColumn(name="limesurvey_question_title", referencedColumnName="limesurvey_question_title", nullable=false)
	private LimesurveyQuestion limesurveyQuestion;

	//bi-directional many-to-one association to LimesurveySurveyAssignment
	@ManyToOne
	@JoinColumn(name="limesurvey_survey_assignment_id", nullable=false)
	private LimesurveySurveyAssignment limesurveySurveyAssignment;

	public LimesurveyResponse() {
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

	public LimesurveyQuestion getLimesurveyQuestion() {
		return this.limesurveyQuestion;
	}

	public void setLimesurveyQuestion(LimesurveyQuestion limesurveyQuestion) {
		this.limesurveyQuestion = limesurveyQuestion;
	}

	public LimesurveySurveyAssignment getLimesurveySurveyAssignment() {
		return this.limesurveySurveyAssignment;
	}

	public void setLimesurveySurveyAssignment(LimesurveySurveyAssignment limesurveySurveyAssignment) {
		this.limesurveySurveyAssignment = limesurveySurveyAssignment;
	}

}