package swimsWeb.constants;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import static swimsEJB.constants.WebappPaths.HARVEST_MODULE_WEBAPP_PATH;
import static swimsEJB.constants.WebappPaths.SYSPARAMS_MODULE_WEBAPP_PATH;

@Named
@RequestScoped
public class WebappPathsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public WebappPathsBean() {
		// TODO Auto-generated constructor stub
	}

	public String getHARVEST_MODULE_WEBAPP_PATH() {
		return HARVEST_MODULE_WEBAPP_PATH;
	}
	
	public String getSYSPARAMS_MODULE_WEBAPP_PATH() {
		return SYSPARAMS_MODULE_WEBAPP_PATH;
	}
}