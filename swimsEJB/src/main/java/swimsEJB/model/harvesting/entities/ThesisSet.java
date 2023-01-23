package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the thesis_sets database table.
 * 
 */
@Entity
@Table(name="thesis_sets", schema="harvesting")
@NamedQuery(name="ThesisSet.findAll", query="SELECT t FROM ThesisSet t")
public class ThesisSet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=2147483647)
	private String id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(name="is_active", nullable=false)
	private Boolean isActive;

	@Column(nullable=false, length=2147483647)
	private String name;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	//bi-directional many-to-one association to ThesisRecord
	@OneToMany(mappedBy="thesisSet")
	private List<ThesisRecord> thesisRecords;

	public ThesisSet() {
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

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public List<ThesisRecord> getThesisRecords() {
		return this.thesisRecords;
	}

	public void setThesisRecords(List<ThesisRecord> thesisRecords) {
		this.thesisRecords = thesisRecords;
	}

	public ThesisRecord addThesisRecord(ThesisRecord thesisRecord) {
		getThesisRecords().add(thesisRecord);
		thesisRecord.setThesisSet(this);

		return thesisRecord;
	}

	public ThesisRecord removeThesisRecord(ThesisRecord thesisRecord) {
		getThesisRecords().remove(thesisRecord);
		thesisRecord.setThesisSet(null);

		return thesisRecord;
	}

}