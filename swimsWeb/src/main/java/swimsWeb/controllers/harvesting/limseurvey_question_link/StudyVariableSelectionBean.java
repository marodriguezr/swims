package swimsWeb.controllers.harvesting.limseurvey_question_link;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvesting.entities.StudyVariable;
import swimsEJB.model.harvesting.managers.StudyVariableManager;
import swimsWeb.controllers.NavBarBean;
import swimsWeb.interfaces.OnRefreshEventListener;
import swimsWeb.utilities.JSFMessages;

import static swimsWeb.constants.WebappPaths.HARVESTING_LIMESURVEY_QUESTION_LINK_STUDY_VARIABLE_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_LIMESURVEY_QUESTION_LINK_SURVEY_SELECTION_WEBAPP_PATH;

@Named
@SessionScoped
public class StudyVariableSelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private SurveySelectionBean surveySelectionBean;
	private String selectedStudyVariableId;
	@Inject
	private NavBarBean navBarBean;
	private List<StudyVariable> studyVariables;
	@EJB
	private StudyVariableManager studyVariableManager;

	public StudyVariableSelectionBean() {
		// TODO Auto-generated constructor stub
	}

	public void loadStudyVariables() {
		this.studyVariables = studyVariableManager.findAllStudyVariables();
	}

	@PostConstruct
	public void onLoad() {
		loadStudyVariables();
	}

	public String loadPage() {
		if (surveySelectionBean.getSelectedSurveyId() == null) {
			JSFMessages.WARN("Debe seleccionar una Encuesta.");
			return null;
		}
		return HARVESTING_LIMESURVEY_QUESTION_LINK_STUDY_VARIABLE_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public String onPageLoad() {
		navBarBean.setOnRefreshEventListener(new OnRefreshEventListener() {
			@Override
			public void onRefreshEvent() {
				loadStudyVariables();
			}
		});
		navBarBean.setUpdatableFormString(":form");
		if (surveySelectionBean.getSelectedSurveyId() != null)
			return null;

		JSFMessages.WARN("Debe seleccionar una Encuesta.");
		return HARVESTING_LIMESURVEY_QUESTION_LINK_SURVEY_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public void clean() {
		surveySelectionBean.clean();
		this.selectedStudyVariableId = null;
	}

	public String getSelectedStudyVariableId() {
		return selectedStudyVariableId;
	}

	public void setSelectedStudyVariableId(String selectedStudyVariableId) {
		this.selectedStudyVariableId = selectedStudyVariableId;
	}

	public List<StudyVariable> getStudyVariables() {
		return studyVariables;
	}

	public void setStudyVariables(List<StudyVariable> studyVariables) {
		this.studyVariables = studyVariables;
	}

}
