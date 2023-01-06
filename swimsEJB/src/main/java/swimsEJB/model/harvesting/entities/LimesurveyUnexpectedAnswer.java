package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the limesurvey_unexpected_answers database table.
 * 
 */
@Entity
@Table(name="limesurvey_unexpected_answers", schema="harvesting")
@NamedQuery(name="LimesurveyUnexpectedAnswer.findAll", query="SELECT l FROM LimesurveyUnexpectedAnswer l")
public class LimesurveyUnexpectedAnswer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Long id;

	@Column(nullable=false, length=2147483647)
	private String answer;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	//bi-directional many-to-one association to LimesurveyQuestion
	@ManyToOne
	@JoinColumn(name="limesurvey_question_id", nullable=false)
	private LimesurveyQuestion limesurveyQuestion;

	//bi-directional many-to-one association to LimesurveySurveyAssignment
	@ManyToOne
	@JoinColumn(name="limesurvey_survey_assignment_id", nullable=false)
	private LimesurveySurveyAssignment limesurveySurveyAssignment;

	public LimesurveyUnexpectedAnswer() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
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