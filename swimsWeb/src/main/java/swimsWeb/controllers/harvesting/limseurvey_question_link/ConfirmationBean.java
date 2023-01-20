package swimsWeb.controllers.harvesting.limseurvey_question_link;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvesting.managers.LimesurveyQuestionManager;
import swimsWeb.utilities.JSFMessages;

import static swimsWeb.constants.WebappPaths.HARVESTING_LIMESURVEY_QUESTION_LINK_CONFIRMATION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.INDEX_WEBAPP_PATH;

@Named("limesurveyQuestionLinkConfirmationBean")
@ApplicationScoped
public class ConfirmationBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private QuestionSelectionBean questionSelectionBean;
	@Inject
	private StudyVariableSelectionBean studyVariableSelectionBean;
	@EJB
	private LimesurveyQuestionManager limesurveyQuestionManager;

	public ConfirmationBean() {
		// TODO Auto-generated constructor stub
	}

	public String loadPage() {
		if (questionSelectionBean.getSelectedLinkableLimesurveyQuestionDtos() == null) {
			JSFMessages.WARN("Debe seleccionar una o mas preguntas.");
			return null;
		}
		if (questionSelectionBean.getSelectedLinkableLimesurveyQuestionDtos().size() == 0) {
			JSFMessages.WARN("Debe seleccionar una o mas preguntas.");
			return null;
		}
		return HARVESTING_LIMESURVEY_QUESTION_LINK_CONFIRMATION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public String onPageLoad() {
		if (questionSelectionBean.getSelectedLinkableLimesurveyQuestionDtos() == null) {
			JSFMessages.WARN("Debe seleccionar una o mas preguntas.");
			return HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		if (questionSelectionBean.getSelectedLinkableLimesurveyQuestionDtos().size() == 0) {
			JSFMessages.WARN("Debe seleccionar una o mas preguntas.");
			return HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		return null;
	}

	public void clean() {
		questionSelectionBean.clean();
	}

	public String createManyLimeSurveyQuestionsAction() {
		try {
			limesurveyQuestionManager.createManyLimesurveyQuestions(
					studyVariableSelectionBean.getSelectedStudyVariableId(),
					questionSelectionBean.getSelectedLinkableLimesurveyQuestionDtos());
			JSFMessages.INFO("Preguntas enlazadas de forma exitosa.");
			clean();
			return INDEX_WEBAPP_PATH + "?faces-redirect=true";
		} catch (Exception e) {
			// TODO: handle exception
			JSFMessages.ERROR("Ha ocurrido un error en el enlace de las preguntas. " + e.getMessage());
			return null;
		}
	}
}
