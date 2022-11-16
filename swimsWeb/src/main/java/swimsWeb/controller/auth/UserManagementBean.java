package swimsWeb.controller.auth;

import static swimsEJB.constants.WebappPaths.AUTH_USER_MANAGEMENT_WEBAPP_PATH;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.auth.dtos.UserDto;
import swimsEJB.model.auth.entities.Group;
import swimsEJB.model.auth.managers.GroupManager;
import swimsEJB.model.auth.managers.UserManager;
import swimsWeb.utilities.JSFMessages;

@Named
@SessionScoped
public class UserManagementBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public UserManagementBean() {
		// TODO Auto-generated constructor stub
	}

	@EJB
	private UserManager userManager;
	@EJB
	private GroupManager groupManager;

	@Inject
	private SignInBean signInBean;

	private List<UserDto> userDtos;
	private List<UserDto> selectedUserDtos;
	private List<Group> groups;
	private List<Integer> selectedGroupIds;
	private UserDto selectedUserDto;
	private String password;
	private String passwordConfirmation;

	@PostConstruct
	public void onLoad() {
		this.findAllUserDtos();
		this.selectedUserDtos = new ArrayList<>();
		this.groups = signInBean != null ? groupManager.findAllActiveGroups() : new ArrayList<>();
		this.selectedGroupIds = new ArrayList<>();
	}

	public void findAllUserDtos() {
		this.userDtos = signInBean.getSignedUserDto() == null ? new ArrayList<>()
				: userManager.findAllUsers(signInBean.getSignedUserDto().isRoot(),
						signInBean.getSignedUserDto().getId());
	}

	public String loadPage() {
		return AUTH_USER_MANAGEMENT_WEBAPP_PATH + "?faces-redirect=true";
	}

	public void openNew() {
		UserDto newUserDto = new UserDto();
		newUserDto.setId(-1);
		newUserDto.setEmail("");
		newUserDto.setFirstName("");
		newUserDto.setLastName("");
		newUserDto.setActive(true);
		password = "";
		passwordConfirmation = "";

		this.selectedUserDto = newUserDto;
	}

	public boolean hasSelectedUserDtos() {
		return this.selectedUserDtos != null && !this.selectedUserDtos.isEmpty();
	}

	public void inactivateSelectedUserDtos() {
		for (UserDto userDto : selectedUserDtos) {
			try {
				userManager.updateOneUserById(userDto.getId(), userDto.getFirstName(), userDto.getLastName(),
						userDto.getEmail(), false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFMessages.ERROR(e.getMessage());
			}
		}
		JSFMessages
				.INFO(selectedUserDtos.size() > 1 ? selectedUserDtos.size() + " usuarios inactivados de forma exitosa."
						: "1 usuario inactivado de forma exitosa.");
		this.findAllUserDtos();
		selectedUserDtos = new ArrayList<>();
	}

	public void inactivateSelectedUserDto() {
		this.selectedUserDtos = Arrays.asList(new UserDto[] { selectedUserDto });
		inactivateSelectedUserDtos();
	}

	public void activateUserDto(UserDto userDto) {
		try {
			userManager.updateOneUserById(userDto.getId(), userDto.getFirstName(), userDto.getLastName(),
					userDto.getEmail(), true);
			JSFMessages.INFO("Usuario activado de forma exitosa.");
			this.findAllUserDtos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());

		}
	}

	public void deleteSelectedUserDto() {
		try {
			userManager.deleteOneUserById(this.selectedUserDto.getId());
			JSFMessages.INFO("Usuario eliminado de forma exitosa.");
			this.findAllUserDtos();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());
		}
	}

	public String getInactivateButtonMessage() {
		if (hasSelectedUserDtos()) {
			int size = this.selectedUserDtos.size();
			return size > 1 ? "inactivar " + size + " usuarios" : "inactivar 1 usuario";
		}
		return "Inactivar";
	}

	public void saveUserDto() {
		if (!password.equals(passwordConfirmation)) {
			JSFMessages.WARN("Las contraseñas no coinciden.");
			return;
		}
		if (selectedUserDto.getId() == -1) {
			if (password.isEmpty()) {
				JSFMessages.WARN("Por favor ingrese una constraseña apropiada");
				return;
			}
			try {
				userManager.createOneUserWithGroupIds(selectedUserDto.getFirstName(), selectedUserDto.getLastName(),
						selectedUserDto.getEmail(), this.password, selectedGroupIds);
				this.findAllUserDtos();
				JSFMessages.INFO("Usuario creado de forma exitosa.");
				return;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFMessages.ERROR(e.getMessage());
			}
		}
		try {
			userManager.updateOneUserById(selectedUserDto.getId(), selectedUserDto.getFirstName(),
					selectedUserDto.getLastName(), selectedUserDto.getEmail(), selectedUserDto.isActive(), password,
					selectedGroupIds);
			this.findAllUserDtos();
			JSFMessages.INFO("Usuario actualizado de forma exitosa.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());
		}
	}

	public void setSelectedUserDtoWithGroups(UserDto selectedUserDto) {
		this.selectedGroupIds = groupManager.findAllGroupIdsByUserId(selectedUserDto.getId());
		this.selectedUserDto = selectedUserDto;
	}

	public List<UserDto> getUserDtos() {
		return userDtos;
	}

	public void setUserDtos(List<UserDto> userDtos) {
		this.userDtos = userDtos;
	}

	public List<UserDto> getSelectedUserDtos() {
		return selectedUserDtos;
	}

	public void setSelectedUserDtos(List<UserDto> selectedUserDtos) {
		this.selectedUserDtos = selectedUserDtos;
	}

	public UserDto getSelectedUserDto() {
		return selectedUserDto;
	}

	public void setSelectedUserDto(UserDto selectedUserDto) {
		this.selectedUserDto = selectedUserDto;
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

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Integer> getSelectedGroupIds() {
		return selectedGroupIds;
	}

	public void setSelectedGroupIds(List<Integer> selectedGroupIds) {
		this.selectedGroupIds = selectedGroupIds;
	}

}