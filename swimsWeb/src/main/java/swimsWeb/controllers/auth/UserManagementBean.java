package swimsWeb.controllers.auth;

import static swimsWeb.constants.WebappPaths.AUTH_USER_MANAGEMENT_WEBAPP_PATH;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import swimsEJB.model.auth.dtos.UserDto;
import swimsEJB.model.auth.entities.Group;
import swimsEJB.model.auth.managers.GroupManager;
import swimsEJB.model.auth.managers.UserManager;
import swimsWeb.controllers.NavBarBean;
import swimsWeb.interfaces.OnRefreshEventListener;
import swimsWeb.utilities.JSFMessages;

@Named
@SessionScoped
public class UserManagementBean implements Serializable {

	private static final long serialVersionUID = 1L;

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
	private UploadedFile uploadedFile;
	private StreamedContent xlsxTemplate;
	private StreamedContent odsTemplate;
	private StreamedContent csvTemplate;
	@Inject
	private NavBarBean navBarBean;

	public UserManagementBean() {
		// TODO Auto-generated constructor stub
		xlsxTemplate = DefaultStreamedContent.builder().name("plantilla.xlsx").contentType("text/xlsx")
				.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
						.getResourceAsStream("/resources/assets/plantilla.xlsx"))
				.build();
		odsTemplate = DefaultStreamedContent.builder().name("plantilla.ods").contentType("text/ods")
				.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
						.getResourceAsStream("/resources/assets/plantilla.ods"))
				.build();
		csvTemplate = DefaultStreamedContent.builder().name("plantilla.csv").contentType("text/csv")
				.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
						.getResourceAsStream("/resources/assets/plantilla.csv"))
				.build();
	}

	@PostConstruct
	public void onLoad() {
		this.findAllUserDtos();
		this.selectedUserDtos = new ArrayList<>();
		this.groups = signInBean != null ? groupManager.findAllActiveGroups(signInBean.getSignedUserDto().isRoot())
				: new ArrayList<>();
		this.selectedGroupIds = new ArrayList<>();
	}

	public void onPageLoad() {
		this.navBarBean.setOnRefreshEventListener(new OnRefreshEventListener() {
			@Override
			public void onRefreshEvent() {
				// TODO Auto-generated method stub
				onLoad();
			}
		});
		this.navBarBean.setUpdatableFormString(":form :dialogs");
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
		this.selectedGroupIds = new ArrayList<>();
	}

	public boolean hasSelectedUserDtos() {
		return this.selectedUserDtos != null && !this.selectedUserDtos.isEmpty();
	}

	public void inactivateSelectedUserDtos() {
		for (UserDto userDto : selectedUserDtos) {
			try {
				UserDto updatedUser = userManager.updateOneUserById(userDto.getId(), userDto.getFirstName(),
						userDto.getLastName(), false);
				this.userDtos.removeIf(t -> t.getId() == updatedUser.getId());
				this.userDtos.add(0, updatedUser);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFMessages.ERROR(e.getMessage());
			}
		}
		JSFMessages
				.INFO(selectedUserDtos.size() > 1 ? selectedUserDtos.size() + " usuarios inactivados de forma exitosa."
						: "1 usuario inactivado de forma exitosa.");

		selectedUserDtos = new ArrayList<>();
	}

	public void inactivateSelectedUserDto() {
		this.selectedUserDtos = Arrays.asList(new UserDto[] { selectedUserDto });
		inactivateSelectedUserDtos();
	}

	public void activateUserDto(UserDto userDto) {
		try {
			UserDto updatedUser = userManager.updateOneUserById(userDto.getId(), userDto.getFirstName(),
					userDto.getLastName(), true);
			JSFMessages.INFO("Usuario activado de forma exitosa.");
			this.userDtos.removeIf(t -> t.getId() == updatedUser.getId());
			this.userDtos.add(0, updatedUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
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

	public void openImportDialog() {
		this.uploadedFile = null;
		this.selectedGroupIds = new ArrayList<>();
	}

	public void saveUserDto() {
		if (!password.equals(passwordConfirmation)) {
			JSFMessages.WARN("Las contraseñas no coinciden.");
			return;
		}
		if (selectedUserDto.getId() == -1) {
			if (password.isBlank()) {
				JSFMessages.WARN("Por favor ingrese una constraseña apropiada");
				return;
			}
			try {
				UserDto createdUser = userManager.createOneUserWithGroupIds(selectedUserDto.getFirstName(),
						selectedUserDto.getLastName(), selectedUserDto.getEmail(), this.password, selectedGroupIds);
				this.userDtos.add(0, createdUser);
				JSFMessages.INFO("Usuario creado de forma exitosa.");
				return;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFMessages.ERROR(e.getMessage());
				return;
			}
		}
		try {
			UserDto updatedUser = userManager.updateOneUserById(selectedUserDto.getId(), selectedUserDto.getFirstName(),
					selectedUserDto.getLastName(), selectedUserDto.isActive(), password, selectedGroupIds);
			this.userDtos.removeIf(arg0 -> arg0.getId() == updatedUser.getId());
			this.userDtos.add(0, updatedUser);
			JSFMessages.INFO("Usuario actualizado de forma exitosa.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());
		}
	}

	public void importActionListener() {
		if (uploadedFile == null) {
			JSFMessages.WARN("Por favor seleccione un archivo .csv para continuar.");
			return;
		}
		if (!uploadedFile.getContentType().equals("text/csv")) {
			JSFMessages.WARN("Solo es posible procesar archivos csv.");
			return;
		}
		try {
			userDtos.addAll(userManager.importUsers(uploadedFile.getContent(), selectedGroupIds));
			JSFMessages.INFO("Usuarios importados de forma exitosa.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());
		}
	}

	public void setSelectedUserDtoWithGroups(UserDto selectedUserDto) {
		this.selectedGroupIds = groupManager.findAllGroupIdsByUserId(selectedUserDto.getId());
		this.selectedUserDto = selectedUserDto;
		password = "";
		passwordConfirmation = "";
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

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public StreamedContent getXlsxTemplate() {
		return xlsxTemplate;
	}

	public void setXlsxTemplate(StreamedContent xlsxTemplate) {
		this.xlsxTemplate = xlsxTemplate;
	}

	public StreamedContent getOdsTemplate() {
		return odsTemplate;
	}

	public void setOdsTemplate(StreamedContent odsTemplate) {
		this.odsTemplate = odsTemplate;
	}

	public StreamedContent getCsvTemplate() {
		return csvTemplate;
	}

	public void setCsvTemplate(StreamedContent csvTemplate) {
		this.csvTemplate = csvTemplate;
	}

}