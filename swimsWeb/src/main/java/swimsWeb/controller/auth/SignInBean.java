package swimsWeb.controller.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.auth.dtos.UserDto;
import swimsEJB.model.auth.managers.UserManager;
import swimsEJB.model.core.managers.SeedManager;
import swimsWeb.utilities.JSFMessages;

@Named
@SessionScoped
public class SignInBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private UserManager userManager;
	@EJB
	private SeedManager coreManager;

	private String email;
	private String password;
	private UserDto signedUserDto;
	private List<String> accessibleWebappPaths;

	public SignInBean() {
		// TODO Auto-generated constructor stub
	}

	public String loadPage() {
		this.email = "";
		this.password = "";
		return "/iniciar-sesion?faces-redirect=true";
	}

	public String signInAction() {
		if (email.isEmpty() || password.isEmpty()) {
			JSFMessages.WARN("Por favor ingrese sus credenciales");
			return null;
		}
		try {
			this.signedUserDto = userManager.signIn(email, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.WARN(e.getMessage());
			return null;
		}
		findAllAccesibleWebappPathsByUserIdActionListener();
		JSFMessages.INFO("Sesión iniciada");
		return null;
	}

	public void findAllAccesibleWebappPathsByUserIdActionListener() {
		try {
			this.accessibleWebappPaths = userManager.findAllAccesibleWebappPathsByUserId(this.signedUserDto.getId());
		} catch (Exception e) {
			// TODO: handle exception
			this.accessibleWebappPaths = new ArrayList<>();
		}
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

	public List<String> getAccessibleWebappPaths() {
		return accessibleWebappPaths;
	}

	public void setAccessibleWebappPaths(List<String> accessibleWebappPaths) {
		this.accessibleWebappPaths = accessibleWebappPaths;
	}

	public UserDto getSignedUserDto() {
		return signedUserDto;
	}

	public void setSignedUserDto(UserDto signedUserDto) {
		this.signedUserDto = signedUserDto;
	}

}
