package swimsWeb.controller.harvesting.oai_records_inclusion;

import static swimsWeb.constants.WebappPaths.*;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvesting.managers.OaiRecordManager;
import swimsEJB.model.harvesting.managers.OaiSetManager;
import swimsWeb.utilities.JSFMessages;

@Named
@RequestScoped
public class ConfirmationBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private SetSelectionBean origenBean;
	@Inject
	private RecordsSelectionBean filtradoBean;
	@EJB
	private OaiRecordManager oaiRecordManager;
	@EJB
	private OaiSetManager oaiSetManager;

	private int toAddOaiRecordsCount;

	public ConfirmationBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		this.toAddOaiRecordsCount = filtradoBean.getSelectedOaiRecordDtos().size();
	}

	public String onPageLoad() {
		if (filtradoBean.getSelectedOaiRecordDtos().isEmpty()) {
			JSFMessages.WARN("Por favor, seleccione uno o mas registros antes de confirmar la adición de registros.");
			return HARVESTING_OAI_RECORDS_INCLUSION_RECORDS_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		return null;
	}

	public String loadPage() {
		if (filtradoBean.getSelectedOaiRecordDtos().isEmpty()) {
			JSFMessages.WARN("Por favor, seleccione uno o mas registros antes de avanzar.");
			return null;
		}
		return HARVESTING_OAI_RECORDS_INCLUSION_CONFIRMATION_WEBAPP_PATH + "?faces-redirect=true";
	}
	
	public void clean() {
		this.filtradoBean.clean();
	}

	public String createManyOaiRecordsAction() {
		try {
			oaiRecordManager.createManyOaiRecords(
					oaiRecordManager.oaiRecordDtosToOaiRecords(this.filtradoBean.getSelectedOaiRecordDtos()),
					oaiSetManager.findOneOaiSetById(this.origenBean.getOaiSetId()));
			JSFMessages.INFO("Registros creados de forma exitosa.");
			this.clean();
			return INDEX_WEBAPP_PATH + "?faces-redirec=true";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			JSFMessages.ERROR("Incapaz de crear los registros.");
			return null;
		}
	}

	public int getToAddOaiRecordsCount() {
		return toAddOaiRecordsCount;
	}

	public void setToAddOaiRecordsCount(int toAddOaiRecordsCount) {
		this.toAddOaiRecordsCount = toAddOaiRecordsCount;
	}
}
