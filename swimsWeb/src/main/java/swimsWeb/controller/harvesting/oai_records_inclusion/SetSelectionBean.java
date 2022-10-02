package swimsWeb.controller.harvesting.oai_records_inclusion;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.harvesting.entities.OaiSet;
import swimsEJB.model.harvesting.managers.OaiSetManager;

import static swimsEJB.constants.WebappPaths.HARVESTING_OAI_RECORDS_INCLUSION_SET_SELECTION_WEBAPP_PATH;

@Named
@SessionScoped
public class SetSelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String oaiSetId;
	private List<OaiSet> oaiSets;
	@EJB
	private OaiSetManager oaiSetManager;

	public SetSelectionBean() {
		// TODO Auto-generated constructor stub
	}
	
	@PostConstruct
	public void onLoad() {
		this.oaiSets = oaiSetManager.findAllOaiSets();
	}
	
	public String loadPage() {
		this.oaiSets = oaiSetManager.findAllOaiSets();
		return HARVESTING_OAI_RECORDS_INCLUSION_SET_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public String getOaiSetId() {
		return oaiSetId;
	}

	public void setOaiSetId(String oaiSetId) {
		this.oaiSetId = oaiSetId;
	}

	public List<OaiSet> getOaiSets() {
		return oaiSets;
	}

	public void setOaiSets(List<OaiSet> oaiSets) {
		this.oaiSets = oaiSets;
	}
	
}
