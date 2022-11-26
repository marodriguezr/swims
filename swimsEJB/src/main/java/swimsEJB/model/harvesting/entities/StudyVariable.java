package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the study_variables database table.
 * 
 */
@Entity
@Table(name="study_variables", schema="harvesting")
@NamedQuery(name="StudyVariable.findAll", query="SELECT s FROM StudyVariable s")
public class StudyVariable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(name="is_boolean", nullable=false)
	private Boolean isBoolean;

	@Column(name="is_likert", nullable=false)
	private Boolean isLikert;

	@Column(name="is_qualitative", nullable=false)
	private Boolean isQualitative;

	@Column(name="is_quantitative_continuous", nullable=false)
	private Boolean isQuantitativeContinuous;

	@Column(name="is_quantitative_discrete", nullable=false)
	private Boolean isQuantitativeDiscrete;

	@Column(name="long_name", nullable=false, length=2147483647)
	private String longName;

	@Column(name="short_name", nullable=false, length=2147483647)
	private String shortName;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	//bi-directional many-to-one association to LimesurveyQuestion
	@OneToMany(mappedBy="studyVariable")
	private List<LimesurveyQuestion> limesurveyQuestions;

	//bi-directional many-to-one association to StudyVariableClass
	@ManyToOne
	@JoinColumn(name="study_variable_class_id", nullable=false)
	private StudyVariableClass studyVariableClass;

	public StudyVariable() {
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

	public Boolean getIsBoolean() {
		return this.isBoolean;
	}

	public void setIsBoolean(Boolean isBoolean) {
		this.isBoolean = isBoolean;
	}

	public Boolean getIsLikert() {
		return this.isLikert;
	}

	public void setIsLikert(Boolean isLikert) {
		this.isLikert = isLikert;
	}

	public Boolean getIsQualitative() {
		return this.isQualitative;
	}

	public void setIsQualitative(Boolean isQualitative) {
		this.isQualitative = isQualitative;
	}

	public Boolean getIsQuantitativeContinuous() {
		return this.isQuantitativeContinuous;
	}

	public void setIsQuantitativeContinuous(Boolean isQuantitativeContinuous) {
		this.isQuantitativeContinuous = isQuantitativeContinuous;
	}

	public Boolean getIsQuantitativeDiscrete() {
		return this.isQuantitativeDiscrete;
	}

	public void setIsQuantitativeDiscrete(Boolean isQuantitativeDiscrete) {
		this.isQuantitativeDiscrete = isQuantitativeDiscrete;
	}

	public String getLongName() {
		return this.longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<LimesurveyQuestion> getLimesurveyQuestions() {
		return this.limesurveyQuestions;
	}

	public void setLimesurveyQuestions(List<LimesurveyQuestion> limesurveyQuestions) {
		this.limesurveyQuestions = limesurveyQuestions;
	}

	public LimesurveyQuestion addLimesurveyQuestion(LimesurveyQuestion limesurveyQuestion) {
		getLimesurveyQuestions().add(limesurveyQuestion);
		limesurveyQuestion.setStudyVariable(this);

		return limesurveyQuestion;
	}

	public LimesurveyQuestion removeLimesurveyQuestion(LimesurveyQuestion limesurveyQuestion) {
		getLimesurveyQuestions().remove(limesurveyQuestion);
		limesurveyQuestion.setStudyVariable(null);

		return limesurveyQuestion;
	}

	public StudyVariableClass getStudyVariableClass() {
		return this.studyVariableClass;
	}

	public void setStudyVariableClass(StudyVariableClass studyVariableClass) {
		this.studyVariableClass = studyVariableClass;
	}

}