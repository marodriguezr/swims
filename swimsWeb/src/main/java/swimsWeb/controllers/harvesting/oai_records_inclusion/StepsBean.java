package swimsWeb.controllers.harvesting.oai_records_inclusion;

import static swimsWeb.constants.WebappPaths.HARVESTING_OAI_RECORDS_INCLUSION_CONFIRMATION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_OAI_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_OAI_RECORDS_INCLUSION_RECORDS_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_OAI_RECORDS_INCLUSION_SET_SELECTION_WEBAPP_PATH;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class StepsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int activeIndex = 0;

	public StepsBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		String viewId = ctx.getViewRoot().getViewId();
		switch (viewId) {
		case HARVESTING_OAI_RECORDS_INCLUSION_SET_SELECTION_WEBAPP_PATH + ".xhmtl":
			this.activeIndex = 0;
			break;
		case HARVESTING_OAI_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH + ".xhtml":
			this.activeIndex = 1;
			break;
		case HARVESTING_OAI_RECORDS_INCLUSION_RECORDS_SELECTION_WEBAPP_PATH + ".xhtml":
			this.activeIndex = 2;
			break;
		case HARVESTING_OAI_RECORDS_INCLUSION_CONFIRMATION_WEBAPP_PATH + ".xhtml":
			this.activeIndex = 3;
			break;
		default:
			break;
		}
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

}
