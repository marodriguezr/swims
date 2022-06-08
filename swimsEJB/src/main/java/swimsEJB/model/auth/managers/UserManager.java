package swimsEJB.model.auth.managers;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.mindrot.jbcrypt.BCrypt;

import swimsEJB.model.auth.dtos.UserDto;
import swimsEJB.model.auth.entities.User;
import swimsEJB.model.core.managers.DaoManager;

/**
 * Session Bean implementation class UserManager
 */
@Stateless
@LocalBean
public class UserManager {

	@EJB
	private DaoManager daoManager;

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
		userDto.setSuperUser(user.getIsSuperUser());
		return userDto;
	}
	
	public UserDto createOneUser(String firstName, String lastName, String email, String password)
			throws Exception {
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(20)));
		user.setIsSuperUser(false);
		user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		user.setIsActive(true);

		try {
			return UserToUserDto(((User) daoManager.createOne(user)));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception("Ha ocurrido un error en la creación de usuario.");
		}
	}

	public UserDto signIn(String email, String password) throws Exception {
		User user = (User) daoManager.findOneWhere(User.class, String.format("o.email = %s", email));
		if (user == null)
			throw new Exception("Acceso no permitido.");
		if (!BCrypt.checkpw(password, user.getPassword()))
			return null;
		return UserToUserDto(user);
	}
}
