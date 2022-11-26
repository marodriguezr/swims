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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(name="limesurvey_question_id", nullable=false)
	private Integer limesurveyQuestionId;

	@Column(name="limesurvey_survey_id", nullable=false)
	private Integer limesurveySurveyId;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	//bi-directional many-to-one association to StudyVariable
	@ManyToOne
	@JoinColumn(name="study_variable_id", nullable=false)
	private StudyVariable studyVariable;

	//bi-directional many-to-one association to LimesurveyResponse
	@OneToMany(mappedBy="limesurveyQuestion")
	private List<LimesurveyResponse> limesurveyResponses;

	public LimesurveyQuestion() {
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

	public Integer getLimesurveyQuestionId() {
		return this.limesurveyQuestionId;
	}

	public void setLimesurveyQuestionId(Integer limesurveyQuestionId) {
		this.limesurveyQuestionId = limesurveyQuestionId;
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

	public StudyVariable getStudyVariable() {
		return this.studyVariable;
	}

	public void setStudyVariable(StudyVariable studyVariable) {
		this.studyVariable = studyVariable;
	}

	public List<LimesurveyResponse> getLimesurveyResponses() {
		return this.limesurveyResponses;
	}

	public void setLimesurveyResponses(List<LimesurveyResponse> limesurveyResponses) {
		this.limesurveyResponses = limesurveyResponses;
	}

	public LimesurveyResponse addLimesurveyRespons(LimesurveyResponse limesurveyRespons) {
		getLimesurveyResponses().add(limesurveyRespons);
		limesurveyRespons.setLimesurveyQuestion(this);

		return limesurveyRespons;
	}

	public LimesurveyResponse removeLimesurveyRespons(LimesurveyResponse limesurveyRespons) {
		getLimesurveyResponses().remove(limesurveyRespons);
		limesurveyRespons.setLimesurveyQuestion(null);

		return limesurveyRespons;
	}

}