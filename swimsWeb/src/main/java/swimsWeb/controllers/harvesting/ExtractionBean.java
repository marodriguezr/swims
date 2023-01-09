package swimsWeb.controllers.harvesting;

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
import swimsEJB.model.harvesting.entities.ExpectedAnswer;
import swimsEJB.model.harvesting.entities.LimesurveySurveyAssignment;
import swimsEJB.model.harvesting.entities.ThesisAssignment;
import swimsEJB.model.harvesting.managers.LimesurveySurveyAssignmentManager;
import swimsEJB.model.harvesting.managers.ExpectedAnswerManager;
import swimsEJB.model.harvesting.managers.AnswerManager;
import swimsEJB.model.harvesting.managers.OaiRecordManager;
import swimsEJB.model.harvesting.managers.ThesisAssignmentManager;
import swimsEJB.model.harvesting.services.LimesurveyService;
import swimsWeb.controllers.auth.SignInBean;
import swimsWeb.controllers.harvesting.thesis_record_assignment.ThesisSelectionBean;
import swimsWeb.utilities.JSFMessages;

import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_DATA_EXTRACTION_WEBAPP_PATH;
import static swimsEJB.constants.StudyVariables.BENEFICIARY_NAME_STUDY_VARIABLE_NAME;

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
	private List<ExpectedAnswer> beneficiaries;
	private String beneficiaryName;
	private Integer selectedBeneficiaryId;
	@EJB
	private AnswerManager answerManager;
	@EJB
	private ExpectedAnswerManager expectedAnswerManager;

	public ExtractionBean() {
		// TODO Auto-generated constructor stub
	}

	public String loadPage() {
		return HARVESTING_THESIS_RECORD_DATA_EXTRACTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public String onPageLoad() {
		loadThesisAssignments();
		return null;
	}

	public void loadThesisAssignments() {
		try {
			this.thesisAssignments = thesisAssignmentManager
					.findAllUndispatchedThesisAssignmentsByUserId(signInBean.getSignedUserDto().getId());
			for (ThesisAssignment thesisAssignment : thesisAssignments) {
				thesisAssignment.setLimesurveySurveyAssignments(limesurveySurveyAssignmentManager
						.findAllUndispatchedSurveyAssignmentsByThesisAssignementId(thesisAssignment.getId()));
			}

		} catch (Exception e) {
			// TODO: handle exception
			this.thesisAssignments = new ArrayList<>();
		}
	}

	public void loadBeneficiaries() {
		this.beneficiaries = expectedAnswerManager
				.findManyExpectedAnswersByStudyVariableId(BENEFICIARY_NAME_STUDY_VARIABLE_NAME);
	}

	public void openNewBeneficiaryDialog() {
		this.beneficiaryName = "";
	}

	@PostConstruct
	public void onLoad() {
		loadBeneficiaries();
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

	public void refresh() {
		loadBeneficiaries();
		loadThesisAssignments();
	}

	public void dispatchSurveyActionListener(LimesurveySurveyAssignment surveyAssignment) {
		try {
			limesurveySurveyAssignmentManager.dispatchSurvey(surveyAssignment);
			loadThesisAssignments();
//			this.thesisAssignments.removeIf(arg0 -> arg0.getId() == dispatchedSurveyAssignment.getId());
			JSFMessages.INFO("Ecuesta registrada de forma exitosa.");
		} catch (Exception e) {
			// TODO: handle exception
			JSFMessages.ERROR(e.getMessage());
			e.printStackTrace();
		}
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

	public void dispatchBeneficiaryAnswer(ThesisAssignment thesisAssignment) {
		if (this.selectedBeneficiaryId == null) {
			JSFMessages.WARN("Debe seleccionar una Entidad Beneficiaria.");
			return;
		}
		try {
			answerManager.dispatchBeneficiaryAnswer(this.selectedBeneficiaryId, thesisAssignment);
			JSFMessages.INFO("Nombre de la entidad beneficiaria registrado de forma exitosa.");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());
		}
	}

	public LimesurveySurveyDto findLimesurveySurveyById(int sid) {
		return thesisSelectionBean.getLimesurveySurveyDtos().stream().filter(arg0 -> arg0.getSid() == sid).findFirst()
				.orElse(null);
	}

	public void createNewBeneficiary() {
		try {
			ExpectedAnswer createdBeneficiary = this.expectedAnswerManager.createOneExpectedAnswer(beneficiaryName,
					BENEFICIARY_NAME_STUDY_VARIABLE_NAME);
			this.beneficiaries.add(createdBeneficiary);
			JSFMessages.INFO("Beneficiario creado de forma exitosa.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR("La entidad Beneficiaria ya exite.");
		}
	}

	public boolean isBeneficiaryStudyVariableAnsweredForThesisAssignent(ThesisAssignment thesisAssignment) {
		return answerManager.isBeneficiaryStudyVariableAnsweredForThesisAssignent(thesisAssignment);
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

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public Integer getSelectedBeneficiaryId() {
		return selectedBeneficiaryId;
	}

	public void setSelectedBeneficiaryId(Integer selectedBeneficiaryId) {
		this.selectedBeneficiaryId = selectedBeneficiaryId;
	}

	public List<ExpectedAnswer> getBeneficiaries() {
		return beneficiaries;
	}

	public void setBeneficiaries(List<ExpectedAnswer> beneficiaries) {
		this.beneficiaries = beneficiaries;
	}

}
