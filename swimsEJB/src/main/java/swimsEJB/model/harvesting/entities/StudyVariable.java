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
	@Column(unique=true, nullable=false, length=32)
	private String id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(name="is_categorical_nominal", nullable=false)
	private Boolean isCategoricalNominal;

	@Column(name="is_categorical_ordinal", nullable=false)
	private Boolean isCategoricalOrdinal;

	@Column(name="is_numeric_continuous", nullable=false)
	private Boolean isNumericContinuous;

	@Column(name="is_numeric_discrete", nullable=false)
	private Boolean isNumericDiscrete;

	@Column(nullable=false, length=256)
	private String name;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	//bi-directional many-to-one association to Question
	@OneToMany(mappedBy="studyVariable")
	private List<Question> questions;

	//bi-directional many-to-one association to StudyVariableClass
	@ManyToOne
	@JoinColumn(name="study_variable_class_id", nullable=false)
	private StudyVariableClass studyVariableClass;

	public StudyVariable() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getIsCategoricalNominal() {
		return this.isCategoricalNominal;
	}

	public void setIsCategoricalNominal(Boolean isCategoricalNominal) {
		this.isCategoricalNominal = isCategoricalNominal;
	}

	public Boolean getIsCategoricalOrdinal() {
		return this.isCategoricalOrdinal;
	}

	public void setIsCategoricalOrdinal(Boolean isCategoricalOrdinal) {
		this.isCategoricalOrdinal = isCategoricalOrdinal;
	}

	public Boolean getIsNumericContinuous() {
		return this.isNumericContinuous;
	}

	public void setIsNumericContinuous(Boolean isNumericContinuous) {
		this.isNumericContinuous = isNumericContinuous;
	}

	public Boolean getIsNumericDiscrete() {
		return this.isNumericDiscrete;
	}

	public void setIsNumericDiscrete(Boolean isNumericDiscrete) {
		this.isNumericDiscrete = isNumericDiscrete;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Question> getQuestions() {
		return this.questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public Question addQuestion(Question question) {
		getQuestions().add(question);
		question.setStudyVariable(this);

		return question;
	}

	public Question removeQuestion(Question question) {
		getQuestions().remove(question);
		question.setStudyVariable(null);

		return question;
	}

	public StudyVariableClass getStudyVariableClass() {
		return this.studyVariableClass;
	}

	public void setStudyVariableClass(StudyVariableClass studyVariableClass) {
		this.studyVariableClass = studyVariableClass;
	}

}