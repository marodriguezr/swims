package swimsWeb.controllers.harvesting.thesis_records_inclusion;

import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORDS_INCLUSION_SET_SELECTION_WEBAPP_PATH;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.harvesting.entities.ThesisSet;
import swimsEJB.model.harvesting.managers.ThesisSetManager;

@Named
@SessionScoped
public class SetSelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String oaiSetId;
	private List<ThesisSet> oaiSets;
	@EJB
	private ThesisSetManager thesisSetManager;

	public SetSelectionBean() {
		// TODO Auto-generated constructor stub
	}
	
	@PostConstruct
	public void onLoad() {
		this.oaiSets = thesisSetManager.findAllThesisSets();
	}
	
	public String loadPage() {
		this.oaiSets = thesisSetManager.findAllThesisSets();
		return HARVESTING_THESIS_RECORDS_INCLUSION_SET_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}
	
	public void clean() {
		this.oaiSetId = null;
	}

	public String getOaiSetId() {
		return oaiSetId;
	}

	public void setOaiSetId(String oaiSetId) {
		this.oaiSetId = oaiSetId;
	}

	public List<ThesisSet> getOaiSets() {
		return oaiSets;
	}

	public void setOaiSets(List<ThesisSet> oaiSets) {
		this.oaiSets = oaiSets;
	}
	
}
