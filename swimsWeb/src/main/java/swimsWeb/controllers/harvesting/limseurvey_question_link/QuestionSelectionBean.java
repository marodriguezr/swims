package swimsWeb.controllers.harvesting.limseurvey_question_link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvesting.dtos.LinkableLimesurveyQuestionDto;
import swimsEJB.model.harvesting.managers.LimesurveyQuestionManager;
import swimsWeb.controllers.NavBarBean;
import swimsWeb.controllers.harvesting.dtos.CompoundLinkableLimesurveyQuestionDto;
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
	private boolean showChildQuestions;
	private int onlyParentQuestionCount;
	private List<CompoundLinkableLimesurveyQuestionDto> compoundLinkableLimesurveyQuestionDtos;

	public QuestionSelectionBean() {
	}

	@PostConstruct
	public void onLoad() {
		this.compoundLinkableLimesurveyQuestionDtos = new ArrayList<>();
	}

	public void loadLinkableLimesurveyQuestionDtos() {
		try {
			this.linkableLimesurveyQuestionDtos = limesurveyQuestionManager
					.findAllLinkableLimesurveyQuestionDtos(surveySelectionBean.getSelectedSurveyId());
			this.onlyParentQuestionCount = (int) this.linkableLimesurveyQuestionDtos.stream()
					.filter(arg0 -> arg0.getLinkableChildLimesurveyQuestionDtos() == null
							|| arg0.getLinkableChildLimesurveyQuestionDtos().isEmpty())
					.count();
			if (this.compoundLinkableLimesurveyQuestionDtos.stream()
					.anyMatch(t -> t.getSelectedLimesurveyQuestionIds().size() > 0))
				return;
			this.compoundLinkableLimesurveyQuestionDtos = this.linkableLimesurveyQuestionDtos.stream()
					.map(arg0 -> new CompoundLinkableLimesurveyQuestionDto(
							arg0.getLinkableParentLimesurveyQuestionDto(),
							arg0.getLinkableChildLimesurveyQuestionDtos(), new ArrayList<>()))
					.collect(Collectors.toList());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR("Error en la carga de Preguntas.");
			this.onlyParentQuestionCount = 0;
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
		this.selectedLinkableLimesurveyQuestionDtos = new ArrayList<>();
		this.compoundLinkableLimesurveyQuestionDtos = new ArrayList<>();
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

	public boolean isShowChildQuestions() {
		return showChildQuestions;
	}

	public void setShowChildQuestions(boolean showChildQuestions) {
		this.showChildQuestions = showChildQuestions;
	}

	public int getOnlyParentQuestionCount() {
		return onlyParentQuestionCount;
	}

	public void setOnlyParentQuestionCount(int onlyParentQuestionCount) {
		this.onlyParentQuestionCount = onlyParentQuestionCount;
	}

	public List<CompoundLinkableLimesurveyQuestionDto> getCompoundLinkableLimesurveyQuestionDtos() {
		return compoundLinkableLimesurveyQuestionDtos;
	}

	public void setCompoundLinkableLimesurveyQuestionDtos(
			List<CompoundLinkableLimesurveyQuestionDto> compoundLinkableLimesurveyQuestionDtos) {
		this.compoundLinkableLimesurveyQuestionDtos = compoundLinkableLimesurveyQuestionDtos;
	}

}
