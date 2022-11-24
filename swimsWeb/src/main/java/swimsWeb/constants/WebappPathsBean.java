package swimsWeb.constants;

import static swimsWeb.constants.WebappPaths.*;
import static swimsEJB.constants.SystemEnvironmentVariables.*;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

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

	public String getINDEX_WEBAPP_PATH() {
		return INDEX_WEBAPP_PATH;
	}

	public String getAUTH_WEBAPP_PATH() {
		return AUTH_WEBAPP_PATH;
	}

	public String getAUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH() {
		return AUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH;
	}

	public String getAUTH_USER_MANAGEMENT_WEBAPP_PATH() {
		return AUTH_USER_MANAGEMENT_WEBAPP_PATH;
	}

	public String getHARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH() {
		return HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH;
	}

	public String getHARVESTING_THESIS_RECORD_DATA_EXTRACTION_WEBAPP_PATH() {
		return HARVESTING_THESIS_RECORD_DATA_EXTRACTION_WEBAPP_PATH;
	}

	public String getLIMESURVEY_PUBLIC_HOST() {
		return LIMESURVEY_PUBLIC_HOST;
	}

	public String getLIMESURVEY_PUBLIC_PORT() {
		return LIMESURVEY_PUBLIC_PORT;
	}

	public String getLIMESURVEY_BASE_URL() {
		return LIMESURVEY_BASE_URL;
	}
	
	public String getLIMESURVEY_PROTOCOL() {
		return LIMESURVEY_PROTOCOL;
	}
}