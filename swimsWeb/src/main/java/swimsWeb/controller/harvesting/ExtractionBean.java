package swimsWeb.controller.harvesting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.ResponsiveOption;

import swimsEJB.model.harvesting.dtos.LimesurveySurveyDto;
import swimsEJB.model.harvesting.entities.ThesisAssignment;
import swimsEJB.model.harvesting.managers.LimesurveySurveyAssignmentManager;
import swimsEJB.model.harvesting.managers.OaiRecordManager;
import swimsEJB.model.harvesting.managers.ThesisAssignmentManager;
import swimsEJB.model.harvesting.services.LimesurveyService;
import swimsWeb.controller.auth.SignInBean;
import swimsWeb.controller.harvesting.thesis_record_assignment.ThesisSelectionBean;
import swimsWeb.utilities.JSFMessages;

import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_DATA_EXTRACTION_WEBAPP_PATH;

@Named
@SessionScoped
public class ExtractionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<ThesisAssignment> thesisAssignments;
	@EJB
	private ThesisAssignmentManager thesisAssignmentManager;
	@EJB
	private LimesurveySurveyAssignmentManager limesurveySurveyAssignmentManager;
	@EJB
	private OaiRecordManager oaiRecordManager;
	@Inject
	private ThesisSelectionBean thesisSelectionBean;
	@Inject
	private SignInBean signInBean;
	private List<ResponsiveOption> responsiveOptions;

	public ExtractionBean() {
		// TODO Auto-generated constructor stub
	}

	public String loadPage() {
		return HARVESTING_THESIS_RECORD_DATA_EXTRACTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public String onPageLoad() {
		try {
			this.thesisAssignments = thesisAssignmentManager
					.findAllUndispatchedThesisAssignmentsByUserId(signInBean.getSignedUserDto().getId());
			for (ThesisAssignment thesisAssignment : thesisAssignments) {
				thesisAssignment.setLimesurveySurveyAssignments(limesurveySurveyAssignmentManager
						.findAllUndispatchedLimesurveySurveyAssignmentsByThesisAssignementId(thesisAssignment.getId()));
			}
		} catch (Exception e) {
			// TODO: handle exception
			this.thesisAssignments = new ArrayList<>();
		}
		return null;
	}

	@PostConstruct
	public void onLoad() {
		if (this.thesisSelectionBean.getLimesurveySurveyDtos().isEmpty()) {
			try {
				this.thesisSelectionBean.setLimesurveySurveyDtos(LimesurveyService.listAllActiveSurveys());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		responsiveOptions = new ArrayList<>();
		responsiveOptions.add(new ResponsiveOption("1024px", 3, 1));
		responsiveOptions.add(new ResponsiveOption("768px", 2, 1));
		responsiveOptions.add(new ResponsiveOption("560px", 1, 1));
	}

	public void extractLimesurveySurveyDataActionListener(int limesurveySurveyId, String token) {
		try {
			LimesurveyService.exportResponse(limesurveySurveyId, token);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.WARN(e.getMessage());
		}
	}

	public LimesurveySurveyDto findLimesurveySurveyById(int sid) {
		return thesisSelectionBean.getLimesurveySurveyDtos().stream().filter(arg0 -> arg0.getSid() == sid).findFirst()
				.orElse(null);
	}

	public List<ThesisAssignment> getThesisAssignments() {
		return thesisAssignments;
	}

	public void setThesisAssignments(List<ThesisAssignment> thesisAssignments) {
		this.thesisAssignments = thesisAssignments;
	}

	public List<ResponsiveOption> getResponsiveOptions() {
		return responsiveOptions;
	}

	public void setResponsiveOptions(List<ResponsiveOption> responsiveOptions) {
		this.responsiveOptions = responsiveOptions;
	}

}
