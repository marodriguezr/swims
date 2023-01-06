package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the thesis_assignments database table.
 * 
 */
@Entity
@Table(name="thesis_assignments", schema="harvesting")
@NamedQuery(name="ThesisAssignment.findAll", query="SELECT t FROM ThesisAssignment t")
public class ThesisAssignment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	@Column(name="user_id", nullable=false)
	private Integer userId;

	//bi-directional many-to-one association to ExpectedAnswer
	@OneToMany(mappedBy="thesisAssignment")
	private List<ExpectedAnswer> expectedAnswers;

	//bi-directional many-to-one association to LimesurveySurveyAssignment
	@OneToMany(mappedBy="thesisAssignment")
	private List<LimesurveySurveyAssignment> limesurveySurveyAssignments;

	//bi-directional many-to-one association to OaiRecord
	@ManyToOne
	@JoinColumn(name="oai_record_id", nullable=false)
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

	public List<ExpectedAnswer> getExpectedAnswers() {
		return this.expectedAnswers;
	}

	public void setExpectedAnswers(List<ExpectedAnswer> expectedAnswers) {
		this.expectedAnswers = expectedAnswers;
	}

	public ExpectedAnswer addExpectedAnswer(ExpectedAnswer expectedAnswer) {
		getExpectedAnswers().add(expectedAnswer);
		expectedAnswer.setThesisAssignment(this);

		return expectedAnswer;
	}

	public ExpectedAnswer removeExpectedAnswer(ExpectedAnswer expectedAnswer) {
		getExpectedAnswers().remove(expectedAnswer);
		expectedAnswer.setThesisAssignment(null);

		return expectedAnswer;
	}

	public List<LimesurveySurveyAssignment> getLimesurveySurveyAssignments() {
		return this.limesurveySurveyAssignments;
	}

	public void setLimesurveySurveyAssignments(List<LimesurveySurveyAssignment> limesurveySurveyAssignments) {
		this.limesurveySurveyAssignments = limesurveySurveyAssignments;
	}

	public LimesurveySurveyAssignment addLimesurveySurveyAssignment(LimesurveySurveyAssignment limesurveySurveyAssignment) {
		getLimesurveySurveyAssignments().add(limesurveySurveyAssignment);
		limesurveySurveyAssignment.setThesisAssignment(this);

		return limesurveySurveyAssignment;
	}

	public LimesurveySurveyAssignment removeLimesurveySurveyAssignment(LimesurveySurveyAssignment limesurveySurveyAssignment) {
		getLimesurveySurveyAssignments().remove(limesurveySurveyAssignment);
		limesurveySurveyAssignment.setThesisAssignment(null);

		return limesurveySurveyAssignment;
	}

	public OaiRecord getOaiRecord() {
		return this.oaiRecord;
	}

	public void setOaiRecord(OaiRecord oaiRecord) {
		this.oaiRecord = oaiRecord;
	}

}