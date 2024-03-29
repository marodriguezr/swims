package swimsEJB.model.auth.managers;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.auth.entities.Group;
import swimsEJB.model.auth.entities.User;
import swimsEJB.model.auth.entities.UserGroup;
import swimsEJB.model.core.managers.DaoManager;

/**
 * Session Bean implementation class UserGroupManager
 */
@Stateless
@LocalBean
public class UserGroupManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private UserManager userManager;
	@EJB
	private GroupManager groupManager;

	/**
	 * Default constructor.
	 */
	public UserGroupManager() {
		// TODO Auto-generated constructor stub
	}

	public UserGroup createOneUserGroup(int userId, int groupId) throws Exception {
		User user = (User) daoManager.findOneById(User.class, userId);
		if (user == null)
			throw new Exception("El usuario especificado no está registrado.");
		Group group = groupManager.findOneGroupById(groupId);
		if (group == null)
			throw new Exception("El grupo especificado no está registrado.");

		UserGroup userGroup = new UserGroup();
		userGroup.setUser(user);
		userGroup.setGroup(group);
		try {
			return (UserGroup) daoManager.createOne(userGroup);
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Ha ocurrido un error en la creación del UserGroup.");
		}
	}

	@SuppressWarnings("unchecked")
	public List<UserGroup> findAllUserGroupsByUserId(int userId) {
		List<UserGroup> userGroups = daoManager.findManyWhere(UserGroup.class, "o.user.id = " + userId, "");
		return userGroups;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroup> findAllUserGroupsByGroupId(int groupId) {
		List<UserGroup> userGroups = daoManager.findManyWhere(UserGroup.class, "o.group.id = " + groupId, "");
		return userGroups;
	}

	public UserGroup updateOneUserGroup(int userGroupId, int userId, int groupId) throws Exception {
		UserGroup userGroup = (UserGroup) daoManager.findOneById(UserGroup.class, userGroupId);
		if (userGroup == null)
			throw new Exception("El UserGroup especificado no está registrado.");
		User user = (User) daoManager.findOneById(User.class, userId);
		if (user == null)
			throw new Exception("El usuario especificado no está registrado.");
		Group group = groupManager.findOneGroupById(groupId);
		if (group == null)
			throw new Exception("El grupo especificado no está registrado.");

		userGroup.setGroup(group);
		userGroup.setUser(user);

		UserGroup updatedUserGroup = (UserGroup) daoManager.updateOne(userGroup);

		return updatedUserGroup;
	}

	public UserGroup deleteOseUserGroupById(int userGroupId) throws Exception {
		UserGroup userGroup = (UserGroup) daoManager.findOneById(UserGroup.class, userGroupId);
		if (userGroup == null)
			throw new Exception("El UserGroup especificado no está registrado.");
		daoManager.deleteOneById(UserGroup.class, userGroup.getId());
		return userGroup;
	}

}
