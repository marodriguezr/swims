package swimsWeb.controllers.harvesting.thesis_records_inclusion;

import static swimsWeb.constants.WebappPaths.*;

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

import swimsEJB.model.harvesting.dtos.ThesisRecordDto;
import swimsEJB.model.harvesting.managers.ThesisRecordManager;
import swimsWeb.controllers.NavBarBean;
import swimsWeb.interfaces.OnRefreshEventListener;
import swimsWeb.utilities.JSFMessages;

@Named
@SessionScoped
public class RecordsSelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private DatesSelectionBean fechaBean;
	@Inject
	private SetSelectionBean origenBean;
	private List<ThesisRecordDto> thesisRecordDtos;
	private DateFormat dateFormat;
	private ThesisRecordDto selectedOaiRecordDto;
	private List<ThesisRecordDto> selectedOaiRecordDtos;
	@EJB
	private ThesisRecordManager thesisRecordManager;
	@Inject
	private NavBarBean navBarBean;

	public RecordsSelectionBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		this.dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		selectedOaiRecordDtos = new ArrayList<ThesisRecordDto>();
	}

	public String onPageLoad() {
		if (this.fechaBean.getFrom() == null || this.fechaBean.getUntil() == null) {
			JSFMessages.WARN("Debe seleccionar fechas entre las cuales se extraerán los datos.");
			return HARVESTING_THESIS_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		if (this.fechaBean.getUntil().compareTo(LocalDate.now()) > 0) {
			JSFMessages.WARN(
					"Debe seleccionar fechas apropiadas. Ni la fecha de inicio ni fin deben sobrepasar la fecha actual.");
			return HARVESTING_THESIS_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		if (this.fechaBean.getFrom().compareTo(this.fechaBean.getUntil()) >= 0) {
			JSFMessages
					.WARN("Debe seleccionar fechas apropiadas. La fecha de inicio debe ser menor a la fecha de fin.");
			return HARVESTING_THESIS_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
		}
		navBarBean.setOnRefreshEventListener(new OnRefreshEventListener() {
			@Override
			public void onRefreshEvent() {
				loadThesisRecordDtos();
			}
		});
		navBarBean.setUpdatableFormString(":form");

		return null;
	}

	public void loadThesisRecordDtos() {
		try {
			this.thesisRecordDtos = thesisRecordManager.parseStringsToThesisRecordDtos(
					thesisRecordManager.fetchThesisStrings(this.origenBean.getOaiSetId(), this.fechaBean.getFrom(),
							this.fechaBean.getUntil()));
			this.thesisRecordDtos = thesisRecordManager.removeBlankThesisRecordDtos(thesisRecordDtos);
			this.thesisRecordDtos = thesisRecordManager.removeDuplicateThesisRecordDtos(
					thesisRecordManager.thesisRecordsToThesisRecordDtos(thesisRecordManager.findAllThesisRecords()),
					this.thesisRecordDtos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR("Ha ocurrido un error en la adquisición de registros de tesis. " + e.getMessage());
			this.thesisRecordDtos = new ArrayList<>();
		}

	}

	public String loadPage(boolean avoidThesisRecordRefetching) {
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

		if (!avoidThesisRecordRefetching)
			loadThesisRecordDtos();
		return HARVESTING_THESIS_RECORDS_INCLUSION_RECORDS_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public String loadPage() {
		return loadPage(false);
	}

	public void autoSelectOaiRecordDtos() {
		this.selectedOaiRecordDtos = thesisRecordManager.filterThesisRecordDtosByKeyWords(
				new String[] { "desarrollo de", "implementación de", "desarrollar", "implementar" }, thesisRecordDtos);
	}

	public void clean() {
		this.selectedOaiRecordDto = null;
		this.selectedOaiRecordDtos = new ArrayList<>();
		fechaBean.clean();
	}

	public String getURLFromOaiRecordDto(ThesisRecordDto thesisRecordDto) {
		return thesisRecordManager.getURLFromThesisRecordDto(thesisRecordDto);
	}

	public String getFirstObjectFromListAsString(List<Object> objects) {
		if (objects.isEmpty())
			return "Registro sin dato disponible.";
		return objects.get(0).toString();
	}

	public List<ThesisRecordDto> getOaiRecordDtos() {
		return thesisRecordDtos;
	}

	public void setOaiRecordDtos(List<ThesisRecordDto> thesisRecordDtos) {
		this.thesisRecordDtos = thesisRecordDtos;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public ThesisRecordDto getSelectedOaiRecordDto() {
		return selectedOaiRecordDto;
	}

	public void setSelectedOaiRecordDto(ThesisRecordDto selectedOaiRecordDto) {
		this.selectedOaiRecordDto = selectedOaiRecordDto;
	}

	public List<ThesisRecordDto> getSelectedOaiRecordDtos() {
		return selectedOaiRecordDtos;
	}

	public void setSelectedOaiRecordDtos(List<ThesisRecordDto> selectedOaiRecordDtos) {
		this.selectedOaiRecordDtos = selectedOaiRecordDtos;
	}
}
