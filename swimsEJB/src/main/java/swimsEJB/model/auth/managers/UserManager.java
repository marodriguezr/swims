package swimsEJB.model.auth.managers;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.HashSet;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.mindrot.jbcrypt.BCrypt;

import swimsEJB.model.auth.dtos.UserDto;
import swimsEJB.model.auth.entities.Group;
import swimsEJB.model.auth.entities.User;
import swimsEJB.model.auth.entities.UserGroup;
import swimsEJB.model.core.managers.DaoManager;

/**
 * Session Bean implementation class UserManager
 */
@Stateless
@LocalBean
public class UserManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private UserGroupManager userGroupManager;
	@EJB
	private GroupManager groupManager;
	@EJB
	private PermissionManager permissionManager;

	/**
	 * Default constructor.
	 */
	public UserManager() {
		// TODO Auto-generated constructor stub
	}

	public UserDto UserToUserDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setActive(user.getIsActive());
		userDto.setEmail(user.getEmail());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setRoot(user.getIsRoot());
		return userDto;
	}

	public User userDtoToUser(UserDto userDto) {
		User user = new User();
		user.setId(userDto.getId());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		return user;
	}

	public List<UserDto> UsersToUserDtos(List<User> users) {
		List<UserDto> userDtos = new ArrayList<>();
		for (User user : users) {
			userDtos.add(UserToUserDto(user));
		}
		return userDtos;
	}

	public UserDto createOneUser(String firstName, String lastName, String email, String password, boolean isRoot)
			throws Exception {
		User user = new User();

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(10)));
		user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		user.setIsActive(true);
		user.setIsRoot(isRoot);

		try {
			return UserToUserDto(((User) daoManager.createOne(user)));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception("Ha ocurrido un error en la creación del Usuario.");
		}
	}

	public UserDto createOneUser(String firstName, String lastName, String email, String password) throws Exception {
		return createOneUser(firstName, lastName, email, password, false);
	}

	public UserDto createOneUserWithGroupIds(String firstName, String lastName, String email, String password,
			List<Integer> groupIds) throws Exception {
		UserDto createdUserDto = createOneUser(firstName, lastName, email, password);
		for (Integer integer : groupIds) {
			addGroupById(createdUserDto.getId(), integer);
		}
		return createdUserDto;
	}

	public List<UserDto> findAllUsersInAdminGroup() {
		Group rootGroup = (Group) daoManager.findOneWhere(Group.class, "o.isRoot = true");
		if (rootGroup == null)
			return new ArrayList<>();
		List<UserDto> foundUserDtos = new ArrayList<>();

		UserDto foundUserDto;
		for (UserGroup userGroup : rootGroup.getUsersGroups()) {
			try {
				foundUserDto = this.findOneUserById(userGroup.getUser().getId());
			} catch (Exception e) {
				// TODO: handle exception
				continue;
			}
			foundUserDtos.add(foundUserDto);
		}

		return foundUserDtos;
	}

	@SuppressWarnings("unchecked")
	public List<UserDto> findAllUsers() {
		List<UserDto> foundUserDtos = UsersToUserDtos(daoManager.findAll(User.class));
		return foundUserDtos;
	}

	public List<UserDto> findAllUsers(boolean isRoot, int userId) {
		List<UserDto> foundUserDtos = findAllUsers();
		foundUserDtos.removeIf(arg0 -> arg0.getId() == userId);
		if (isRoot)
			return foundUserDtos;

		List<Integer> rootGroupUserIds = groupManager.findRootGroupUserIds();
		foundUserDtos.removeIf(arg0 -> arg0.isRoot());
		foundUserDtos.removeIf(arg0 -> rootGroupUserIds.contains(arg0.getId()));
		return foundUserDtos;
	}

	public UserDto findOneUserById(int id) throws Exception {
		User user = (User) daoManager.findOneById(User.class, id);
		if (user == null)
			return null;
		return UserToUserDto(user);
	}

	public List<UserGroup> updateUserGroups(int userId, List<Integer> groupIds) throws Exception {
		List<UserGroup> foundUserGroups = userGroupManager.findAllUserGroupsByUserId(userId);

		List<Integer> toAddGroupIds = new ArrayList<>(groupIds);
		toAddGroupIds.removeIf(arg0 -> foundUserGroups.stream().map(arg1 -> arg1.getGroup().getId())
				.collect(Collectors.toList()).contains(arg0));

		List<UserGroup> toModifyUserGroups = new ArrayList<>(foundUserGroups);
		toModifyUserGroups.removeIf(arg0 -> groupIds.contains(arg0.getGroup().getId()));

		int difference = toAddGroupIds.size() - toModifyUserGroups.size();

		if (difference > 0) {
			for (int i = 0; i < toModifyUserGroups.size(); i++) {
				userGroupManager.updateOneUserGroup(toModifyUserGroups.get(i).getId(), userId, toAddGroupIds.get(i));
			}
			while (difference != 0) {
				userGroupManager.createOneUserGroup(userId,
						toAddGroupIds.get(toModifyUserGroups.size() - 1 + difference));
				difference--;
			}
			return userGroupManager.findAllUserGroupsByUserId(userId);
		}
		if (difference < 0) {
			for (int i = 0; i < toAddGroupIds.size(); i++) {
				userGroupManager.updateOneUserGroup(toModifyUserGroups.get(i).getId(), userId, toAddGroupIds.get(i));
			}
			while (difference != 0) {
				userGroupManager.deleteOseUserGroupById(
						toModifyUserGroups.get(toModifyUserGroups.size() - (difference * -1)).getId());
				difference++;
			}
			return userGroupManager.findAllUserGroupsByUserId(userId);
		}

		for (int i = 0; i < toModifyUserGroups.size(); i++) {
			userGroupManager.updateOneUserGroup(toModifyUserGroups.get(i).getId(), userId, toAddGroupIds.get(i));
		}
		return userGroupManager.findAllUserGroupsByUserId(userId);
	}

	public UserDto updateOneUserById(int id, String firstName, String lastName, Boolean isActive, String password)
			throws Exception {
		User user = (User) daoManager.findOneById(User.class, id);
		if (user == null)
			throw new Exception("El Usuario específicado no está registrado.");
		user.setFirstName(firstName);
		user.setLastName(lastName);

		if (!password.isEmpty())
			user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(10)));
		user.setIsActive(isActive);
		try {
			return UserToUserDto((User) daoManager.updateOne(user));
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Ha ocurrido un error en la actualización del Usuario.");
		}
	}

	public UserDto updateOneUserById(int id, String firstName, String lastName, Boolean isActive, String password,
			List<Integer> groupIds) throws Exception {
		updateUserGroups(id, groupIds);
		return updateOneUserById(id, firstName, lastName, isActive, password);
	}

	public UserDto updateOneUserById(int id, String firstName, String lastName, Boolean isActive) throws Exception {
		return updateOneUserById(id, firstName, lastName, isActive, "");
	}

	public UserDto deleteOneUserById(int id) throws Exception {
		UserDto foundUserDto = findOneUserById(id);
		if (foundUserDto == null)
			throw new Exception("El usuario especificado no está registrado.");
		List<UserGroup> userGroups = userGroupManager.findAllUserGroupsByUserId(id);
		for (UserGroup userGroup : userGroups) {
			userGroupManager.deleteOseUserGroupById(userGroup.getId());
		}
		daoManager.deleteOneById(User.class, foundUserDto.getId());
		return foundUserDto;
	}

	public UserDto signIn(String email, String password) throws Exception {
		User user = (User) daoManager.findOneWhere(User.class, String.format("o.email = '%s'", email));
		if (user == null)
			throw new Exception("Acceso no permitido.");
		if (!user.getIsActive())
			throw new Exception("Acceso no permitido.");
		if (!BCrypt.checkpw(password, user.getPassword()))
			throw new Exception("Acceso no permitido.");
		return UserToUserDto(user);
	}

	public UserDto addGroupById(int userId, int groupId) throws Exception {
		UserGroup userGroup = userGroupManager.createOneUserGroup(userId, groupId);
		return UserToUserDto(userGroup.getUser());
	}

	public List<String> findAllAccesibleWebappPathsByUserId(int id) throws Exception {
		User user = (User) daoManager.findOneById(User.class, id);
		if (user == null)
			throw new Exception("El Usuario no está registrado.");
		if (!user.getIsActive())
			throw new Exception("El Usuario se encuentra inactivo.");
		List<Group> groups = groupManager.findAllActiveGroupsByUserId(user.getId());
		List<String> accessibleWebAppPaths = new ArrayList<>();
		for (Group group : groups) {
			accessibleWebAppPaths = Stream
					.concat(accessibleWebAppPaths.stream(),
							permissionManager.findAllActiveWebappRelatedPathsByGroupId(group.getId()).stream())
					.collect(Collectors.toList());
		}
		return new ArrayList<>(new HashSet<>(accessibleWebAppPaths));
	}

	/**
	 * The following method validates that all the required web app paths are
	 * present in the accessible web app paths.
	 * 
	 * @param accessibleWebappPaths
	 * @param requiredWebappPaths
	 * @return
	 * @throws Exception
	 */
	public boolean verifyAuthorizationByAllWebappPaths(int userId, List<String> requiredWebappPaths) throws Exception {
		List<String> accessibleWebappPaths = findAllAccesibleWebappPathsByUserId(userId);
		for (String requiredWebappPath : requiredWebappPaths) {
			if (!accessibleWebappPaths.stream().anyMatch(requiredWebappPath::contains))
				return false;
		}
		return true;
	}

	/**
	 * The following method validates that at least one of the required web app
	 * paths is present in the accessible web app paths.
	 * 
	 * @param accessibleWebappPaths
	 * @param requiredWebappPaths
	 * @return
	 * @throws Exception
	 */
	public boolean verifyAuthorizationByOneWebappPath(int userId, List<String> requiredWebappPaths) throws Exception {
		List<String> accessibleWebappPaths = findAllAccesibleWebappPathsByUserId(userId);
		for (String requiredWebappPath : requiredWebappPaths) {
			if (accessibleWebappPaths.stream().anyMatch(requiredWebappPath::contains))
				return true;
		}
		return false;
	}

	public UserDto updateSelfPassword(UserDto signedUser, String currentPassword, String newPassword) throws Exception {
		if (signedUser == null)
			throw new Exception("Acceso no permitido.");
		signIn(signedUser.getEmail(), currentPassword);

		User user = (User) daoManager.findOneById(User.class, signedUser.getId());
		if (user == null)
			throw new Exception("El Usuario específicado no está registrado.");
		if (!newPassword.isEmpty())
			user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(10)));
		try {
			return UserToUserDto((User) daoManager.updateOne(user));
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Ha ocurrido un error en la actualización del Usuario.");
		}
	}

	public List<Integer> findAllUserIdsByWebappRelatedPath(String... webappRelatedPaths) throws Exception {
		List<Integer> groupIds = new ArrayList<>();
		if (webappRelatedPaths.length == 1) {
			groupIds = groupManager.findAllGroupIdsByRelatedWebappPath(webappRelatedPaths[0]);
		}
		if (webappRelatedPaths.length > 1) {
			for (String webappRelatedPath : webappRelatedPaths) {
				groupIds = Stream
						.concat(groupIds.stream(),
								groupManager.findAllGroupIdsByRelatedWebappPath(webappRelatedPath).stream())
						.collect(Collectors.toList());
			}
			groupIds = new ArrayList<>(new HashSet<>(groupIds));
		}
		List<UserGroup> userGroups = new ArrayList<>();
		for (int groupId : groupIds) {
			userGroups = Stream
					.concat(userGroups.stream(), userGroupManager.findAllUserGroupsByGroupId(groupId).stream())
					.collect(Collectors.toList());
		}

		return new ArrayList<>(
				new HashSet<>(userGroups.stream().map(arg0 -> arg0.getUser().getId()).collect(Collectors.toList())));
	}

	public List<UserDto> findAllUserDtosByWebappRelatedPath(String... webappRelatedPaths) throws Exception {
		List<Integer> userIds = findAllUserIdsByWebappRelatedPath(webappRelatedPaths);
		List<UserDto> userDtos = new ArrayList<>();
		for (Integer userId : userIds) {
			userDtos.add(findOneUserById(userId));
		}
		return userDtos;
	}
}
