package swimsWeb.controller.auth;

import java.io.Serializable;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.auth.managers.UserManager;
import swimsWeb.utilities.JSFMessages;

@Named
@SessionScoped
public class AuthBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private UserManager userManager;
	@Inject
	private SignInBean signInBean;

	public AuthBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
	}

	public String onPageLoad() {
//		Checks whether if the user is requesting any of its accessible paths
		if (signInBean.getSignedUserDto() == null) {
			JSFMessages.ERROR("Acceso no permitido");
			return "/iniciar-sesion?faces-redirect=true";
		}
		try {
			if (userManager.verifyAuthorizationByAllWebappPaths(this.signInBean.getSignedUserDto().getId(),
					Arrays.asList(new String[] { FacesContext.getCurrentInstance().getViewRoot().getViewId() }))) {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());
		}
		return "/iniciar-sesion?faces-redirect=true";
	}

	public String signOutAction() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/index?faces-redirect=true";
	}

	/**
	 * Verifies if all of the given paths is available to the user.
	 * @param requiredWebappPaths
	 * @return
	 */
	public boolean verifyRenderabilityByAllWebappPaths(String... requiredWebappPaths) {
		if (signInBean.getSignedUserDto() == null)
			return false;
		try {
			return userManager.verifyAuthorizationByAllWebappPaths(signInBean.getSignedUserDto().getId(),
					Arrays.asList(requiredWebappPaths));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Verifies if at least one of the given paths is available to the user.
	 * @param requiredWebappPaths
	 * @return
	 */
	public boolean verifyRenderabilityByOneWebappPath(String... requiredWebappPaths) {
		if (signInBean.getSignedUserDto() == null)
			return false;
		try {
			return userManager.verifyAuthorizationByOneWebappPath(signInBean.getSignedUserDto().getId(),
					Arrays.asList(requiredWebappPaths));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
