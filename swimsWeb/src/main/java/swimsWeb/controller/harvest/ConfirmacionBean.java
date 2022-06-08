package swimsWeb.controller.harvest;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvest.managers.OaiRecordManager;
import swimsWeb.utilities.JSFMessages;

@Named
@RequestScoped
public class ConfirmacionBean implements Serializable {

	@Inject
	private OrigenBean origenBean;
	@Inject
	private FechaBean fechaBean;
	@Inject
	private FiltradoBean filtradoBean;
	@EJB
	private OaiRecordManager oaiRecordManager;

	private int toAddOaiRecordsCount;

	public ConfirmacionBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		this.toAddOaiRecordsCount = filtradoBean.getSelectedOaiRecordDtos().size();
	}

	public String onPageLoad() {
		if (filtradoBean.getSelectedOaiRecordDtos().isEmpty()) {
			JSFMessages.WARN("Por favor, seleccione uno o mas registros antes de confirmar la adición de registros.");
			return "/harvest/confirmacion?faces-redirect=true";
		}
		return null;
	}

	public String loadPage() {
		if (filtradoBean.getSelectedOaiRecordDtos().isEmpty()) {
			JSFMessages.WARN("Por favor, seleccione uno o mas registros antes de avanzar.");
			return null;
		}
		return "/harvest/confirmacion?faces-redirect=true";
	}

	public int getToAddOaiRecordsCount() {
		return toAddOaiRecordsCount;
	}

	public void setToAddOaiRecordsCount(int toAddOaiRecordsCount) {
		this.toAddOaiRecordsCount = toAddOaiRecordsCount;
	}
}
