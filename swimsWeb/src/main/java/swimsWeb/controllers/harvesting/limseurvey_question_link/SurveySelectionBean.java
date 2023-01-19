package swimsWeb.controllers.harvesting.limseurvey_question_link;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import static swimsWeb.constants.WebappPaths.HARVESTING_LIMESURVEY_QUESTION_LINK_SURVEY_SELECTION_WEBAPP_PATH;

@Named
@SessionScoped
public class SurveySelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public SurveySelectionBean() {
		// TODO Auto-generated constructor stub
	}

	public String loadPage() {
		return HARVESTING_LIMESURVEY_QUESTION_LINK_SURVEY_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

}
