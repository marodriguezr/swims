package swimsEJB.model.auth.managers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

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
			permissions2.add(createOnePermission(permission.getName(),permission.getWebappRelatedPath()));
		}
		return permissions2;
	}
	
	@SuppressWarnings("unchecked")
	public List<Permission> findAllPemiPermissions() {
		return daoManager.findAll(Permission.class);
	}

	public Permission findOnePermissionById(int id) throws Exception {
		return (Permission) daoManager.findOneById(Permission.class, id);
	}
	
	public Permission updateOnePermissionById(int id, String name, String webAppRelatedPath, Boolean isActive) throws Exception {
		Permission permission = findOnePermissionById(id);
		if (permission == null) throw new Exception("El Permiso especificado no existe.");
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
}
