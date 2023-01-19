package swimsWeb.controllers.harvesting.limseurvey_question_link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvesting.dtos.LimesurveySurveyDto;
import swimsEJB.model.harvesting.services.LimesurveyService;
import swimsWeb.controllers.NavBarBean;
import swimsWeb.interfaces.OnRefreshEventListener;
import swimsWeb.utilities.JSFMessages;

import static swimsWeb.constants.WebappPaths.HARVESTING_LIMESURVEY_QUESTION_LINK_SURVEY_SELECTION_WEBAPP_PATH;

@Named
@SessionScoped
public class SurveySelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<LimesurveySurveyDto> limesurveySurveyDtos;
	@Inject
	private NavBarBean navBarBean;
	private Integer selectedSurveyId;

	public SurveySelectionBean() {
		// TODO Auto-generated constructor stub
	}

	public String loadPage() {
		return HARVESTING_LIMESURVEY_QUESTION_LINK_SURVEY_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public void loadLimesurveySurveyDtos() {
		try {
			this.limesurveySurveyDtos = LimesurveyService.listAllActiveSurveys();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR("Error en la adiquisición de encuestas de Limesurvey.");
			this.limesurveySurveyDtos = new ArrayList<>();
		}
	}

	public void onPageLoad() {
		navBarBean.setOnRefreshEventListener(new OnRefreshEventListener() {
			@Override
			public void onRefreshEvent() {
				loadLimesurveySurveyDtos();
			}
		});
		navBarBean.setUpdatableFormString(":form");
	}

	@PostConstruct
	public void onLoad() {
		loadLimesurveySurveyDtos();
	}

	public void clean() {
		this.selectedSurveyId = null;
	}

	public List<LimesurveySurveyDto> getLimesurveySurveyDtos() {
		return limesurveySurveyDtos;
	}

	public void setLimesurveySurveyDtos(List<LimesurveySurveyDto> limesurveySurveyDtos) {
		this.limesurveySurveyDtos = limesurveySurveyDtos;
	}

	public Integer getSelectedSurveyId() {
		return selectedSurveyId;
	}

	public void setSelectedSurveyId(Integer selectedSurveyId) {
		this.selectedSurveyId = selectedSurveyId;
	}

}
