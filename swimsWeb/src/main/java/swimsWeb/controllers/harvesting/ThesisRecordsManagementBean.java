package swimsWeb.controllers.harvesting;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.harvesting.entities.OaiRecord;
import swimsEJB.model.harvesting.managers.OaiRecordManager;

import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_MANAGEMENT_WEBAPP_PATH;

@Named
@SessionScoped
public class ThesisRecordsManagementBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private OaiRecordManager oaiRecordManager;
	private List<OaiRecord> oaiRecords;

	public ThesisRecordsManagementBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		loadOaiRecords();
	}
	
	public void refresh() {
		loadOaiRecords();
	}

	public void loadOaiRecords() {
		this.oaiRecords = oaiRecordManager.findAllOaiRecords();
	}

	public String loadPage() {
		return HARVESTING_THESIS_RECORD_MANAGEMENT_WEBAPP_PATH + "?faces-redirect=true";
	}

	public List<OaiRecord> getOaiRecords() {
		return oaiRecords;
	}

	public void setOaiRecords(List<OaiRecord> oaiRecords) {
		this.oaiRecords = oaiRecords;
	}

}
