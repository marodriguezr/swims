package swimsWeb.controllers.harvesting;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.harvesting.entities.ThesisRecord;
import swimsEJB.model.harvesting.managers.ThesisRecordManager;

import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_MANAGEMENT_WEBAPP_PATH;

@Named
@SessionScoped
public class ThesisRecordsManagementBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private ThesisRecordManager thesisRecordManager;
	private List<ThesisRecord> thesisRecords;

	public ThesisRecordsManagementBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		loadThesisRecords();
	}
	
	public void refresh() {
		loadThesisRecords();
	}

	public void loadThesisRecords() {
		this.thesisRecords = thesisRecordManager.findAllThesisRecords();
	}

	public String loadPage() {
		return HARVESTING_THESIS_RECORD_MANAGEMENT_WEBAPP_PATH + "?faces-redirect=true";
	}

	public List<ThesisRecord> getThesisRecords() {
		return thesisRecords;
	}

	public void setThesisRecords(List<ThesisRecord> thesisRecords) {
		this.thesisRecords = thesisRecords;
	}

}
