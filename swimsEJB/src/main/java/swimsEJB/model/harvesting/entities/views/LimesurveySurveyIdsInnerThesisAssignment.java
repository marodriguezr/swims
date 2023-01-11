package swimsEJB.model.harvesting.entities.views;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the limesurvey_survey_ids_inner_thesis_assignments
 * database table.
 * 
 */
@Entity
@Table(name = "limesurvey_survey_ids_inner_thesis_assignments", schema = "harvesting")
@NamedQuery(name = "LimesurveySurveyIdsInnerThesisAssignment.findAll", query = "SELECT l FROM LimesurveySurveyIdsInnerThesisAssignment l")
public class LimesurveySurveyIdsInnerThesisAssignment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "limesurvey_survey_id")
	private Integer limesurveySurveyId;

	@Column(name = "thesis_record_id")
	private String thesisRecordId;

	public LimesurveySurveyIdsInnerThesisAssignment() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getLimesurveySurveyId() {
		return this.limesurveySurveyId;
	}

	public void setLimesurveySurveyId(Integer limesurveySurveyId) {
		this.limesurveySurveyId = limesurveySurveyId;
	}

	public String getThesisRecordId() {
		return this.thesisRecordId;
	}

	public void setThesisRecordId(String thesisRecordId) {
		this.thesisRecordId = thesisRecordId;
	}

}