package swimsWeb.controller.harvest;

import java.io.Serializable;
import java.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsWeb.utilities.JSFMessages;

@Named
@SessionScoped
public class FechaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private OrigenBean origenBean;
	private LocalDate from;
	private LocalDate until;

	public FechaBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
	}

	public String onPageLoad() {
		if (this.origenBean.getOaiSetId() != null)
			return null;
		JSFMessages.WARN("Por favor, seleccione un set OAI");
		return "/harvest/origen?faces-redirect=true";

	}

	public String loadPage() {
		if (this.origenBean.getOaiSetId() != null)
			return "/harvest/fecha?faces-redirect=true";
		JSFMessages.WARN("Por favor, seleccione un set OAI");
		return null;
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
