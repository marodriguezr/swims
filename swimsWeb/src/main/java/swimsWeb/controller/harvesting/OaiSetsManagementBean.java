/**
 * 
 */
package swimsWeb.controller.harvesting;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.harvesting.managers.OaiSetManager;
import swimsWeb.utilities.JSFMessages;

import static swimsEJB.constants.WebappPaths.HARVESTING_OAI_SETS_MANAGEMENT_WEBAPP_PATH;

@Named
@SessionScoped
/**
 * @author miguel
 *
 */
public class OaiSetsManagementBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String identifier;

	@EJB
	private OaiSetManager oaiSetManager;

	/**
	 * 
	 */
	public OaiSetsManagementBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		this.name = "";
		this.identifier = "";
	}
	
	public String loadPage() {
		System.out.println(1);
		return HARVESTING_OAI_SETS_MANAGEMENT_WEBAPP_PATH + "?faces-redirect=true";
	}

	public void createOneOaiSetActionListener() {
		try {
			oaiSetManager.createOneOaiSet(identifier, name, 0);
			JSFMessages.INFO("Set OAI creado de forma exitosa.");
			onLoad();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
