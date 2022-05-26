package swimsWeb.controller.harvest;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.harvest.entities.OaiSet;
import swimsEJB.model.harvest.managers.OaiSetManager;

@Named
@SessionScoped
public class OrigenBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String oaiSetId;
	private List<OaiSet> oaiSets;
	@EJB
	private OaiSetManager oaiSetManager;

	public OrigenBean() {
		// TODO Auto-generated constructor stub
	}
	
	@PostConstruct
	public void onLoad() {
		this.oaiSets = oaiSetManager.findAllOaiSets();
	}
	
	public String loadPage() {
		return "/harvest/origen?faces-redirect=true";
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
