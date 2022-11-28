package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the survey_assignments database table.
 * 
 */
@Entity
@Table(name="survey_assignments", schema="harvesting")
@NamedQuery(name="SurveyAssignment.findAll", query="SELECT s FROM SurveyAssignment s")
public class SurveyAssignment implements Serializable {
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

	//bi-directional many-to-one association to Response
	@OneToMany(mappedBy="surveyAssignment")
	private List<Response> responses;

	//bi-directional many-to-one association to ThesisAssignment
	@ManyToOne
	@JoinColumn(name="thesis_assignment_id", nullable=false)
	private ThesisAssignment thesisAssignment;

	public SurveyAssignment() {
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

	public List<Response> getResponses() {
		return this.responses;
	}

	public void setResponses(List<Response> responses) {
		this.responses = responses;
	}

	public Response addRespons(Response respons) {
		getResponses().add(respons);
		respons.setSurveyAssignment(this);

		return respons;
	}

	public Response removeRespons(Response respons) {
		getResponses().remove(respons);
		respons.setSurveyAssignment(null);

		return respons;
	}

	public ThesisAssignment getThesisAssignment() {
		return this.thesisAssignment;
	}

	public void setThesisAssignment(ThesisAssignment thesisAssignment) {
		this.thesisAssignment = thesisAssignment;
	}

}