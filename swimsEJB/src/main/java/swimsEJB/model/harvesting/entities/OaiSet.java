package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the oai_sets database table.
 * 
 */
@Entity
@Table(name="oai_sets", schema = "harvesting")
@NamedQuery(name="OaiSet.findAll", query="SELECT o FROM OaiSet o")
public class OaiSet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(name="is_active")
	private Boolean isActive;

	private String name;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	//bi-directional many-to-one association to OaiRecord
	@OneToMany(mappedBy="oaiSet")
	private List<OaiRecord> oaiRecords;

	public OaiSet() {
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

	public List<OaiRecord> getOaiRecords() {
		return this.oaiRecords;
	}

	public void setOaiRecords(List<OaiRecord> oaiRecords) {
		this.oaiRecords = oaiRecords;
	}

	public OaiRecord addOaiRecord(OaiRecord oaiRecord) {
		getOaiRecords().add(oaiRecord);
		oaiRecord.setOaiSet(this);

		return oaiRecord;
	}

	public OaiRecord removeOaiRecord(OaiRecord oaiRecord) {
		getOaiRecords().remove(oaiRecord);
		oaiRecord.setOaiSet(null);

		return oaiRecord;
	}

}