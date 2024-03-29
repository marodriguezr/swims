package swimsWeb.controllers.harvesting.thesis_record_assignment;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvesting.dtos.ThesisRecordAssignedLimesurveySurveyIdsDto;
import swimsEJB.model.harvesting.managers.ThesisAssignmentManager;
import swimsWeb.utilities.JSFMessages;

import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_CONFIRMATION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.INDEX_WEBAPP_PATH;

@Named("thesisAssignmentConfirmationBean")
@SessionScoped
public class ConfirmationBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private SurveysSelectionBean surveysSelectionBean;
	@Inject
	private ThesisSelectionBean thesisSelectionBean;
	@Inject
	private UserSelectionBean userSelectionBean;
	@EJB
	private ThesisAssignmentManager thesisAssignmentManager;

	public ConfirmationBean() {
		// TODO Auto-generated constructor stub
	}

	public String onPageLoad() {
		if (surveysSelectionBean.getCompoundThesisSurveyAssignments().isEmpty()) {
			return HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		for (ThesisRecordAssignedLimesurveySurveyIdsDto oaiRecordSurveyAssignments : surveysSelectionBean
				.getCompoundThesisSurveyAssignments()) {
			if (oaiRecordSurveyAssignments.getAssignedLimesurveySurveyIds().isEmpty()) {
				JSFMessages.WARN("La tesis " + oaiRecordSurveyAssignments.getThesisRecord().getTitle()
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
		for (ThesisRecordAssignedLimesurveySurveyIdsDto oaiRecordSurveyAssignments : surveysSelectionBean
				.getCompoundThesisSurveyAssignments()) {
			if (oaiRecordSurveyAssignments.getAssignedLimesurveySurveyIds().isEmpty()) {
				JSFMessages.WARN("La tesis " + oaiRecordSurveyAssignments.getThesisRecord().getTitle()
						+ " carece de encuestas asignadas. Por favor seleccione una o mas.");
				return null;
			}
		}
		return HARVESTING_THESIS_RECORD_ASSIGNMENT_CONFIRMATION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public List<String> getLimesurveySurveyTitlesByLimesurveySurveyIds(List<Integer> selectedLimesurveySurveyIds) {
		return thesisSelectionBean.getLimesurveySurveyDtos().stream()
				.filter(arg0 -> selectedLimesurveySurveyIds.contains(arg0.getSid())).collect(Collectors.toList())
				.stream().map(arg0 -> arg0.getSurveylsTitle()).collect(Collectors.toList());
	}

	public void clean() {
		this.surveysSelectionBean.clean();
	}

	public String createManyThesisAssignementsAction() {
		try {
			thesisAssignmentManager.createManyThesisAssignemnts(
					surveysSelectionBean.getCompoundThesisSurveyAssignments(),
					userSelectionBean.getSelectedUserDto().getId());
			JSFMessages.INFO("Tesis y encuestas asignadas de forma exitosa.");
			clean();
			return INDEX_WEBAPP_PATH;
		} catch (Exception e) {
			// TODO: handle exception
			JSFMessages.ERROR(
					"Las asignaciones que está intentando crear ya han sido creadas por otra persona o La Encuesta se encuentra inactiva, por favor reinicie el proceso para ver datos actualizados. "
							+ e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

}
