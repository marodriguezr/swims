package swimsWeb.controller.auth;

import java.io.IOException;
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
	private FacesContext facesContext;

	public AuthBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
	}

	public void onPageLoad() {
		this.facesContext = FacesContext.getCurrentInstance();
		try {
			if (this.signInBean.getAccessibleWebappPaths().stream()
					.anyMatch(facesContext.getViewRoot().getViewId()::contains))
				return;
		} catch (Exception e) {
			try {
				this.facesContext.getExternalContext()
						.redirect(this.facesContext.getExternalContext().getRequestContextPath() + "/iniciar-sesion.xhtml");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public String signOutAction() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/index?faces-redirect=true";
	}
}
