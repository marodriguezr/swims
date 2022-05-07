/**
 * 
 */
package swimsWeb.controller.harvest;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.harvest.managers.OaiSetManager;
import swimsWeb.utilities.JSFMessages;

@Named
@SessionScoped
/**
 * @author miguel
 *
 */
public class OaiSetBean implements Serializable {

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
	public OaiSetBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onInit() {
		this.name = "";
		this.identifier = "";
	}

	public void createOneOaiSetActionListener() {
		try {
			oaiSetManager.createOneOaiSet(identifier, name, 0);
			JSFMessages.INFO("Set OAI creado de forma exitosa.");
			onInit();
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
