package swimsWeb.controllers.harvesting.limseurvey_question_link;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvesting.dtos.LimesurveyQuestionDto;
import swimsEJB.model.harvesting.dtos.LinkableLimesurveyQuestionDto;
import swimsEJB.model.harvesting.managers.LimesurveyQuestionManager;
import swimsWeb.utilities.JSFMessages;

import static swimsWeb.constants.WebappPaths.HARVESTING_LIMESURVEY_QUESTION_LINK_CONFIRMATION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.INDEX_WEBAPP_PATH;

@Named("limesurveyQuestionLinkConfirmationBean")
@SessionScoped
public class ConfirmationBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private QuestionSelectionBean questionSelectionBean;
	@EJB
	private LimesurveyQuestionManager limesurveyQuestionManager;
	@Inject
	private StudyVariableSelectionBean studyVariableSelectionBean;

	public ConfirmationBean() {
		// TODO Auto-generated constructor stub
	}

	public String loadPage() {
		if (questionSelectionBean.isShowChildQuestions()
				&& questionSelectionBean.getCompoundLinkableLimesurveyQuestionDtos().stream()
						.allMatch(t -> t.getSelectedLimesurveyQuestionIds().isEmpty())) {
			JSFMessages.WARN("Debe seleccionar una o mas sub preguntas.");
			return null;
		}
		if (!questionSelectionBean.isShowChildQuestions()
				&& questionSelectionBean.getSelectedLinkableLimesurveyQuestionDtos() == null) {
			JSFMessages.WARN("Debe seleccionar una o mas preguntas.");
			return null;
		}
		if (!questionSelectionBean.isShowChildQuestions()
				&& questionSelectionBean.getSelectedLinkableLimesurveyQuestionDtos().size() == 0) {
			JSFMessages.WARN("Debe seleccionar una o mas preguntas.");
			return null;
		}

		return HARVESTING_LIMESURVEY_QUESTION_LINK_CONFIRMATION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public String onPageLoad() {
		if (questionSelectionBean.isShowChildQuestions()
				&& questionSelectionBean.getCompoundLinkableLimesurveyQuestionDtos().stream()
						.allMatch(t -> t.getSelectedLimesurveyQuestionIds().isEmpty())) {
			JSFMessages.WARN("Debe seleccionar una o mas sub preguntas.");
			return HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		if (!questionSelectionBean.isShowChildQuestions()
				&& questionSelectionBean.getSelectedLinkableLimesurveyQuestionDtos() == null) {
			JSFMessages.WARN("Debe seleccionar una o mas preguntas.");
			return HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		if (!questionSelectionBean.isShowChildQuestions()
				&& questionSelectionBean.getSelectedLinkableLimesurveyQuestionDtos().size() == 0) {
			JSFMessages.WARN("Debe seleccionar una o mas preguntas.");
			return HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}

		return null;
	}

	public void clean() {
		questionSelectionBean.clean();
	}

	public LimesurveyQuestionDto findOneLimesurveyQuestionDto(String limesurveyQuestionDtoId,
			List<LimesurveyQuestionDto> limesurveyQuestionDtos) {
		LimesurveyQuestionDto foundLimesurveyQuestionDto = limesurveyQuestionDtos.stream()
				.filter(t -> t.getId() == limesurveyQuestionDtoId).findFirst().orElse(null);
		return foundLimesurveyQuestionDto;
	}

	public String createManyLimeSurveyQuestionsAction() {
		try {
			limesurveyQuestionManager.createManyLimesurveyQuestions(
					studyVariableSelectionBean.getSelectedStudyVariableId(),
					questionSelectionBean.isShowChildQuestions()
							? questionSelectionBean.getCompoundLinkableLimesurveyQuestionDtos().stream()
									.map(arg0 -> new LinkableLimesurveyQuestionDto(
											arg0.getLinkableParentLimesurveyQuestionDto(),
											arg0.getSelectedLimesurveyQuestionIds().stream()
													.map(t -> findOneLimesurveyQuestionDto(t,
															arg0.getLinkableChildLimesurveyQuestionDtos()))
													.collect(Collectors.toList()).stream().filter(t -> t != null)
													.collect(Collectors.toList()),
											arg0.isParentQuestionAlreadyRegistered()))
									.collect(Collectors.toList())
							: questionSelectionBean.getSelectedLinkableLimesurveyQuestionDtos());
			JSFMessages.INFO("Preguntas enlazadas de forma exitosa.");
			clean();
//			return "";
			return INDEX_WEBAPP_PATH;
		} catch (Exception e) {
			// TODO: handle exception
			JSFMessages.ERROR("Ha ocurrido un error en el enlace de las preguntas. " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
