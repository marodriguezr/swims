package swimsEJB.model.harvest.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


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

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(name="created_by", nullable=false)
	private Integer createdBy;

	@Column(name="is_active", nullable=false)
	private Boolean isActive;

	@Column(name="oai_set_id", nullable=false, length=2147483647)
	private String oaiSetId;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	@Column(name="updated_by", nullable=false)
	private Integer updatedBy;

	@Column(nullable=false, length=2147483647)
	private String url;

	//bi-directional many-to-one association to OaiRecordContributor
	@OneToMany(mappedBy="oaiRecord")
	private List<OaiRecordContributor> oaiRecordContributors;

	//bi-directional many-to-one association to OaiRecordCreator
	@OneToMany(mappedBy="oaiRecord")
	private List<OaiRecordCreator> oaiRecordCreators;

	//bi-directional many-to-one association to OaiRecordDate
	@OneToMany(mappedBy="oaiRecord")
	private List<OaiRecordDate> oaiRecordDates;

	//bi-directional many-to-one association to OaiRecordDescription
	@OneToMany(mappedBy="oaiRecord")
	private List<OaiRecordDescription> oaiRecordDescriptions;

	//bi-directional many-to-one association to OaiRecordIdentifier
	@OneToMany(mappedBy="oaiRecord")
	private List<OaiRecordIdentifier> oaiRecordIdentifiers;

	//bi-directional many-to-one association to OaiRecordPublisher
	@OneToMany(mappedBy="oaiRecord")
	private List<OaiRecordPublisher> oaiRecordPublishers;

	//bi-directional many-to-one association to OaiRecordSubject
	@OneToMany(mappedBy="oaiRecord")
	private List<OaiRecordSubject> oaiRecordSubjects;

	//bi-directional many-to-one association to OaiRecordTitle
	@OneToMany(mappedBy="oaiRecord")
	private List<OaiRecordTitle> oaiRecordTitles;

	public OaiRecord() {
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

	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getOaiSetId() {
		return this.oaiSetId;
	}

	public void setOaiSetId(String oaiSetId) {
		this.oaiSetId = oaiSetId;
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

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<OaiRecordContributor> getOaiRecordContributors() {
		return this.oaiRecordContributors;
	}

	public void setOaiRecordContributors(List<OaiRecordContributor> oaiRecordContributors) {
		this.oaiRecordContributors = oaiRecordContributors;
	}

	public OaiRecordContributor addOaiRecordContributor(OaiRecordContributor oaiRecordContributor) {
		getOaiRecordContributors().add(oaiRecordContributor);
		oaiRecordContributor.setOaiRecord(this);

		return oaiRecordContributor;
	}

	public OaiRecordContributor removeOaiRecordContributor(OaiRecordContributor oaiRecordContributor) {
		getOaiRecordContributors().remove(oaiRecordContributor);
		oaiRecordContributor.setOaiRecord(null);

		return oaiRecordContributor;
	}

	public List<OaiRecordCreator> getOaiRecordCreators() {
		return this.oaiRecordCreators;
	}

	public void setOaiRecordCreators(List<OaiRecordCreator> oaiRecordCreators) {
		this.oaiRecordCreators = oaiRecordCreators;
	}

	public OaiRecordCreator addOaiRecordCreator(OaiRecordCreator oaiRecordCreator) {
		getOaiRecordCreators().add(oaiRecordCreator);
		oaiRecordCreator.setOaiRecord(this);

		return oaiRecordCreator;
	}

	public OaiRecordCreator removeOaiRecordCreator(OaiRecordCreator oaiRecordCreator) {
		getOaiRecordCreators().remove(oaiRecordCreator);
		oaiRecordCreator.setOaiRecord(null);

		return oaiRecordCreator;
	}

	public List<OaiRecordDate> getOaiRecordDates() {
		return this.oaiRecordDates;
	}

	public void setOaiRecordDates(List<OaiRecordDate> oaiRecordDates) {
		this.oaiRecordDates = oaiRecordDates;
	}

	public OaiRecordDate addOaiRecordDate(OaiRecordDate oaiRecordDate) {
		getOaiRecordDates().add(oaiRecordDate);
		oaiRecordDate.setOaiRecord(this);

		return oaiRecordDate;
	}

	public OaiRecordDate removeOaiRecordDate(OaiRecordDate oaiRecordDate) {
		getOaiRecordDates().remove(oaiRecordDate);
		oaiRecordDate.setOaiRecord(null);

		return oaiRecordDate;
	}

	public List<OaiRecordDescription> getOaiRecordDescriptions() {
		return this.oaiRecordDescriptions;
	}

	public void setOaiRecordDescriptions(List<OaiRecordDescription> oaiRecordDescriptions) {
		this.oaiRecordDescriptions = oaiRecordDescriptions;
	}

	public OaiRecordDescription addOaiRecordDescription(OaiRecordDescription oaiRecordDescription) {
		getOaiRecordDescriptions().add(oaiRecordDescription);
		oaiRecordDescription.setOaiRecord(this);

		return oaiRecordDescription;
	}

	public OaiRecordDescription removeOaiRecordDescription(OaiRecordDescription oaiRecordDescription) {
		getOaiRecordDescriptions().remove(oaiRecordDescription);
		oaiRecordDescription.setOaiRecord(null);

		return oaiRecordDescription;
	}

	public List<OaiRecordIdentifier> getOaiRecordIdentifiers() {
		return this.oaiRecordIdentifiers;
	}

	public void setOaiRecordIdentifiers(List<OaiRecordIdentifier> oaiRecordIdentifiers) {
		this.oaiRecordIdentifiers = oaiRecordIdentifiers;
	}

	public OaiRecordIdentifier addOaiRecordIdentifier(OaiRecordIdentifier oaiRecordIdentifier) {
		getOaiRecordIdentifiers().add(oaiRecordIdentifier);
		oaiRecordIdentifier.setOaiRecord(this);

		return oaiRecordIdentifier;
	}

	public OaiRecordIdentifier removeOaiRecordIdentifier(OaiRecordIdentifier oaiRecordIdentifier) {
		getOaiRecordIdentifiers().remove(oaiRecordIdentifier);
		oaiRecordIdentifier.setOaiRecord(null);

		return oaiRecordIdentifier;
	}

	public List<OaiRecordPublisher> getOaiRecordPublishers() {
		return this.oaiRecordPublishers;
	}

	public void setOaiRecordPublishers(List<OaiRecordPublisher> oaiRecordPublishers) {
		this.oaiRecordPublishers = oaiRecordPublishers;
	}

	public OaiRecordPublisher addOaiRecordPublisher(OaiRecordPublisher oaiRecordPublisher) {
		getOaiRecordPublishers().add(oaiRecordPublisher);
		oaiRecordPublisher.setOaiRecord(this);

		return oaiRecordPublisher;
	}

	public OaiRecordPublisher removeOaiRecordPublisher(OaiRecordPublisher oaiRecordPublisher) {
		getOaiRecordPublishers().remove(oaiRecordPublisher);
		oaiRecordPublisher.setOaiRecord(null);

		return oaiRecordPublisher;
	}

	public List<OaiRecordSubject> getOaiRecordSubjects() {
		return this.oaiRecordSubjects;
	}

	public void setOaiRecordSubjects(List<OaiRecordSubject> oaiRecordSubjects) {
		this.oaiRecordSubjects = oaiRecordSubjects;
	}

	public OaiRecordSubject addOaiRecordSubject(OaiRecordSubject oaiRecordSubject) {
		getOaiRecordSubjects().add(oaiRecordSubject);
		oaiRecordSubject.setOaiRecord(this);

		return oaiRecordSubject;
	}

	public OaiRecordSubject removeOaiRecordSubject(OaiRecordSubject oaiRecordSubject) {
		getOaiRecordSubjects().remove(oaiRecordSubject);
		oaiRecordSubject.setOaiRecord(null);

		return oaiRecordSubject;
	}

	public List<OaiRecordTitle> getOaiRecordTitles() {
		return this.oaiRecordTitles;
	}

	public void setOaiRecordTitles(List<OaiRecordTitle> oaiRecordTitles) {
		this.oaiRecordTitles = oaiRecordTitles;
	}

	public OaiRecordTitle addOaiRecordTitle(OaiRecordTitle oaiRecordTitle) {
		getOaiRecordTitles().add(oaiRecordTitle);
		oaiRecordTitle.setOaiRecord(this);

		return oaiRecordTitle;
	}

	public OaiRecordTitle removeOaiRecordTitle(OaiRecordTitle oaiRecordTitle) {
		getOaiRecordTitles().remove(oaiRecordTitle);
		oaiRecordTitle.setOaiRecord(null);

		return oaiRecordTitle;
	}

}