package swimsWeb.controller.harvesting.thesis_record_assignment;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsWeb.dtos.OaiRecordSurveyAssignments;
import swimsWeb.utilities.JSFMessages;

import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_CONFIRMATION_WEBAPP_PATH;

@Named("thesisAssignmentConfirmationBean")
@SessionScoped
public class ConfirmationBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private SurveysSelectionBean surveysSelectionBean;
	@Inject
	private ThesisSelectionBean thesisSelectionBean;

	public ConfirmationBean() {
		// TODO Auto-generated constructor stub
	}

	public String onPageLoad() {
		if (surveysSelectionBean.getCompoundThesisSurveyAssignments().isEmpty()) {
			return HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		for (OaiRecordSurveyAssignments oaiRecordSurveyAssignments : surveysSelectionBean
				.getCompoundThesisSurveyAssignments()) {
			if (oaiRecordSurveyAssignments.getAssignedLimesurveySurveyIds().isEmpty()) {
				JSFMessages.WARN("La tesis " + oaiRecordSurveyAssignments.getOaiRecord().getTitle()
						+ " carece de encuestas asignadas. Por favor seleccione una o mas.");
				return HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
			}
		}
		return null;
	}

	public String loadPage() {
		if (surveysSelectionBean.getCompoundThesisSurveyAssignments().isEmpty()) {
			return null;
		}
		for (OaiRecordSurveyAssignments oaiRecordSurveyAssignments : surveysSelectionBean
				.getCompoundThesisSurveyAssignments()) {
			if (oaiRecordSurveyAssignments.getAssignedLimesurveySurveyIds().isEmpty()) {
				JSFMessages.WARN("La tesis " + oaiRecordSurveyAssignments.getOaiRecord().getTitle()
						+ " carece de encuestas asignadas. Por favor seleccione una o mas.");
				return null;
			}
		}
		return HARVESTING_THESIS_RECORD_ASSIGNMENT_CONFIRMATION_WEBAPP_PATH + "?faces-redirect=true";
	}
	
	public List<String> getLimesurveySurveyTitlesByLimesurveySurveyIds(List<Integer> selectedLimesurveySurveyIds) {
		return thesisSelectionBean.getLimesurveySurveyDtos().stream()
				.filter(arg0 -> selectedLimesurveySurveyIds.contains(arg0.getSid()))
				.collect(Collectors.toList()).stream().map(arg0 -> arg0.getSurveylsTitle())
				.collect(Collectors.toList());
	}

}
