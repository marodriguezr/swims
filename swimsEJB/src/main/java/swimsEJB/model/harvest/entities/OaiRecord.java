package swimsEJB.model.harvest.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the oai_records database table.
 * 
 */
@Entity
@Table(name="oai_records", schema = "harvest")
@NamedQuery(name="OaiRecord.findAll", query="SELECT o FROM OaiRecord o")
public class OaiRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=2147483647)
	private String id;

	@Column(nullable=false, length=2147483647)
	private String contributor;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(nullable=false, length=2147483647)
	private String creator;

	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date date;

	@Column(nullable=false, length=2147483647)
	private String description;

	@Column(name="is_active", nullable=false)
	private Boolean isActive;

	@Column(nullable=false, length=2147483647)
	private String publisher;

	@Column(nullable=false, length=2147483647)
	private String subject;

	@Column(nullable=false, length=2147483647)
	private String title;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	@Column(nullable=false, length=2147483647)
	private String url;

	//bi-directional many-to-one association to OaiSet
	@ManyToOne
	@JoinColumn(name="oai_set_id", nullable=false)
	private OaiSet oaiSet;

	public OaiRecord() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContributor() {
		return this.contributor;
	}

	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getPublisher() {
		return this.publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public OaiSet getOaiSet() {
		return this.oaiSet;
	}

	public void setOaiSet(OaiSet oaiSet) {
		this.oaiSet = oaiSet;
	}

}