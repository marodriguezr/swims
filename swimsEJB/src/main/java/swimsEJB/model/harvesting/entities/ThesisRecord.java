package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the thesis_records database table.
 * 
 */
@Entity
@Table(name="thesis_records", schema="harvesting")
@NamedQuery(name="ThesisRecord.findAll", query="SELECT t FROM ThesisRecord t")
public class ThesisRecord implements Serializable {
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

	@Column(nullable=false, length=2147483647)
	private String description;

	@Temporal(TemporalType.DATE)
	@Column(name="inferred_creation_date", nullable=false)
	private Date inferredCreationDate;

	@Temporal(TemporalType.DATE)
	@Column(name="inferred_issue_date", nullable=false)
	private Date inferredIssueDate;

	@Column(name="is_active", nullable=false)
	private Boolean isActive;

	@Column(nullable=false, length=2147483647)
	private String subject;

	@Column(nullable=false, length=2147483647)
	private String title;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	@Column(nullable=false, length=2147483647)
	private String url;

	//bi-directional many-to-one association to ThesisAssignment
	@OneToMany(mappedBy="thesisRecord")
	private List<ThesisAssignment> thesisAssignments;

	//bi-directional many-to-one association to ThesisSet
	@ManyToOne
	@JoinColumn(name="thesis_set_id", nullable=false)
	private ThesisSet thesisSet;

	public ThesisRecord() {
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getInferredCreationDate() {
		return this.inferredCreationDate;
	}

	public void setInferredCreationDate(Date inferredCreationDate) {
		this.inferredCreationDate = inferredCreationDate;
	}

	public Date getInferredIssueDate() {
		return this.inferredIssueDate;
	}

	public void setInferredIssueDate(Date inferredIssueDate) {
		this.inferredIssueDate = inferredIssueDate;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public List<ThesisAssignment> getThesisAssignments() {
		return this.thesisAssignments;
	}

	public void setThesisAssignments(List<ThesisAssignment> thesisAssignments) {
		this.thesisAssignments = thesisAssignments;
	}

	public ThesisAssignment addThesisAssignment(ThesisAssignment thesisAssignment) {
		getThesisAssignments().add(thesisAssignment);
		thesisAssignment.setThesisRecord(this);

		return thesisAssignment;
	}

	public ThesisAssignment removeThesisAssignment(ThesisAssignment thesisAssignment) {
		getThesisAssignments().remove(thesisAssignment);
		thesisAssignment.setThesisRecord(null);

		return thesisAssignment;
	}

	public ThesisSet getThesisSet() {
		return this.thesisSet;
	}

	public void setThesisSet(ThesisSet thesisSet) {
		this.thesisSet = thesisSet;
	}

}