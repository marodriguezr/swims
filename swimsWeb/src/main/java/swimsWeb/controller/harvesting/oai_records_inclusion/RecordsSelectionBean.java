package swimsWeb.controller.harvesting.oai_records_inclusion;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvesting.dtos.OaiRecordDto;
import swimsEJB.model.harvesting.managers.OaiRecordManager;
import swimsWeb.utilities.JSFMessages;

import static swimsEJB.constants.WebappPaths.*;

@Named
@SessionScoped
public class RecordsSelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private DatesSelectionBean fechaBean;
	@Inject
	private SetSelectionBean origenBean;
	private List<OaiRecordDto> oaiRecordDtos;
	private DateFormat dateFormat;
	private OaiRecordDto selectedOaiRecordDto;
	private List<OaiRecordDto> selectedOaiRecordDtos;
	@EJB
	private OaiRecordManager oaiRecordManager;

	public RecordsSelectionBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		this.dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		selectedOaiRecordDtos = new ArrayList<OaiRecordDto>();
	}

	public String onPageLoad() {
		if (this.fechaBean.getFrom() == null || this.fechaBean.getUntil() == null) {
			JSFMessages.WARN("Debe seleccionar fechas entre las cuales se extraerán los datos.");
			return HARVESTING_OAI_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		if (this.fechaBean.getUntil().compareTo(LocalDate.now()) > 0) {
			JSFMessages.WARN(
					"Debe seleccionar fechas apropiadas. Ni la fecha de inicio ni fin deben sobrepasar la fecha actual.");
			return HARVESTING_OAI_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		if (this.fechaBean.getFrom().compareTo(this.fechaBean.getUntil()) >= 0) {
			JSFMessages
					.WARN("Debe seleccionar fechas apropiadas. La fecha de inicio debe ser menor a la fecha de fin.");
			return HARVESTING_OAI_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		return null;
	}

	public String loadPage() {
		if (this.fechaBean.getFrom() == null || this.fechaBean.getUntil() == null) {
			JSFMessages.WARN("Debe seleccionar fechas entre las cuales se extraerán los datos.");
			return null;
		}
		if (this.fechaBean.getUntil().compareTo(LocalDate.now()) > 0) {
			JSFMessages.WARN(
					"Debe seleccionar fechas apropiadas. Ni la fecha de inicio ni fin deben sobrepasar la fecha actual.");
			return null;
		}
		if (this.fechaBean.getFrom().compareTo(this.fechaBean.getUntil()) >= 0) {
			JSFMessages
					.WARN("Debe seleccionar fechas apropiadas. La fecha de inicio debe ser menor a la fecha de fin.");
			return null;
		}
		try {
			this.oaiRecordDtos = oaiRecordManager.parseStringsToOaiRecordDtos(oaiRecordManager.fetchOaiStrings(
					this.origenBean.getOaiSetId(), this.fechaBean.getFrom(), this.fechaBean.getUntil()));
			this.oaiRecordDtos = oaiRecordManager.removeDuplicateOaiRecordDtos(
					oaiRecordManager.oaiRecordsToOaiRecordDtos(oaiRecordManager.findAllOaiRecords()),
					this.oaiRecordDtos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());
			return null;
		}
		return HARVESTING_OAI_RECORDS_INCLUSION_RECORDS_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public void autoSelectOaiRecordDtos() {
		this.selectedOaiRecordDtos = oaiRecordManager.filterOaiRecordDtosByKeyWords(
				new String[] { "desarrollo de", "implementación de", "desarrollar", "implementar" }, oaiRecordDtos);
	}

	public String getURLFromOaiRecordDto(OaiRecordDto oaiRecordDto) {
		return oaiRecordManager.getURLFromOaiRecordDto(oaiRecordDto);
	}

	public List<OaiRecordDto> getOaiRecordDtos() {
		return oaiRecordDtos;
	}

	public void setOaiRecordDtos(List<OaiRecordDto> oaiRecordDtos) {
		this.oaiRecordDtos = oaiRecordDtos;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public OaiRecordDto getSelectedOaiRecordDto() {
		return selectedOaiRecordDto;
	}

	public void setSelectedOaiRecordDto(OaiRecordDto selectedOaiRecordDto) {
		this.selectedOaiRecordDto = selectedOaiRecordDto;
	}

	public List<OaiRecordDto> getSelectedOaiRecordDtos() {
		return selectedOaiRecordDtos;
	}

	public void setSelectedOaiRecordDtos(List<OaiRecordDto> selectedOaiRecordDtos) {
		this.selectedOaiRecordDtos = selectedOaiRecordDtos;
	}
}
