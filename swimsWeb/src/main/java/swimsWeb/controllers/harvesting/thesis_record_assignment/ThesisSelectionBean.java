package swimsWeb.controllers.harvesting.thesis_record_assignment;

import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_THESIS_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_USER_SELECTION_WEBAPP_PATH;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvesting.dtos.LimesurveySurveyDto;
import swimsEJB.model.harvesting.dtos.ThesisRecordDto;
import swimsEJB.model.harvesting.entities.ThesisRecord;
import swimsEJB.model.harvesting.managers.ThesisRecordManager;
import swimsEJB.model.harvesting.services.LimesurveyService;
import swimsWeb.utilities.JSFMessages;;

@Named
@SessionScoped
public class ThesisSelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserSelectionBean userSelectionBean;
	@EJB
	private ThesisRecordManager thesisRecordManager;
	List<ThesisRecord> oaiRecords;
	List<ThesisRecord> selectedOaiRecords;
	private ThesisRecordDto selectedOaiRecordDto;
	private List<LimesurveySurveyDto> limesurveySurveyDtos;

	public ThesisSelectionBean() {
		// TODO Auto-generated constructor stub
	}

	public void setDefaultValues() {
		try {
			this.limesurveySurveyDtos = LimesurveyService.listAllActiveSurveys();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			this.limesurveySurveyDtos = new ArrayList<>();
		}
		try {
			this.oaiRecords = thesisRecordManager
					.findAllUnassignedThesisRecordsByLimesurveySurveyDtos(this.limesurveySurveyDtos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.oaiRecords = new ArrayList<>();
		}
	}

	public String loadPage() {
		if (userSelectionBean.getSelectedUserDto() == null) {
			JSFMessages.WARN("Por favor seleccione un usuario antes de continuar.");
			return null;
		}
		return HARVESTING_THESIS_RECORD_ASSIGNMENT_THESIS_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public String onPageLoad() {
		if (userSelectionBean.getSelectedUserDto() == null) {
			JSFMessages.WARN("Por favor seleccione un usuario antes de continuar.");
			return HARVESTING_THESIS_RECORD_ASSIGNMENT_USER_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		setDefaultValues();
		return null;
	}

	@PostConstruct
	public void onLoad() {
		this.selectedOaiRecords = new ArrayList<>();
		this.limesurveySurveyDtos = new ArrayList<>();
	}

	public void clean() {
		this.selectedOaiRecords = new ArrayList<>();
		userSelectionBean.clean();
	}

	public void setSelectedOaiRecordDtoWithExternalData(ThesisRecord oaiRecord) {
		try {
			this.selectedOaiRecordDto = thesisRecordManager.findOneExternalOaiRecordDtoById(oaiRecord.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.selectedOaiRecordDto = thesisRecordManager.oaiRecordToOaiRecordDto(oaiRecord);
		}
	}

	public List<ThesisRecord> getOaiRecords() {
		return oaiRecords;
	}

	public void setOaiRecords(List<ThesisRecord> oaiRecords) {
		this.oaiRecords = oaiRecords;
	}

	public List<ThesisRecord> getSelectedOaiRecords() {
		return selectedOaiRecords;
	}

	public void setSelectedOaiRecords(List<ThesisRecord> selectedOaiRecords) {
		this.selectedOaiRecords = selectedOaiRecords;
	}

	public ThesisRecordDto getSelectedOaiRecordDto() {
		return selectedOaiRecordDto;
	}

	public void setSelectedOaiRecordDto(ThesisRecordDto selectedOaiRecordDto) {
		this.selectedOaiRecordDto = selectedOaiRecordDto;
	}

	public List<LimesurveySurveyDto> getLimesurveySurveyDtos() {
		return limesurveySurveyDtos;
	}

	public void setLimesurveySurveyDtos(List<LimesurveySurveyDto> limesurveySurveyDtos) {
		this.limesurveySurveyDtos = limesurveySurveyDtos;
	}
}
