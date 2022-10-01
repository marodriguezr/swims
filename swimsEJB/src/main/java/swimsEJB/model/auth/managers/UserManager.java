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

	public UserDto createOneUser(String firstName, String lastName, String email, String password) throws Exception {
		User user = new User();

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(10)));
		user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		user.setIsActive(true);

		try {
			return UserToUserDto(((User) daoManager.createOne(user)));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception("Ha ocurrido un error en la creación del Usuario.");
		}
	}

	@SuppressWarnings("unchecked")
	public List<UserDto> findAllUsers() {
		return UsersToUserDtos(daoManager.findAll(User.class));
	}

	public UserDto findOneUserById(String id) throws Exception {
		User user = (User) daoManager.findOneById(User.class, id);
		if (user == null)
			return null;
		return UserToUserDto(user);
	}

	public UserDto updateOneUserById(String id, String firstName, String lastName, String email, String password,
			Boolean isActive) throws Exception {
		User user = (User) daoManager.findOneById(User.class, id);
		if (user == null)
			throw new Exception("El Usuario específicado no está registrado.");
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(10)));
		user.setIsActive(isActive);
		try {
			return UserToUserDto((User) daoManager.updateOne(user));
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Ha ocurrido un error en la actualización del Usuario.");
		}
	}

	public UserDto updateSelfUserById(String id, String firstName, String lastName, String email, String password)
			throws Exception {
		UserDto userDto = signIn(email, password);
		return updateOneUserById(id, firstName, lastName, email, password, userDto.isActive());
	}

	public UserDto signIn(String email, String password) throws Exception {
		User user = (User) daoManager.findOneWhere(User.class, String.format("o.email = '%s'", email));
		if (user == null)
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
			throw new Exception("El Usuario especificado no está registrado.");
		List<Group> groups = groupManager.findAllGroupsByUserId(user.getId());
		List<String> accessibleWebAppPaths = new ArrayList<>();
		for (Group group : groups) {
			accessibleWebAppPaths = Stream
					.concat(accessibleWebAppPaths.stream(),
							permissionManager.findAllWebappRelatedPathsByGroupId(group.getId()).stream())
					.collect(Collectors.toList());
		}
		return new ArrayList<>(new HashSet<>(accessibleWebAppPaths));
	}
	
	public boolean verifyAuthorizationByWebappPaths(List<String> accessibleWebappPaths, List<String> requiredWebappPaths) {
		for (String requiredWebappPath : requiredWebappPaths) {
			if (accessibleWebappPaths.stream().anyMatch(requiredWebappPath::contains)) return true;
		}
		return false;
	}
	
	public boolean verifyAuthorizationByUserId(int userId, List<String> requiredWebappPaths) throws Exception {
		List<String> accessibleWebappPaths = findAllAccesibleWebappPathsByUserId(userId);
		return verifyAuthorizationByWebappPaths(accessibleWebappPaths, requiredWebappPaths);
	}
}
