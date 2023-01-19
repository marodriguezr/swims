package swimsWeb.controllers.harvesting.limseurvey_question_link;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvesting.dtos.LimesurveyQuestionDto;
import swimsEJB.model.harvesting.entities.LimesurveyQuestion;
import swimsEJB.model.harvesting.managers.LimesurveyQuestionManager;
import swimsEJB.model.harvesting.services.LimesurveyService;
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
	private HashMap<String, LimesurveyQuestionDto> limesurveyQuestionDtosMap;
	private List<LimesurveyQuestion> alreadyRegisteredQuestions;
	@EJB
	private LimesurveyQuestionManager limesurveyQuestionManager;

	public QuestionSelectionBean() {
		// TODO Auto-generated constructor stub
	}

	public void loadLimesurveyQuestionDtosMap() {
		try {
			this.limesurveyQuestionDtosMap = LimesurveyService.listQuestions(surveySelectionBean.getSelectedSurveyId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR("Ha ocurrido un error en la adquisicón de preguntas disponibles.");
			this.limesurveyQuestionDtosMap = new HashMap<>();
		}
	}

	public void loadAlreadyRegisteredQuestions() {
		this.alreadyRegisteredQuestions = limesurveyQuestionManager
				.findAllQuestionsByLimesurveySurveyId(this.surveySelectionBean.getSelectedSurveyId());
	}

	@PostConstruct
	public void onLoad() {
		loadLimesurveyQuestionDtosMap();
		loadAlreadyRegisteredQuestions();
	}

	public String loadPage() {
		if (studyVariableSelectionBean.getSelectedStudyVariableId() == null) {
			JSFMessages.WARN("Debe seleccionar una Variable de Estudio");
			return null;
		}
		return HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public String onPageLoad() {
		this.navBarBean.setOnRefreshEventListener(new OnRefreshEventListener() {
			@Override
			public void onRefreshEvent() {
				loadLimesurveyQuestionDtosMap();
				loadAlreadyRegisteredQuestions();
			}
		});
		if (studyVariableSelectionBean.getSelectedStudyVariableId() != null)
			return null;
		JSFMessages.WARN("Debe seleccionar una Variable de Estudio");
		return HARVESTING_LIMESURVEY_QUESTION_LINK_STUDY_VARIABLE_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public HashMap<String, LimesurveyQuestionDto> getLimesurveyQuestionDtosMap() {
		return limesurveyQuestionDtosMap;
	}

	public void setLimesurveyQuestionDtosMap(HashMap<String, LimesurveyQuestionDto> limesurveyQuestionDtosMap) {
		this.limesurveyQuestionDtosMap = limesurveyQuestionDtosMap;
	}

}
