package swimsWeb.constants;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import static swimsEJB.constants.WebappPaths.*;

@Named
@RequestScoped
public class WebappPathsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public WebappPathsBean() {
		// TODO Auto-generated constructor stub
	}

	public String getHARVESTING_OAI_RECORDS_INCLUSION_CONFIRMATION_WEBAPP_PATH() {
		return HARVESTING_OAI_RECORDS_INCLUSION_CONFIRMATION_WEBAPP_PATH;
	}
	
	public String getHARVESTING_OAI_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH() {
		return HARVESTING_OAI_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH;
	}
	
	public String getHARVESTING_OAI_RECORDS_INCLUSION_RECORDS_SELECTION_WEBAPP_PATH() {
		return HARVESTING_OAI_RECORDS_INCLUSION_RECORDS_SELECTION_WEBAPP_PATH;
	}
	
	public String getHARVESTING_OAI_RECORDS_INCLUSION_SET_SELECTION_WEBAPP_PATH() {
		return HARVESTING_OAI_RECORDS_INCLUSION_SET_SELECTION_WEBAPP_PATH;
	}
	
	public String getHARVESTING_OAI_RECORDS_INCLUSION_WEBAPP_PATH() {
		return HARVESTING_OAI_RECORDS_INCLUSION_WEBAPP_PATH;
	}
	
	public String getHARVESTING_OAI_SETS_MANAGEMENT_WEBAPP_PATH() {
		return HARVESTING_OAI_SETS_MANAGEMENT_WEBAPP_PATH;
	}
	
	public String getHARVESTING_WEBAPP_PATH() {
		return HARVESTING_WEBAPP_PATH;
	}
}