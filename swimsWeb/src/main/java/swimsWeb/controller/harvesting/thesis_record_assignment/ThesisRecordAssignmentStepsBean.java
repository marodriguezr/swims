package swimsWeb.controller.harvesting.thesis_record_assignment;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import static swimsEJB.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_USER_SELECTION_WEBAPP_PATH;

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
