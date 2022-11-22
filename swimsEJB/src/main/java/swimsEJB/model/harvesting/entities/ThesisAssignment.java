package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the thesis_assignments database table.
 * 
 */
@Entity
@Table(name="thesis_assignments", schema = "harvesting")
@NamedQuery(name="ThesisAssignment.findAll", query="SELECT t FROM ThesisAssignment t")
public class ThesisAssignment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(name="is_active", nullable=false)
	private Boolean isActive;

	@Column(name="is_dispatched", nullable=false)
	private Boolean isDispatched;

	@Column(name="limesurvey_survey_id", nullable=false)
	private Integer limesurveySurveyId;

	@Column(name="limesurvey_survey_token", nullable=false, length=2147483647)
	private String limesurveySurveyToken;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	@Column(name="user_id", nullable=false)
	private Integer userId;

	//bi-directional many-to-one association to OaiRecord
	@ManyToOne
	@JoinColumn(name="thesis_record_id", nullable=false)
	private OaiRecord oaiRecord;

	public ThesisAssignment() {
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

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public OaiRecord getOaiRecord() {
		return this.oaiRecord;
	}

	public void setOaiRecord(OaiRecord oaiRecord) {
		this.oaiRecord = oaiRecord;
	}

}