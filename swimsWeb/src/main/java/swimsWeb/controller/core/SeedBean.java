package swimsWeb.controller.core;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import swimsEJB.model.core.managers.SeedManager;
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
	}

	public String onPageLoad() {
		try {
			if (seedManager.isSystemSeeded())
				return "/index?faces-redirect=true";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.INFO(e.getMessage());
		}
		return null;
	}

	public void loadPage() {
	}

	public String seedAction() {
		try {
			if (!password.equals(passwordConfirmation)) {
				JSFMessages.WARN("Las contraseñas no coinciden.");
				return null;
			}
			if (password.isEmpty()) {
				JSFMessages.WARN("Por favor ingrese una constraseña apropiada");
				return null;
			}
			seedManager.seed(this.firstName, this.lastName, this.email, this.password);
			JSFMessages.INFO("System susccesfully seeded.");
			return "/index?faces-redirect=true";
		} catch (Exception e) {
			// TODO: handle exception
			JSFMessages.WARN(e.getMessage());
			return null;
		}
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
