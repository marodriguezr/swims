package swimsWeb.controller.auth;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.auth.managers.UserManager;
import swimsEJB.model.core.managers.SeedManager;
import swimsWeb.utilities.JSFMessages;

@Named
@SessionScoped
public class LoginBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private UserManager userManager;
	@EJB
	private SeedManager coreManager;
	
	public LoginBean() {
		// TODO Auto-generated constructor stub
	}
	
	public void createUserActionHandler() {
		try {
			//coreManager.seed();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());
		}
	}

}
