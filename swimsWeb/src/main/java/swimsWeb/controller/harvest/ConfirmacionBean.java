package swimsWeb.controller.harvest;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.harvest.managers.OaiRecordManager;

@Named
@RequestScoped
public class ConfirmacionBean implements Serializable {

	@Inject
	private OrigenBean origenBean;
	@Inject
	private FechaBean fechaBean;
	@Inject
	private FiltradoBean filtradoBean;
	@EJB
	private OaiRecordManager oaiRecordManager;
	
	private int oaiRecordsCount;
	
	public ConfirmacionBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		
	}
	
	public void onPageLoad() {
		
	}
	
	public void loadPage() {
		
	}
	
	
}
