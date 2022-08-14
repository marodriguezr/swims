package swimsWeb.controller.auth;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.auth.managers.UserManager;

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
		if (this.signInBean.getAccessibleWebappPaths().stream()
				.anyMatch(FacesContext.getCurrentInstance().getViewRoot().getViewId()::contains))
			return null;
		return "/iniciar-sesion?faces-redirect=true";
	}

	public String signOutAction() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/index?faces-redirect=true";
	}
}
