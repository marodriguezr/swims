package swimsWeb.controller.harvesting.thesis_record_assignment;

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

import swimsEJB.model.harvesting.dtos.OaiRecordDto;
import swimsEJB.model.harvesting.entities.OaiRecord;
import swimsEJB.model.harvesting.managers.OaiRecordManager;
import swimsWeb.dtos.LimesurveySurveyDto;
import swimsWeb.services.LimesurveyService;
import swimsWeb.utilities.JSFMessages;;

@Named
@SessionScoped
public class ThesisSelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserSelectionBean userSelectionBean;
	@EJB
	private OaiRecordManager oaiRecordManager;
	List<OaiRecord> oaiRecords;
	List<OaiRecord> selectedOaiRecords;
	private OaiRecordDto selectedOaiRecordDto;
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
			this.oaiRecords = oaiRecordManager.findAllUnassignedOaiRecords(this.limesurveySurveyDtos.size());
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
	}

	public void setSelectedOaiRecordDtoWithExternalData(OaiRecord oaiRecord) {
		try {
			this.selectedOaiRecordDto = oaiRecordManager.findOneExternalOaiRecordDtoById(oaiRecord.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.selectedOaiRecordDto = oaiRecordManager.oaiRecordToOaiRecordDto(oaiRecord);
		}
	}

	public List<OaiRecord> getOaiRecords() {
		return oaiRecords;
	}

	public void setOaiRecords(List<OaiRecord> oaiRecords) {
		this.oaiRecords = oaiRecords;
	}

	public List<OaiRecord> getSelectedOaiRecords() {
		return selectedOaiRecords;
	}

	public void setSelectedOaiRecords(List<OaiRecord> selectedOaiRecords) {
		this.selectedOaiRecords = selectedOaiRecords;
	}

	public OaiRecordDto getSelectedOaiRecordDto() {
		return selectedOaiRecordDto;
	}

	public void setSelectedOaiRecordDto(OaiRecordDto selectedOaiRecordDto) {
		this.selectedOaiRecordDto = selectedOaiRecordDto;
	}

	public List<LimesurveySurveyDto> getLimesurveySurveyDtos() {
		return limesurveySurveyDtos;
	}

	public void setLimesurveySurveyDtos(List<LimesurveySurveyDto> limesurveySurveyDtos) {
		this.limesurveySurveyDtos = limesurveySurveyDtos;
	}
}
