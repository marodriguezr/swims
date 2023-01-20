package swimsWeb.controllers.harvesting.limseurvey_question_link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvesting.dtos.LinkableLimesurveyQuestionDto;
import swimsEJB.model.harvesting.managers.LimesurveyQuestionManager;
import swimsWeb.controllers.NavBarBean;
import swimsWeb.interfaces.OnRefreshEventListener;
import swimsWeb.utilities.JSFMessages;

import static swimsWeb.constants.WebappPaths.HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_LIMESURVEY_QUESTION_LINK_STUDY_VARIABLE_SELECTION_WEBAPP_PATH;

@Named
@SessionScoped
public class QuestionSelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private StudyVariableSelectionBean studyVariableSelectionBean;
	@Inject
	private NavBarBean navBarBean;
	@Inject
	private SurveySelectionBean surveySelectionBean;
	@EJB
	private LimesurveyQuestionManager limesurveyQuestionManager;
	private List<LinkableLimesurveyQuestionDto> linkableLimesurveyQuestionDtos;
	private List<LinkableLimesurveyQuestionDto> selectedLinkableLimesurveyQuestionDtos;

	public QuestionSelectionBean() {
		// TODO Auto-generated constructor stub
	}

	public void loadLinkableLimesurveyQuestionDtos() {
		try {
			this.linkableLimesurveyQuestionDtos = limesurveyQuestionManager
					.findAllLinkableLimesurveyQuestionDtos(surveySelectionBean.getSelectedSurveyId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR("Error en la carga de Preguntas.");
		}
	}

	public String loadPage() {
		if (studyVariableSelectionBean.getSelectedStudyVariableId() == null) {
			JSFMessages.WARN("Debe seleccionar una Variable de Estudio");
			return null;
		}
		return HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public String onPageLoad() {
		if (studyVariableSelectionBean.getSelectedStudyVariableId() == null) {
			JSFMessages.WARN("Debe seleccionar una Variable de Estudio");
			return HARVESTING_LIMESURVEY_QUESTION_LINK_STUDY_VARIABLE_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		this.navBarBean.setOnRefreshEventListener(new OnRefreshEventListener() {
			@Override
			public void onRefreshEvent() {
				loadLinkableLimesurveyQuestionDtos();
			}
		});
		this.navBarBean.setUpdatableFormString(":form");
		loadLinkableLimesurveyQuestionDtos();
		return null;

	}

	public void clean() {
		this.selectedLinkableLimesurveyQuestionDtos = new ArrayList<>();
		studyVariableSelectionBean.clean();
	}

	public List<LinkableLimesurveyQuestionDto> getLinkableLimesurveyQuestionDtos() {
		return linkableLimesurveyQuestionDtos;
	}

	public void setLinkableLimesurveyQuestionDtos(List<LinkableLimesurveyQuestionDto> linkableLimesurveyQuestionDtos) {
		this.linkableLimesurveyQuestionDtos = linkableLimesurveyQuestionDtos;
	}

	public List<LinkableLimesurveyQuestionDto> getSelectedLinkableLimesurveyQuestionDtos() {
		return selectedLinkableLimesurveyQuestionDtos;
	}

	public void setSelectedLinkableLimesurveyQuestionDtos(
			List<LinkableLimesurveyQuestionDto> selectedLinkableLimesurveyQuestionDtos) {
		this.selectedLinkableLimesurveyQuestionDtos = selectedLinkableLimesurveyQuestionDtos;
	}

}
