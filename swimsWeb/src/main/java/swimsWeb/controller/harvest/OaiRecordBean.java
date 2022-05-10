/**
 * 
 */
package swimsWeb.controller.harvest;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.harvest.dtos.OaiRecordDto;
import swimsEJB.model.harvest.entities.OaiSet;
import swimsEJB.model.harvest.managers.OaiRecordManager;
import swimsEJB.model.harvest.managers.OaiSetManager;
import swimsWeb.utilities.JSFMessages;

@Named("oaiRecordBean")
@SessionScoped
/**
 * @author miguel
 *
 */
public class OaiRecordBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private OaiRecordManager oaiRecordManager;
	@EJB
	private OaiSetManager oaiSetManager;
	private List<OaiSet> oaiSets;
	private String oaiSetId;
	private LocalDate from;
	private LocalDate until;
	private List<OaiRecordDto> oaiRecordDtos;
	private DateFormat dateFormat;
	private OaiRecordDto selectedOaiRecordDto;

	/**
	 * 
	 */
	public OaiRecordBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onInit() {
		this.oaiSets = oaiSetManager.findAllOaiSets();
		dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		setDefaultValues();
	}

	public void setDefaultValues() {
		oaiSetId = "";
		oaiRecordDtos = new ArrayList<OaiRecordDto>();
		selectedOaiRecordDto = new OaiRecordDto();
	}

	public String forwardToDateSelection() {

		if (this.oaiSetId == null) {
			JSFMessages.WARN("Por favor, seleccione un set OAI");
			return "";
		}

		return "/harvest/fecha?faces-redirect=true";
	}

	public String forwardToFilterSelection() {
		if (this.oaiSetId == null) {
			JSFMessages.WARN("Por favor, seleccione un set OAI");
			return "/harvest/origen?faces-redirect=true";
		}
		if (from == null || until == null) {
			JSFMessages.WARN("Debe seleccionar fechas entre las cuales se extraerán los datos.");
			return "";
		}
		if (until.compareTo(LocalDate.now()) > 0) {
			JSFMessages.WARN(
					"Debe seleccionar fechas apropiadas. Ni la fecha de inicio ni fin deben sobrepasar la fecha actual.");
			return "";
		}
		if (from.compareTo(until) >= 0) {
			JSFMessages
					.WARN("Debe seleccionar fechas apropiadas. La fecha de inicio debe ser menor a la fecha de fin.");
			return "";
		}
		try {

			this.oaiRecordDtos = oaiRecordManager
					.parseStringToOaiRecordDtos2(oaiRecordManager.findManyOaiRecords2(oaiSetId, from, until));
			return "/harvest/filtrado?faces-redirect=true";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());
			return "";
		}
	}

	public List<OaiSet> getOaiSets() {
		return oaiSets;
	}

	public void setOaiSets(List<OaiSet> oaiSets) {
		this.oaiSets = oaiSets;
	}

	public String getOaiSetId() {
		return oaiSetId;
	}

	public void setOaiSetId(String oaiSetId) {
		this.oaiSetId = oaiSetId;
	}

	public LocalDate getFrom() {
		return from;
	}

	public void setFrom(LocalDate from) {
		this.from = from;
	}

	public LocalDate getUntil() {
		return until;
	}

	public void setUntil(LocalDate until) {
		this.until = until;
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
		System.out.println("this one");
		this.selectedOaiRecordDto = selectedOaiRecordDto;
	}
}
