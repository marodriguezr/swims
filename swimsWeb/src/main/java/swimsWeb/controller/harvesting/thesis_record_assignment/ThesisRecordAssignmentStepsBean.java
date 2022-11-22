package swimsWeb.controller.harvesting.thesis_record_assignment;

import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_USER_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_THESIS_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_CONFIRMATION_WEBAPP_PATH;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class ThesisRecordAssignmentStepsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int activeIndex = 0;

	public ThesisRecordAssignmentStepsBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		String viewId = ctx.getViewRoot().getViewId();
		switch (viewId) {
		case HARVESTING_THESIS_RECORD_ASSIGNMENT_USER_SELECTION_WEBAPP_PATH + ".xhmtl":
			this.activeIndex = 0;
			break;
		case HARVESTING_THESIS_RECORD_ASSIGNMENT_THESIS_SELECTION_WEBAPP_PATH + ".xhtml":
			this.activeIndex = 1;
			break;
		case HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH + ".xhtml":
			this.activeIndex = 2;
			break;
		case HARVESTING_THESIS_RECORD_ASSIGNMENT_CONFIRMATION_WEBAPP_PATH + ".xhtml":
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
