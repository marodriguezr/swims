package swimsWeb.controller.core;

import static swimsWeb.constants.WebappPaths.INDEX_WEBAPP_PATH;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import swimsEJB.model.core.managers.SeedManager;
import swimsWeb.constants.WebappPaths;
import swimsWeb.utilities.JSFMessages;

@Named
@RequestScoped
public class SeedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private SeedManager seedManager;

	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String passwordConfirmation;

	private Boolean isSystemSeeded;

	public SeedBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		firstName = "";
		lastName = "";
		email = "";
		password = "";
		passwordConfirmation = "";
		try {
			isSystemSeeded = seedManager.isSystemSeeded();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSystemSeeded = false;
			JSFMessages.INFO(
					"No se ha encontrado el esquema de base de datos. Por favor configure la base de datos antes de continuar.");
		}
	}

	public String onPageLoad() {
		if (this.isSystemSeeded)
			return INDEX_WEBAPP_PATH + "?faces-redirect=true";
		return null;
	}

	public void loadPage() {
	}

	public void seedActionListener() {
		try {
			if (!password.equals(passwordConfirmation)) {
				JSFMessages.WARN("Las contraseñas no coinciden.");
				return;
			}
			if (password.isEmpty()) {
				JSFMessages.WARN("Por favor ingrese una constraseña apropiada");
				return;
			}
			seedManager.seed(this.firstName, this.lastName, this.email, this.password,
					WebappPaths.getAllWebappPathsAsMap());
			this.isSystemSeeded = seedManager.isSystemSeeded();
		} catch (Exception e) {
			// TODO: handle exception
			JSFMessages.WARN(e.getMessage());
			e.printStackTrace();
		}
	}

	public Boolean getIsSystemSeeded() {
		return isSystemSeeded;
	}

	public void setIsSystemSeeded(Boolean isSystemSeeded) {
		this.isSystemSeeded = isSystemSeeded;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

}
