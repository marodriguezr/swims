package swimsWeb.controllers.auth;

import static swimsWeb.constants.WebappPaths.AUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.auth.entities.Permission;
import swimsEJB.model.auth.managers.PermissionManager;
import swimsWeb.utilities.JSFMessages;

@Named
@SessionScoped
public class PermissionManagementBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public PermissionManagementBean() {
		// TODO Auto-generated constructor stub
	}

	@EJB
	private PermissionManager permissionManager;

	private List<Permission> permissions;
	private List<Permission> selectedPermissions;
	private Permission selectedPermission;

	@PostConstruct
	public void onLoad() {
		this.permissions = permissionManager.findAllPermissionsExcept(AUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH);
		this.selectedPermissions = new ArrayList<>();
	}

	public String loadPage() {
		return AUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH + "?faces-redirect=true";
	}

	public void openNew() {
		Permission newPermission = new Permission();
		newPermission.setId(null);
		newPermission.setName("");
		newPermission.setWebappRelatedPath("");
		newPermission.setIsActive(true);

		this.selectedPermission = newPermission;
	}

	public boolean hasSelectedPermissions() {
		return this.selectedPermissions != null && !this.selectedPermissions.isEmpty();
	}

	public void inactivateSelectedPermissions() {
		for (Permission permission : selectedPermissions) {
			try {
				permissionManager.updateOnePermissionById(permission.getId(), permission.getName(), false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFMessages.ERROR(
						e.getMessage() + "Nombre del permiso: " + permission.getId() + " " + permission.getName());
			}
		}
		JSFMessages.INFO(
				selectedPermissions.size() > 1 ? selectedPermissions.size() + " permisos inactivados de forma exitosa."
						: "1 permiso inactivado de forma exitosa.");
		permissions = permissionManager.findAllPermissions();
		selectedPermissions = new ArrayList<>();
	}

	public void inactivateSelectedPermission() {
		this.selectedPermissions = Arrays.asList(new Permission[] { selectedPermission });
		inactivateSelectedPermissions();
	}

	public void activatePermission(Permission permission) {
		try {
			permissionManager.updateOnePermissionById(permission.getId(), permission.getName(), true);
			JSFMessages.INFO("Permiso activado de forma exitosa.");
			permissions = permissionManager.findAllPermissions();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());

		}
	}

	public void deleteSelectedPermission() {
		try {
			permissionManager.deleteOnePermissionById(selectedPermission.getId());
			JSFMessages.INFO("Permiso eliminado de forma exitosa.");
			permissions = permissionManager.findAllPermissions();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());
		}
	}

	public String getInactivateButtonMessage() {
		if (hasSelectedPermissions()) {
			int size = this.selectedPermissions.size();
			return size > 1 ? "inactivar " + size + " permisos" : "inactivar 1 permiso";
		}
		return "Inactivar";
	}

	public void savePermission() {
		if (selectedPermission.getId() == null) {
			try {
				permissionManager.createOnePermission(selectedPermission.getName(),
						selectedPermission.getWebappRelatedPath());
				this.permissions = permissionManager.findAllPermissions();
				JSFMessages.INFO("Permiso creado de forma exitosa.");
				return;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFMessages.ERROR(e.getMessage());
			}
		}
		try {
			permissionManager.updateOnePermissionById(selectedPermission.getId(), selectedPermission.getName(),
					selectedPermission.getIsActive());
			this.permissions = permissionManager.findAllPermissions();
			JSFMessages.INFO("Permiso actualizado de forma exitosa.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());
		}
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public List<Permission> getSelectedPermissions() {
		return selectedPermissions;
	}

	public void setSelectedPermissions(List<Permission> selectedPermissions) {
		this.selectedPermissions = selectedPermissions;
	}

	public Permission getSelectedPermission() {
		return selectedPermission;
	}

	public void setSelectedPermission(Permission selectedPermission) {
		this.selectedPermission = selectedPermission;
	}

}
