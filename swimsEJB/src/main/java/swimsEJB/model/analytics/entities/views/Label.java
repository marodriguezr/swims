package swimsEJB.model.analytics.entities.views;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the labels database table.
 * 
 */
@Entity
@Table(name="labels", schema="analytics")
@NamedQuery(name="Label.findAll", query="SELECT l FROM Label l")
public class Label implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(length=256)
	private String label;

	@Column(name="study_variable_id", length=32)
	private String studyVariableId;

	public Label() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getStudyVariableId() {
		return this.studyVariableId;
	}

	public void setStudyVariableId(String studyVariableId) {
		this.studyVariableId = studyVariableId;
	}

}