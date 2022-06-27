package swimsEJB.model.auth.managers;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.auth.entities.Group;
import swimsEJB.model.auth.entities.GroupPermission;
import swimsEJB.model.auth.entities.Permission;
import swimsEJB.model.core.managers.DaoManager;

/**
 * Session Bean implementation class GroupPermissionManager
 */
@Stateless
@LocalBean
public class GroupPermissionManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private GroupManager groupManager;
	@EJB
	private PermissionManager permissionManager;

	/**
	 * Default constructor.
	 */
	public GroupPermissionManager() {
		// TODO Auto-generated constructor stub
	}

	public GroupPermission createOneGroupPermission(int groupId, int permissionId) throws Exception {
		Group group = groupManager.findOneGroupById(groupId);
		if (group == null)
			throw new Exception("El Grupo especificado no está registrado.");
		Permission permission = permissionManager.findOnePermissionById(permissionId);
		if (permission == null)
			throw new Exception("El Permiso especificado no está registrado.");
		GroupPermission groupPermission = new GroupPermission();
		groupPermission.setGroup(group);
		groupPermission.setPermission(permission);
		try {
			groupPermission = (GroupPermission) daoManager.createOne(groupPermission);
			return groupPermission;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Ha ocurrido un error en la creación del Grupo-Permiso.");
		}
	}

	@SuppressWarnings("unchecked")
	public List<GroupPermission> findAllGroupPermissionsByGroupId(int groupId) {
		List<GroupPermission> groupPermissions = daoManager.findManyWhere(GroupPermission.class,
				"o.group.id = " + groupId, null);
		return groupPermissions;
	}
}
