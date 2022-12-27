package swimsWeb.controllers.auth;

import static swimsWeb.constants.WebappPaths.AUTH_UPDATE_PASSWORD_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.SIGN_IN_WEBAPP_PATH;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.auth.managers.UserManager;
import swimsWeb.utilities.JSFMessages;

@Named
@SessionScoped
public class UpdatePasswordBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public UpdatePasswordBean() {
		// TODO Auto-generated constructor stub
	}

	@Inject
	private SignInBean signInBean;
	@Inject AuthBean authBean;
	@EJB
	private UserManager userManager;

	private String currentPassword;
	private String newPassword;
	private String newPasswordConfirmation;

	public String loadPage() {
		setDefaultValues();
		return AUTH_UPDATE_PASSWORD_WEBAPP_PATH + "?faces-redirect=true";
	}

	public void setDefaultValues() {
		currentPassword = "";
		newPassword = "";
		newPasswordConfirmation = "";
	}

	public String onPageLoad() {
		if (signInBean.getSignedUserDto() == null) {
			JSFMessages.WARN("Acceso no permitido.");
			return SIGN_IN_WEBAPP_PATH + "?faces-redirect=true";
		}
		return null;
	}

	public String updatePasswordActionListener() {
		if (!this.newPassword.equals(this.newPasswordConfirmation)) {
			JSFMessages.INFO("Las contraseñas no coinciden.");
			return null;
		}
		try {
			userManager.updateSelfPassword(this.signInBean.getSignedUserDto(), currentPassword, newPassword);
			JSFMessages.INFO("Contraseña actualizada de forma exitosa.");
			return authBean.signOutAction();
		} catch (Exception e) {
			// TODO: handle exception
			JSFMessages.WARN("La contrasena actual es incorrecta.");
			setDefaultValues();
			return null;
		}
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordConfirmation() {
		return newPasswordConfirmation;
	}

	public void setNewPasswordConfirmation(String newPasswordConfirmation) {
		this.newPasswordConfirmation = newPasswordConfirmation;
	}

}
