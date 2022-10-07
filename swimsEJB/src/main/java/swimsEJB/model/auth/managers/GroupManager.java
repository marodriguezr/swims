package swimsEJB.model.auth.managers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.auth.entities.Group;
import swimsEJB.model.auth.entities.GroupPermission;
import swimsEJB.model.auth.entities.UserGroup;
import swimsEJB.model.core.managers.DaoManager;

/**
 * Session Bean implementation class GroupManager
 */
@Stateless
@LocalBean
public class GroupManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private GroupPermissionManager groupPermissionManager;
	@EJB
	private UserGroupManager userGroupManager;

	/**
	 * Default constructor.
	 */
	public GroupManager() {
		// TODO Auto-generated constructor stub
	}

	public Group createOneGroup(String name) throws Exception {
		Group group = new Group();
		group.setName(name);
		group.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		group.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		group.setIsActive(true);
		try {
			return (Group) daoManager.createOne(group);
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Ha ocurrido un error en la creación del Grupo.");
		}
	}

	@SuppressWarnings("unchecked")
	public List<Group> findAllGroups() {
		return daoManager.findAll(Group.class);
	}

	public List<Group> findAllGroupsByUserId(int userId) {
		List<UserGroup> userGroups = userGroupManager.findAllUserGroupsByUserId(userId);
		List<Group> groups = new ArrayList<>();		
		for (UserGroup userGroup : userGroups) {
			groups.add(userGroup.getGroup());
		}
		return groups;
	};
	
	public List<Group> findAllActiveGroupsByUserId(int userId) {
		List<Group> groups = findAllGroupsByUserId(userId);		
		groups = groups.stream().filter(group -> group.getIsActive()).collect(Collectors.toList());
		return groups;
	}

	public Group findOneGroupById(int id) throws Exception {
		return (Group) daoManager.findOneById(Group.class, id);
	}

	public Group updateOneGroupById(int id, String name, Boolean isActive) throws Exception {
		Group group = findOneGroupById(id);
		if (group == null)
			throw new Exception("El Grupo especificado no existe.");

		group.setName(name);
		group.setIsActive(isActive);

		try {
			return (Group) daoManager.updateOne(group);
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Ha ocurrido un error en la actualización del Grupo.");
		}
	}

	public Group addPermissionById(int groupId, int permissionId) throws Exception {
		GroupPermission groupPermission = groupPermissionManager.createOneGroupPermission(groupId, permissionId);
		return groupPermission.getGroup();
	}
	
	public Group addUserById(int groupId, int userId) throws Exception {
		UserGroup userGroup = userGroupManager.createOneUserGroup(userId, groupId);
		return userGroup.getGroup();
	}
}
