package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the limesurvey_survey_assignments database table.
 * 
 */
@Entity
@Table(name="limesurvey_survey_assignments", schema="harvesting")
@NamedQuery(name="LimesurveySurveyAssignment.findAll", query="SELECT l FROM LimesurveySurveyAssignment l")
public class LimesurveySurveyAssignment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(name="is_dispatched", nullable=false)
	private Boolean isDispatched;

	@Column(name="limesurvey_survey_id", nullable=false)
	private Integer limesurveySurveyId;

	@Column(name="limesurvey_survey_token", nullable=false, length=2147483647)
	private String limesurveySurveyToken;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	//bi-directional many-to-one association to LimesurveyExpectedAnswer
	@OneToMany(mappedBy="limesurveySurveyAssignment")
	private List<LimesurveyExpectedAnswer> limesurveyExpectedAnswers;

	//bi-directional many-to-one association to ThesisAssignment
	@ManyToOne
	@JoinColumn(name="thesis_assignment_id", nullable=false)
	private ThesisAssignment thesisAssignment;

	//bi-directional many-to-one association to LimesurveyUnexpectedAnswer
	@OneToMany(mappedBy="limesurveySurveyAssignment")
	private List<LimesurveyUnexpectedAnswer> limesurveyUnexpectedAnswers;

	public LimesurveySurveyAssignment() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getIsDispatched() {
		return this.isDispatched;
	}

	public void setIsDispatched(Boolean isDispatched) {
		this.isDispatched = isDispatched;
	}

	public Integer getLimesurveySurveyId() {
		return this.limesurveySurveyId;
	}

	public void setLimesurveySurveyId(Integer limesurveySurveyId) {
		this.limesurveySurveyId = limesurveySurveyId;
	}

	public String getLimesurveySurveyToken() {
		return this.limesurveySurveyToken;
	}

	public void setLimesurveySurveyToken(String limesurveySurveyToken) {
		this.limesurveySurveyToken = limesurveySurveyToken;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<LimesurveyExpectedAnswer> getLimesurveyExpectedAnswers() {
		return this.limesurveyExpectedAnswers;
	}

	public void setLimesurveyExpectedAnswers(List<LimesurveyExpectedAnswer> limesurveyExpectedAnswers) {
		this.limesurveyExpectedAnswers = limesurveyExpectedAnswers;
	}

	public LimesurveyExpectedAnswer addLimesurveyExpectedAnswer(LimesurveyExpectedAnswer limesurveyExpectedAnswer) {
		getLimesurveyExpectedAnswers().add(limesurveyExpectedAnswer);
		limesurveyExpectedAnswer.setLimesurveySurveyAssignment(this);

		return limesurveyExpectedAnswer;
	}

	public LimesurveyExpectedAnswer removeLimesurveyExpectedAnswer(LimesurveyExpectedAnswer limesurveyExpectedAnswer) {
		getLimesurveyExpectedAnswers().remove(limesurveyExpectedAnswer);
		limesurveyExpectedAnswer.setLimesurveySurveyAssignment(null);

		return limesurveyExpectedAnswer;
	}

	public ThesisAssignment getThesisAssignment() {
		return this.thesisAssignment;
	}

	public void setThesisAssignment(ThesisAssignment thesisAssignment) {
		this.thesisAssignment = thesisAssignment;
	}

	public List<LimesurveyUnexpectedAnswer> getLimesurveyUnexpectedAnswers() {
		return this.limesurveyUnexpectedAnswers;
	}

	public void setLimesurveyUnexpectedAnswers(List<LimesurveyUnexpectedAnswer> limesurveyUnexpectedAnswers) {
		this.limesurveyUnexpectedAnswers = limesurveyUnexpectedAnswers;
	}

	public LimesurveyUnexpectedAnswer addLimesurveyUnexpectedAnswer(LimesurveyUnexpectedAnswer limesurveyUnexpectedAnswer) {
		getLimesurveyUnexpectedAnswers().add(limesurveyUnexpectedAnswer);
		limesurveyUnexpectedAnswer.setLimesurveySurveyAssignment(this);

		return limesurveyUnexpectedAnswer;
	}

	public LimesurveyUnexpectedAnswer removeLimesurveyUnexpectedAnswer(LimesurveyUnexpectedAnswer limesurveyUnexpectedAnswer) {
		getLimesurveyUnexpectedAnswers().remove(limesurveyUnexpectedAnswer);
		limesurveyUnexpectedAnswer.setLimesurveySurveyAssignment(null);

		return limesurveyUnexpectedAnswer;
	}

}