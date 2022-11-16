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
import swimsEJB.model.harvesting.managers.OaiSetManager;

import static swimsEJB.constants.WebappPaths.*;

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
	@EJB
	private OaiSetManager oaiSetManager;

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
		/**
		 * HARVESTING
		 */
		Permission oaiRecordsInclusionPermission = permissionManager.createOnePermission("Inclusión de Registros OAI",
				HARVESTING_OAI_RECORDS_INCLUSION_WEBAPP_PATH);
		Permission oaiSetsManagementPermission = permissionManager.createOnePermission("Administración de Sets OAI",
				HARVESTING_OAI_SETS_MANAGEMENT_WEBAPP_PATH);

		/**
		 * AUTH
		 */
		Permission permissionManagementPermission = permissionManager.createOnePermission("Administración de Permisos",
				AUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH);
		Permission userManagementPermission = permissionManager.createOnePermission("Administración de Usuario", AUTH_USER_MANAGEMENT_WEBAPP_PATH);
		

		/**
		 * Groups
		 */
		Group adminGroup = groupManager.createOneGroup("Administrador", true);
		Group oaiRecordsInclusionGroup = groupManager.createOneGroup("Inclusores de Registros OAI");

		/**
		 * GroupPermissions
		 */
		groupManager.addPermissionById(adminGroup.getId(), oaiSetsManagementPermission.getId());
		groupManager.addPermissionById(adminGroup.getId(), permissionManagementPermission.getId());
		groupManager.addPermissionById(adminGroup.getId(), userManagementPermission.getId());

		groupManager.addPermissionById(oaiRecordsInclusionGroup.getId(), oaiRecordsInclusionPermission.getId());

		/**
		 * Users
		 */
		UserDto adminUser = userManager.createOneUser(firstName, lastName, email, password, true);
		
		/**
		 * UserGroups
		 */
		groupManager.addUserById(adminGroup.getId(), adminUser.getId());
		
		/**
		 * HARVESTING
		 */
		/**
		 * CISIC OAI SET
		 */
		oaiSetManager.createOneOaiSet("col_123456789_40", "CISIC");

		/**
		 * temp
		 */
		groupManager.addUserById(oaiRecordsInclusionGroup.getId(), adminUser.getId());

		/**
		 * 
		 */
		sysparamManager.createOneSysparam("IS_SYSTEM_SEEDED", "true");
	}

}
