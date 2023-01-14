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
import swimsEJB.model.auth.entities.Permission;
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
	@EJB
	private PermissionManager permissionManager;

	/**
	 * Default constructor.
	 */
	public GroupManager() {
		// TODO Auto-generated constructor stub
	}

	public Group createOneGroup(String name, boolean isRoot) throws Exception {
		Group group = new Group();
		group.setName(name);
		group.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		group.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		group.setIsActive(true);
		group.setIsRoot(isRoot);
		try {
			return (Group) daoManager.createOne(group);
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Ha ocurrido un error en la creación del Grupo.");
		}
	}

	public List<GroupPermission> updateGroupPermissions(int groupId, List<Integer> permissionIds) throws Exception {
		List<GroupPermission> foundGroupsPermissions = groupPermissionManager.findAllGroupPermissionsByGroupId(groupId);

		List<Integer> toAddPermissionIds = new ArrayList<>(permissionIds);
		toAddPermissionIds.removeIf(arg0 -> foundGroupsPermissions.stream().map(arg1 -> arg1.getPermission().getId())
				.collect(Collectors.toList()).contains(arg0));

		List<GroupPermission> toModifyGroupPermissions = new ArrayList<>(foundGroupsPermissions);
		toModifyGroupPermissions.removeIf(arg0 -> permissionIds.contains(arg0.getPermission().getId()));

		int difference = toAddPermissionIds.size() - toModifyGroupPermissions.size();

		if (difference > 0) {
			for (int i = 0; i < toModifyGroupPermissions.size(); i++) {
				groupPermissionManager.updateOneGroupPermissionById(toModifyGroupPermissions.get(i).getId(), groupId,
						toAddPermissionIds.get(i));
			}
			while (difference != 0) {
				groupPermissionManager.createOneGroupPermission(groupId,
						toAddPermissionIds.get(toModifyGroupPermissions.size() - 1 + difference));
				difference--;
			}
			return groupPermissionManager.findAllGroupPermissionsByGroupId(groupId);
		}
		if (difference < 0) {
			for (int i = 0; i < toAddPermissionIds.size(); i++) {
				groupPermissionManager.updateOneGroupPermissionById(toModifyGroupPermissions.get(i).getId(), groupId,
						toAddPermissionIds.get(i));
			}
			while (difference != 0) {
				groupPermissionManager.deleteOneGroupPermissionById(
						toModifyGroupPermissions.get(toModifyGroupPermissions.size() - (difference * -1)).getId());
				difference++;
			}
			return groupPermissionManager.findAllGroupPermissionsByGroupId(groupId);
		}

		for (int i = 0; i < toModifyGroupPermissions.size(); i++) {
			groupPermissionManager.updateOneGroupPermissionById(toModifyGroupPermissions.get(i).getId(), groupId,
					toAddPermissionIds.get(i));
		}
		return groupPermissionManager.findAllGroupPermissionsByGroupId(groupId);
	}

	public Group createOneGroup(String name) throws Exception {
		return createOneGroup(name, false);
	}

	public Group createOneGroupWithPermissionIds(String name, List<Integer> permissionIds) throws Exception {
		Group createdGroup = createOneGroup(name);
		for (Integer integer : permissionIds) {
			this.addPermissionById(createdGroup.getId(), integer);
		}
		return createdGroup;
	}

	@SuppressWarnings("unchecked")
	private List<Group> findAllGroups() {
		return daoManager.findAll(Group.class, "updatedAt", false);
	}

	public List<Group> findAllGroups(boolean isRoot) {
		List<Group> foundGroups = findAllGroups();
		if (isRoot) {
			return foundGroups;
		}
		foundGroups.removeIf(arg0 -> arg0.getIsRoot());
		return foundGroups;
	}

	public List<Group> findAllActiveGroups() {
		List<Group> foundGroups = findAllGroups();
		foundGroups.removeIf(arg0 -> !arg0.getIsActive());
		return foundGroups;
	}

	public List<Group> findAllActiveGroups(boolean userIsRoot) {
		List<Group> foundGroups = findAllActiveGroups();
		if (userIsRoot)
			return foundGroups;

		foundGroups.removeIf(arg0 -> arg0.getIsRoot());
		return foundGroups;
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

		if (name != null)
			group.setName(name);
		group.setIsActive(isActive);
		group.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

		try

		{
			return (Group) daoManager.updateOne(group);
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Ha ocurrido un error en la actualización del Grupo.");
		}
	}

	public Group updateOneGroupById(int id, String name, Boolean isActive, List<Integer> permissionIds)
			throws Exception {
		updateGroupPermissions(id, permissionIds);
		System.out.println(1);
		return updateOneGroupById(id, name, isActive);
	}

	public Group addPermissionById(int groupId, int permissionId) throws Exception {
		GroupPermission groupPermission = groupPermissionManager.createOneGroupPermission(groupId, permissionId);
		return groupPermission.getGroup();
	}

	public Group addUserById(int groupId, int userId) throws Exception {
		UserGroup userGroup = userGroupManager.createOneUserGroup(userId, groupId);
		return userGroup.getGroup();
	}

	public List<Integer> findRootGroupUserIds() {
		Group rootGroup = (Group) daoManager.findOneWhere(Group.class, "o.isRoot = true");
		if (rootGroup == null) {
			return new ArrayList<>();
		}
		List<Integer> rootGroupUserIds = rootGroup.getUsersGroups().stream().map(arg0 -> arg0.getUser())
				.collect(Collectors.toList()).stream().map(arg0 -> arg0.getId()).collect(Collectors.toList());
		return rootGroupUserIds;
	}

	public List<Integer> findAllGroupIdsByUserId(int userId) {
		List<UserGroup> userGroups = userGroupManager.findAllUserGroupsByUserId(userId);
		return userGroups.stream().map(arg0 -> arg0.getGroup().getId()).collect(Collectors.toList());
	}

	public List<Integer> findAllGroupIdsByRelatedWebappPath(String webAppPath) throws Exception {
		Permission foundPermission = permissionManager.findOnePermissionByRelatedWebappPath(webAppPath);
		if (foundPermission == null)
			throw new Exception("La Ruta Web especificada no está registrada.");

		List<GroupPermission> groupPermissions = groupPermissionManager
				.findGroupPermissionsByPermissionId(foundPermission.getId());
		return groupPermissions.stream().map(arg0 -> arg0.getGroup().getId()).collect(Collectors.toList());
	}
}
