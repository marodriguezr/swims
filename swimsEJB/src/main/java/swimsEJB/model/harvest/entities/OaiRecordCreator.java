package swimsEJB.model.harvest.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the oai_record_creators database table.
 * 
 */
@Entity
@Table(name="oai_record_creators",schema = "harvest")
@NamedQuery(name="OaiRecordCreator.findAll", query="SELECT o FROM OaiRecordCreator o")
public class OaiRecordCreator implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(name="created_by", nullable=false)
	private Integer createdBy;

	@Column(nullable=false, length=2147483647)
	private String creator;

	@Column(name="is_active", nullable=false)
	private Boolean isActive;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	@Column(name="updated_by", nullable=false)
	private Integer updatedBy;

	//bi-directional many-to-one association to OaiRecord
	@ManyToOne
	@JoinColumn(name="oai_record_id", nullable=false)
	private OaiRecord oaiRecord;

	public OaiRecordCreator() {
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

	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public OaiRecord getOaiRecord() {
		return this.oaiRecord;
	}

	public void setOaiRecord(OaiRecord oaiRecord) {
		this.oaiRecord = oaiRecord;
	}

}