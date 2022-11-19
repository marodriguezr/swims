package swimsWeb.controller.harvesting.thesis_record_assignment;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsWeb.utilities.JSFMessages;

import static swimsEJB.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH;
import static swimsEJB.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_THESIS_SELECTION_WEBAPP_PATH;

@Named
@SessionScoped
public class SurveysSelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private ThesisSelectionBean thesisSelectionBean;

	public SurveysSelectionBean() {
		// TODO Auto-generated constructor stub
	}

	public String loadPage() {
		if (thesisSelectionBean.getSelectedOaiRecords().isEmpty()) {
			JSFMessages.WARN("Por favor seleccione uno o varios registros de tesis antes de continuar.");
			return null;
		}
		return HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public String onPageLoad() {
		if (!thesisSelectionBean.getSelectedOaiRecords().isEmpty()) {
			return null;
		}
		JSFMessages.WARN("Por favor seleccione uno o varios registros de tesis antes de continuar.");
		return HARVESTING_THESIS_RECORD_ASSIGNMENT_THESIS_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

}
