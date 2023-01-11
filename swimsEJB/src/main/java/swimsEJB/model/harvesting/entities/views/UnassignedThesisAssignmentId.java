package swimsEJB.model.harvesting.entities.views;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the unassigned_thesis_assignment_ids database table.
 * 
 */
@Entity
@Table(name = "unassigned_thesis_assignment_ids", schema = "harvesting")
@NamedQuery(name = "UnassignedThesisAssignmentId.findAll", query = "SELECT u FROM UnassignedThesisAssignmentId u")
public class UnassignedThesisAssignmentId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	public UnassignedThesisAssignmentId() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

}