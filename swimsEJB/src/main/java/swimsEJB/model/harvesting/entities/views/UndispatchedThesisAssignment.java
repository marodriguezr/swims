package swimsEJB.model.harvesting.entities.views;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the undispatched_thesis_assignments database table.
 * 
 */
@Entity
@Table(name = "undispatched_thesis_assignments", schema = "harvesting")
@NamedQuery(name = "UndispatchedThesisAssignment.findAll", query = "SELECT u FROM UndispatchedThesisAssignment u")
public class UndispatchedThesisAssignment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name = "thesis_record_id")
	private String thesisRecordId;

	@Column(name = "user_id")
	private Integer userId;

	public UndispatchedThesisAssignment() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getThesisRecordId() {
		return this.thesisRecordId;
	}

	public void setThesisRecordId(String thesisRecordId) {
		this.thesisRecordId = thesisRecordId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}