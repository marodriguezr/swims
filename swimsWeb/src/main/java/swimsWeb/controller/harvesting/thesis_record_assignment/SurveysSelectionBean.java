package swimsWeb.controller.harvesting.thesis_record_assignment;

import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_THESIS_SELECTION_WEBAPP_PATH;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvesting.entities.OaiRecord;
import swimsEJB.model.harvesting.managers.ThesisAssignmentManager;
import swimsWeb.dtos.OaiRecordSurveyAssignments;
import swimsWeb.utilities.JSFMessages;

@Named
@SessionScoped
public class SurveysSelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private ThesisSelectionBean thesisSelectionBean;
	private List<OaiRecordSurveyAssignments> compoundThesisSurveyAssignments;
	@EJB
	private ThesisAssignmentManager thesisAssignmentManager;

	public SurveysSelectionBean() {
		// TODO Auto-generated constructor stub
	}

	public void setDefaultValues() {
		try {
			this.compoundThesisSurveyAssignments = new ArrayList<>();
			for (OaiRecord oaiRecord : thesisSelectionBean.getSelectedOaiRecords()) {
				this.compoundThesisSurveyAssignments
						.add(new OaiRecordSurveyAssignments(oaiRecord,
								thesisAssignmentManager.filterLimesurveySurveysByAssignedLimesurveySurveyIds(
										thesisSelectionBean.getLimesurveySurveyDtos().stream()
												.map(arg0 -> arg0.getSid()).collect(Collectors.toList()),
										oaiRecord.getId())));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String loadPage() {
		if (thesisSelectionBean.getSelectedOaiRecords().isEmpty()) {
			JSFMessages.WARN("Por favor seleccione uno o varios registros de tesis antes de continuar.");
			return null;
		}
		setDefaultValues();
		return HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public String onPageLoad() {
		if (!thesisSelectionBean.getSelectedOaiRecords().isEmpty()) {
			return null;
		}
		JSFMessages.WARN("Por favor seleccione uno o varios registros de tesis antes de continuar.");
		return HARVESTING_THESIS_RECORD_ASSIGNMENT_THESIS_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}
	
	@PostConstruct
	public void onLoad() {
		this.compoundThesisSurveyAssignments = new ArrayList<>();
	}

	public List<OaiRecordSurveyAssignments> getCompoundThesisSurveyAssignments() {
		return compoundThesisSurveyAssignments;
	}

	public void setCompoundThesisSurveyAssignments(List<OaiRecordSurveyAssignments> compoundThesisSurveyAssignments) {
		this.compoundThesisSurveyAssignments = compoundThesisSurveyAssignments;
	}

}
