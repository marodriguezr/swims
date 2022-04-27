package swimsEJB.model.harvest.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Instant representation of the XML Schema Adjusted for usage in the OAI-PMH.
 * http://www.openarchives.org/OAI/2.0/oai_dc.xsd
 * @author miguel
 *
 */
public class OaiRecordDto {
	private List<String> titles;
	private List<String> creators;
	private List<String> subjects;
	private List<String> descriptions;
	private List<String> publishers;
	private List<String> contributors;
	private List<Date> dates;
	private List<String> types;
	private List<String> formats;
	private List<String> identifiers;
	private List<String> sources;
	private List<String> languages;
	private List<String> relations;
	private List<String> coverages;
	private List<String> rights;
	
	public OaiRecordDto() {
		this.titles = new ArrayList<String>();
		this.creators = new ArrayList<String>();
		this.subjects = new ArrayList<String>();
		this.descriptions = new ArrayList<String>();
		this.publishers = new ArrayList<String>();
		this.contributors = new ArrayList<String>();
		this.dates = new ArrayList<Date>();
		this.types = new ArrayList<String>();
		this.formats = new ArrayList<String>();
		this.identifiers = new ArrayList<String>();
		this.sources = new ArrayList<String>();
		this.languages = new ArrayList<String>();
		this.relations = new ArrayList<String>();
		this.coverages = new ArrayList<String>();
		this.rights = new ArrayList<String>();
	}

	public List<String> getTitles() {
		return titles;
	}

	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

	public List<String> getCreators() {
		return creators;
	}

	public void setCreators(List<String> creators) {
		this.creators = creators;
	}

	public List<String> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}

	public List<String> getPublishers() {
		return publishers;
	}

	public void setPublishers(List<String> publishers) {
		this.publishers = publishers;
	}

	public List<String> getContributors() {
		return contributors;
	}

	public void setContributors(List<String> contributors) {
		this.contributors = contributors;
	}

	public List<Date> getDates() {
		return dates;
	}

	public void setDates(List<Date> dates) {
		this.dates = dates;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public List<String> getFormats() {
		return formats;
	}

	public void setFormats(List<String> formats) {
		this.formats = formats;
	}

	public List<String> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(List<String> identifiers) {
		this.identifiers = identifiers;
	}

	public List<String> getSources() {
		return sources;
	}

	public void setSources(List<String> sources) {
		this.sources = sources;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	public List<String> getRelations() {
		return relations;
	}

	public void setRelations(List<String> relations) {
		this.relations = relations;
	}

	public List<String> getCoverages() {
		return coverages;
	}

	public void setCoverages(List<String> coverages) {
		this.coverages = coverages;
	}

	public List<String> getRights() {
		return rights;
	}

	public void setRights(List<String> rights) {
		this.rights = rights;
	}
}
