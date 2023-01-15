/**
 * 
 */
package swimsWeb.controllers.harvesting;

import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_SETS_MANAGEMENT_WEBAPP_PATH;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.harvesting.managers.ThesisSetManager;
import swimsWeb.utilities.JSFMessages;

@Named
@SessionScoped
/**
 * @author miguel
 *
 */
public class ThesisSetsManagementBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String identifier;

	@EJB
	private ThesisSetManager thesisSetManager;

	/**
	 * 
	 */
	public ThesisSetsManagementBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		this.name = "";
		this.identifier = "";
	}

	public String loadPage() {
		return HARVESTING_THESIS_SETS_MANAGEMENT_WEBAPP_PATH + "?faces-redirect=true";
	}

	public void createOneOaiSetActionListener() {
		try {
			thesisSetManager.createOneThesisSet(identifier, name);
			JSFMessages.INFO("Set de Tesis creado de forma exitosa.");
			onLoad();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			JSFMessages.ERROR("El Set ya ha sido registrado previamente.\n" + e.getMessage());
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
