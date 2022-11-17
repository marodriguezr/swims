package swimsWeb.controller.harvesting.thesis_record_assignment;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvesting.entities.OaiRecord;
import swimsEJB.model.harvesting.managers.OaiRecordManager;
import swimsWeb.utilities.JSFMessages;

import static swimsEJB.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_THESIS_SELECTION_WEBAPP_PATH;
import static swimsEJB.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_USER_SELECTION_WEBAPP_PATH;;

@Named
@SessionScoped
public class ThesisSelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserSelectionBean userSelectionBean;
	@EJB
	private OaiRecordManager oaiRecordManager;
	List<OaiRecord> oaiRecords;

	public ThesisSelectionBean() {
		// TODO Auto-generated constructor stub
	}

	public void setDefaultValues() {
		this.oaiRecords = oaiRecordManager.findAllUnassignedOaiRecords();
	}

	public String loadPage() {
		if (userSelectionBean.getSelectedUserDto() == null) {
			JSFMessages.WARN("Por favor seleccione un usuario antes de continuar.");
			return null;
		}
		return HARVESTING_THESIS_RECORD_ASSIGNMENT_THESIS_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public String onPageLoad() {
		if (userSelectionBean.getSelectedUserDto() == null) {
			JSFMessages.WARN("Por favor seleccione un usuario antes de continuar.");
			return HARVESTING_THESIS_RECORD_ASSIGNMENT_USER_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}

//		setDefaultValues();
		return null;
	}

	public List<OaiRecord> getOaiRecords() {
		return oaiRecords;
	}

	public void setOaiRecords(List<OaiRecord> oaiRecords) {
		this.oaiRecords = oaiRecords;
	}

}
