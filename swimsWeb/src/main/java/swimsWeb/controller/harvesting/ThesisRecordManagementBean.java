package swimsWeb.controller.harvesting;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_MANAGEMENT_WEBAPP_PATH;

@Named
@SessionScoped
public class ThesisRecordManagementBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public ThesisRecordManagementBean() {
		// TODO Auto-generated constructor stub
	}

	public String loadPage() {
		return HARVESTING_THESIS_RECORD_MANAGEMENT_WEBAPP_PATH + "?faces-redirect=true";
	}

}
