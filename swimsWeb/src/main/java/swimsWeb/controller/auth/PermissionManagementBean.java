package swimsWeb.controller.auth;

import java.io.Serializable;
import java.util.ArrayList;
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
		this.permissions = permissionManager.findAllPermissions();
		this.selectedPermissions = new ArrayList<>();
	}

	public void openNew() {
		this.selectedPermission = new Permission();
	}

	public boolean hasSelectedPermissions() {
		return this.selectedPermissions != null && !this.selectedPermissions.isEmpty();
	}

	public void inactivateSelectedPermissions() {
		for (Permission permission : selectedPermissions) {
			try {
				permissionManager.updateOnePermissionById(permission.getId(), permission.getName(),
						permission.getWebappRelatedPath(), false);
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

	public String getInactivateButtonMessage() {
		if (hasSelectedPermissions()) {
			int size = this.selectedPermissions.size();
			return size > 1 ? "inactivar " + size + " permisos" : "inactivar 1 permiso";
		}
		return "Inactivar";
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
