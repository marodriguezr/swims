package swimsWeb.controllers.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import swimsEJB.model.auth.entities.Group;
import swimsEJB.model.auth.entities.Permission;
import swimsEJB.model.auth.managers.GroupManager;
import swimsEJB.model.auth.managers.PermissionManager;
import swimsWeb.controllers.NavBarBean;
import swimsWeb.interfaces.OnRefreshEventListener;
import swimsWeb.utilities.JSFMessages;

import static swimsWeb.constants.WebappPaths.AUTH_GROUP_MANAGEMENT_WEBAPP_PATH;

@Named
@SessionScoped
public class GroupManagementBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Group> groups;
	@EJB
	private GroupManager groupManager;
	@Inject
	private SignInBean signInBean;
	private List<Group> selectedGroups;
	private Group selectedGroup;
	private List<Integer> selectedPermissionIds;
	private List<Permission> permissions;
	private List<Permission> permissionsExceptGroupManagementPermission;
	@EJB
	private PermissionManager permissionManager;
	@Inject
	private NavBarBean navBarBean;

	public GroupManagementBean() {
		// TODO Auto-generated constructor stub
	}

	public String loadPage() {
		return AUTH_GROUP_MANAGEMENT_WEBAPP_PATH + "?faces-redirect=true";
	}

	@PostConstruct
	public void onLoad() {
		this.groups = signInBean.getSignedUserDto() == null ? new ArrayList<>()
				: groupManager.findAllGroups(signInBean.getSignedUserDto().isRoot());
		this.selectedGroups = new ArrayList<>();
		this.permissionsExceptGroupManagementPermission = signInBean.getSignedUserDto() == null ? new ArrayList<>()
				: permissionManager.findAllActivePermissionsExcept(AUTH_GROUP_MANAGEMENT_WEBAPP_PATH);
		this.permissions = signInBean.getSignedUserDto() == null ? new ArrayList<>()
				: permissionManager.findAllActivePermissions();
	}

	public void onPageLoad() {
		navBarBean.setOnRefreshEventListener(new OnRefreshEventListener() {
			@Override
			public void onRefreshEvent() {
				onLoad();
			}
		});
		navBarBean.setToUpdateFormId(":form");
	}

	public void openNew() {
		this.selectedGroup = new Group();
		this.selectedGroup.setId(-1);
		this.selectedGroup.setIsRoot(false);
		this.selectedPermissionIds = new ArrayList<>();
	}

	public void saveGroup() {
		if (selectedGroup.getId() == -1) {
			if (selectedGroup.getName().isBlank()) {
				JSFMessages.WARN("Por favor ingrese un nombre apropiado");
				return;
			}
			try {
				Group createdGroup = groupManager.createOneGroupWithPermissionIds(selectedGroup.getName(),
						selectedPermissionIds);
				this.groups.add(0, createdGroup);
				JSFMessages.INFO("Grupo creado de forma exitosa.");
				return;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFMessages.ERROR(e.getMessage());
				return;
			}
		}
		try {
			Permission groupManagementPermission = permissionManager
					.findOnePermissionByRelatedWebappPath(AUTH_GROUP_MANAGEMENT_WEBAPP_PATH);
			if (this.selectedGroup.getIsRoot()) {
				this.selectedPermissionIds.add(groupManagementPermission.getId());
				this.selectedGroup.setIsActive(true);
			}
			Group updatedGroup = groupManager.updateOneGroupById(selectedGroup.getId(), selectedGroup.getName(),
					selectedGroup.getIsActive(), selectedPermissionIds);
			this.groups.removeIf(arg0 -> arg0.getId() == updatedGroup.getId());
			this.groups.add(0, updatedGroup);
			JSFMessages.INFO("Grupo actualizado de forma exitosa.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());
		}
	}

	public void setSelectedGroupWithPermissions(Group selectedGroup) {
		this.selectedPermissionIds = permissionManager.findAllPermissionIdsByGroupId(selectedGroup.getId());
		this.selectedGroup = selectedGroup;
	}

	public void inactivateSelectedGroups() {
		int groupsCount = selectedGroups.size();
		for (Group group : selectedGroups) {
			try {
				if (group.getIsRoot()) {
					groupsCount--;
					continue;
				}
				Group updatedGroup = groupManager.updateOneGroupById(group.getId(), null, false);
				this.groups.removeIf(t -> t.getId() == updatedGroup.getId());
				this.groups.add(0, updatedGroup);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFMessages.ERROR(e.getMessage());
			}
		}
		JSFMessages.INFO(groupsCount > 1 ? groupsCount + " grupos inactivados de forma exitosa."
				: groupsCount == 1 ? "grupo inactivado de forma exitosa." : "0 Grupos modificados");

		selectedGroups = new ArrayList<>();
	}

	public boolean hasSelectedGroups() {
		return this.selectedGroups != null && !this.selectedGroups.isEmpty();
	}

	public String getInactivateButtonMessage() {
		if (hasSelectedGroups()) {
			int size = this.selectedGroups.size();
			return size > 1 ? "inactivar " + size + " grupos" : "inactivar grupo";
		}
		return "Inactivar";
	}

	public void inactivateSelectedGroup() {
		this.selectedGroups = Arrays.asList(new Group[] { selectedGroup });
		inactivateSelectedGroups();
	}

	public void activateGroup(Group group) {
		try {
			Group updatedGroup = groupManager.updateOneGroupById(group.getId(), null, true);
			JSFMessages.INFO("Grupo activado de forma exitosa.");
			this.groups.removeIf(t -> t.getId() == updatedGroup.getId());
			this.groups.add(0, updatedGroup);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());

		}
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Group> getSelectedGroups() {
		return selectedGroups;
	}

	public void setSelectedGroups(List<Group> selectedGroups) {
		this.selectedGroups = selectedGroups;
	}

	public Group getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(Group selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

	public List<Integer> getSelectedPermissionIds() {
		return selectedPermissionIds;
	}

	public void setSelectedPermissionIds(List<Integer> selectedPermissionIds) {
		this.selectedPermissionIds = selectedPermissionIds;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public List<Permission> getPermissionsExceptGroupManagementPermission() {
		return permissionsExceptGroupManagementPermission;
	}

	public void setPermissionsExceptGroupManagementPermission(
			List<Permission> permissionsExceptGroupManagementPermission) {
		this.permissionsExceptGroupManagementPermission = permissionsExceptGroupManagementPermission;
	}

}
