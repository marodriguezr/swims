package swimsWeb.controllers.harvesting.limseurvey_question_link;

import static swimsWeb.constants.WebappPaths.HARVESTING_LIMESURVEY_QUESTION_LINK_SURVEY_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_LIMESURVEY_QUESTION_LINK_STUDY_VARIABLE_SELECTION_WEBAPP_PATH;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import static swimsWeb.constants.WebappPaths.HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_LIMESURVEY_QUESTION_LINK_CONFIRMATION_WEBAPP_PATH;

@Named
@ViewScoped
public class LimesurveyQuestionLinkStepsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int activeIndex = 0;

	public LimesurveyQuestionLinkStepsBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		String viewId = ctx.getViewRoot().getViewId();
		switch (viewId) {
		case HARVESTING_LIMESURVEY_QUESTION_LINK_SURVEY_SELECTION_WEBAPP_PATH + ".xhtml":
			this.activeIndex = 0;
			break;
		case HARVESTING_LIMESURVEY_QUESTION_LINK_STUDY_VARIABLE_SELECTION_WEBAPP_PATH + ".xhtml":
			this.activeIndex = 1;
			break;
		case HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH + ".xhtml":
			this.activeIndex = 2;
			break;
		case HARVESTING_LIMESURVEY_QUESTION_LINK_CONFIRMATION_WEBAPP_PATH + ".xhtml":
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
