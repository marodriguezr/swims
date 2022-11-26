package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the study_variable_classes database table.
 * 
 */
@Entity
@Table(name="study_variable_classes", schema="harvesting")
@NamedQuery(name="StudyVariableClass.findAll", query="SELECT s FROM StudyVariableClass s")
public class StudyVariableClass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(name="long_name", nullable=false, length=2147483647)
	private String longName;

	@Column(name="short_name", nullable=false, length=2147483647)
	private String shortName;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	//bi-directional many-to-one association to StudyVariable
	@OneToMany(mappedBy="studyVariableClass")
	private List<StudyVariable> studyVariables;

	public StudyVariableClass() {
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

	public List<StudyVariable> getStudyVariables() {
		return this.studyVariables;
	}

	public void setStudyVariables(List<StudyVariable> studyVariables) {
		this.studyVariables = studyVariables;
	}

	public StudyVariable addStudyVariable(StudyVariable studyVariable) {
		getStudyVariables().add(studyVariable);
		studyVariable.setStudyVariableClass(this);

		return studyVariable;
	}

	public StudyVariable removeStudyVariable(StudyVariable studyVariable) {
		getStudyVariables().remove(studyVariable);
		studyVariable.setStudyVariableClass(null);

		return studyVariable;
	}

}