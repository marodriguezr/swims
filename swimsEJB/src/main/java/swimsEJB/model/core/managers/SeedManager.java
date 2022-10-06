package swimsEJB.model.core.managers;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.auth.dtos.UserDto;
import swimsEJB.model.auth.entities.Group;
import swimsEJB.model.auth.entities.Permission;
import swimsEJB.model.auth.managers.GroupManager;
import swimsEJB.model.auth.managers.PermissionManager;
import swimsEJB.model.auth.managers.UserManager;
import swimsEJB.model.core.entities.Sysparam;

import static swimsEJB.constants.WebappPaths.HARVESTING_OAI_RECORDS_INCLUSION_WEBAPP_PATH;
import static swimsEJB.constants.WebappPaths.HARVESTING_OAI_SETS_MANAGEMENT_WEBAPP_PATH;

/**
 * Session Bean implementation class CoreManager
 */
@Stateless
@LocalBean
public class SeedManager {

	@EJB
	private UserManager userManager;
	@EJB
	private PermissionManager permissionManager;
	@EJB
	private GroupManager groupManager;
	@EJB
	private SysparamManager sysparamManager;

	/**
	 * Default constructor.
	 */
	public SeedManager() {
		// TODO Auto-generated constructor stub
	}

	public boolean isSystemSeeded() throws Exception {
		Sysparam sysparam = sysparamManager.findOneByKey("IS_SYSTEM_SEEDED");
		if (sysparam == null)
			return false;
		return sysparam.getValue().equals("true");
	}

	public void seed(String firstName, String lastName, String email, String password) throws Exception {
		Sysparam sysparam = sysparamManager.findOneByKey("IS_SYSTEM_SEEDED");
		if (sysparam != null)
			if (sysparam.getValue() == "true")
				throw new Exception("System already seeded.");
		if (!userManager.findAllUsers().isEmpty())
			throw new Exception("System already seeded.");

		/**
		 * Permissions
		 */
		Permission oaiRecordsInclusionPermission = permissionManager.createOnePermission("Inclusión de Registros OAI",
				HARVESTING_OAI_RECORDS_INCLUSION_WEBAPP_PATH);
		Permission oaiSetsManagementPermission = permissionManager.createOnePermission("Administración de Sets OAI",
				HARVESTING_OAI_SETS_MANAGEMENT_WEBAPP_PATH);
		
		/**
		 * Groups
		 */
		Group adminGroup = groupManager.createOneGroup("Administrador");
		Group oaiRecordsInclusionGroup = groupManager.createOneGroup("Inclusores de Registros OAI");

		/**
		 * GroupPermissions
		 */
		groupManager.addPermissionById(adminGroup.getId(), oaiSetsManagementPermission.getId());
		
		groupManager.addPermissionById(oaiRecordsInclusionGroup.getId(), oaiRecordsInclusionPermission.getId());

		/**
		 * Users
		 */
		UserDto adminUser = userManager.createOneUser(firstName, lastName, email, password);
		
		UserDto user2 = userManager.createOneUser("Inclusor", "Registros", "mail@mail2.com", "admin");
		
		/**
		 * UserGroups
		 */
		userManager.addGroupById(adminUser.getId(), adminGroup.getId());
		
		userManager.addGroupById(user2.getId(), oaiRecordsInclusionGroup.getId());

		/**
		 * 
		 */
		sysparamManager.createOneSysparam("IS_SYSTEM_SEEDED", "true");
	}

}