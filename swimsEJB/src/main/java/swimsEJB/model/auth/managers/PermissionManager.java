package swimsEJB.model.auth.managers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.auth.entities.GroupPermission;
import swimsEJB.model.auth.entities.Permission;
import swimsEJB.model.core.managers.DaoManager;

/**
 * Session Bean implementation class PermissionManager
 */
@Stateless
@LocalBean
public class PermissionManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private GroupPermissionManager groupPermissionManager;

	/**
	 * Default constructor.
	 */
	public PermissionManager() {
		// TODO Auto-generated constructor stub
	}

	public Permission createOnePermission(String name, String webAppRelatedPath) throws Exception {
		Permission permission = new Permission();
		permission.setName(name);
		permission.setWebappRelatedPath(webAppRelatedPath);
		permission.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		permission.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		permission.setIsActive(true);

		try {
			return (Permission) daoManager.createOne(permission);
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Ha ocurrido un error en la creación del Permiso.");
		}
	}

	public List<Permission> createManyPermissions(List<Permission> permissions) throws Exception {
		List<Permission> permissions2 = new ArrayList<>();
		for (Permission permission : permissions) {
			permissions2.add(createOnePermission(permission.getName(), permission.getWebappRelatedPath()));
		}
		return permissions2;
	}

	@SuppressWarnings("unchecked")
	public List<Permission> findAllPermissions() {
		return daoManager.findAll(Permission.class, "updatedAt", false);
	}

	public Permission findOnePermissionById(int id) throws Exception {
		return (Permission) daoManager.findOneById(Permission.class, id);
	}

	public Permission updateOnePermissionById(int id, String name, String webAppRelatedPath, Boolean isActive)
			throws Exception {
		Permission permission = findOnePermissionById(id);
		if (permission == null)
			throw new Exception("El Permiso especificado no existe.");
		permission.setName(name);
		permission.setWebappRelatedPath(webAppRelatedPath);
		permission.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		permission.setIsActive(isActive);
		try {
			return (Permission) daoManager.updateOne(permission);
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Ha ocurrido un error en la actualización del Permiso.");
		}
	}

	public Permission deleteOnePermissionById(int permissionId) throws Exception {
		try {
			Permission deletedPermission = (Permission) daoManager.deleteOneById(Permission.class, permissionId);
			return deletedPermission;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Ha ocurrido un error en el proceso de eliminado del Permiso.");
		}
	}

	public List<Permission> findAllPermissionsByGroupId(int groupId) {
		List<GroupPermission> groupPermissions = groupPermissionManager.findAllGroupPermissionsByGroupId(groupId);
		List<Permission> permissions = new ArrayList<>();
		for (GroupPermission groupPermission : groupPermissions) {
			permissions.add(groupPermission.getPermission());
		}
		return permissions;
	};

	public List<Permission> findAllActivePermissionsByGroupId(int groupId) {
		List<Permission> permissions = findAllPermissionsByGroupId(groupId);
		permissions = permissions.stream().filter(permission -> permission.getIsActive()).collect(Collectors.toList());
		return permissions;
	};

	public List<String> findAllWebappRelatedPathsByGroupId(int groupId) {
		List<Permission> permissions = findAllPermissionsByGroupId(groupId);
		List<String> webappRelatedPaths = new ArrayList<>();
		for (Permission permission : permissions) {
			webappRelatedPaths.add(permission.getWebappRelatedPath());
		}
		return webappRelatedPaths;
	}

	public List<String> findAllActiveWebappRelatedPathsByGroupId(int groupId) {
		List<Permission> permissions = findAllActivePermissionsByGroupId(groupId);
		List<String> webappRelatedPaths = new ArrayList<>();
		for (Permission permission : permissions) {
			webappRelatedPaths.add(permission.getWebappRelatedPath());
		}
		return webappRelatedPaths;
	}

	public Permission findOnePermissionByRelatedWebappPath(String webAppPath) {
		Permission permission = (Permission) daoManager.findOneWhere(Permission.class,
				"o.webappRelatedPath = '" + webAppPath + "'");
		return permission;
	}
}
