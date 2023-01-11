package swimsWeb.controllers.harvesting.thesis_records_inclusion;

import static swimsWeb.constants.WebappPaths.*;

import java.io.Serializable;
import java.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsWeb.utilities.JSFMessages;

@Named
@SessionScoped
public class DatesSelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private SetSelectionBean origenBean;
	private LocalDate from;
	private LocalDate until;

	public DatesSelectionBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
	}

	public String onPageLoad() {
		if (this.origenBean.getOaiSetId() != null)
			return null;
		JSFMessages.WARN("Por favor, seleccione un set OAI");
		return HARVESTING_THESIS_RECORDS_INCLUSION_SET_SELECTION_WEBAPP_PATH + "?faces-redirect=true";

	}

	public String loadPage() {
		if (this.origenBean.getOaiSetId() != null)
			return HARVESTING_THESIS_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		JSFMessages.WARN("Por favor, seleccione un set OAI");
		return null;
	}

	public void clean() {
		this.from = null;
		this.until = null;
		origenBean.clean();
	}

	public LocalDate getFrom() {
		return from;
	}

	public void setFrom(LocalDate from) {
		this.from = from;
	}

	public LocalDate getUntil() {
		return until;
	}

	public void setUntil(LocalDate until) {
		this.until = until;
	}
}
