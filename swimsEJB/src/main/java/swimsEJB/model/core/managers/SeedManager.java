package swimsEJB.model.core.managers;

import java.util.HashMap;

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

	public void verifyWebappPathsMapCorrectness(HashMap<String, String> webappPaths) throws Exception {
		if (!webappPaths.containsKey("HARVESTING_OAI_RECORDS_INCLUSION_WEBAPP_PATH"))
			throw new Exception("HARVESTING_OAI_RECORDS_INCLUSION_WEBAPP_PATH non existent.");
		;
		if (!webappPaths.containsKey("HARVESTING_OAI_SETS_MANAGEMENT_WEBAPP_PATH"))
			throw new Exception("HARVESTING_OAI_SETS_MANAGEMENT_WEBAPP_PATH non existent.");
		if (!webappPaths.containsKey("HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH"))
			throw new Exception("HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH non existent.");
		if (!webappPaths.containsKey("HARVESTING_THESIS_RECORD_DATA_EXTRACTION_WEBAPP_PATH"))
			throw new Exception("HARVESTING_THESIS_RECORD_DATA_EXTRACTION_WEBAPP_PATH non existent.");
		if (!webappPaths.containsKey("AUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH"))
			throw new Exception("AUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH non existent.");
		if (!webappPaths.containsKey("AUTH_USER_MANAGEMENT_WEBAPP_PATH"))
			throw new Exception("AUTH_USER_MANAGEMENT_WEBAPP_PATH non existent.");
	}

	public void seed(String firstName, String lastName, String email, String password,
			HashMap<String, String> webappPaths) throws Exception {
		Sysparam sysparam = sysparamManager.findOneByKey("IS_SYSTEM_SEEDED");
		if (sysparam != null)
			if (sysparam.getValue() == "true")
				throw new Exception("System already seeded.");
		if (!userManager.findAllUsers().isEmpty())
			throw new Exception("System already seeded.");

		verifyWebappPathsMapCorrectness(webappPaths);

		/**
		 * 1. PERMISSIONS
		 */
		/**
		 * 1.1. HARVESTING
		 */
		Permission oaiRecordsInclusionPermission = permissionManager.createOnePermission("Inclusión de Registros OAI",
				webappPaths.get("HARVESTING_OAI_RECORDS_INCLUSION_WEBAPP_PATH"));
		Permission oaiSetsManagementPermission = permissionManager.createOnePermission("Administración de Sets OAI",
				webappPaths.get("HARVESTING_OAI_SETS_MANAGEMENT_WEBAPP_PATH"));
		Permission thesisRecordInclussionPermission = permissionManager.createOnePermission(
				"Asignación de registros de tesis", webappPaths.get("HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH"));
		Permission thesisRecordDataExtractionPermission = permissionManager.createOnePermission(
				"Extracción de datos de tesis",
				webappPaths.get("HARVESTING_THESIS_RECORD_DATA_EXTRACTION_WEBAPP_PATH"));
		/**
		 * 1.2. AUTH
		 */
		Permission permissionManagementPermission = permissionManager.createOnePermission("Administración de Permisos",
				webappPaths.get("AUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH"));
		Permission userManagementPermission = permissionManager.createOnePermission("Administración de Usuario",
				webappPaths.get("AUTH_USER_MANAGEMENT_WEBAPP_PATH"));

		/**
		 * 2. GROUPS
		 */
		Group adminGroup = groupManager.createOneGroup("Administradores", true);
		Group thesisRecordManagementGroup = groupManager.createOneGroup("Gestores de Registros de Tesis");
		Group thesisDataExtracionGroup = groupManager.createOneGroup("Extractores de Datos de Tesis");

		/**
		 * 3. GroupPermissions
		 */
		/**
		 * 3.1. Admin Group
		 */
		groupManager.addPermissionById(adminGroup.getId(), oaiSetsManagementPermission.getId());
		groupManager.addPermissionById(adminGroup.getId(), permissionManagementPermission.getId());
		groupManager.addPermissionById(adminGroup.getId(), userManagementPermission.getId());

		/**
		 * 3.2. Thesis Record Management Group
		 */
		groupManager.addPermissionById(thesisRecordManagementGroup.getId(), oaiRecordsInclusionPermission.getId());
		groupManager.addPermissionById(thesisRecordManagementGroup.getId(), thesisRecordInclussionPermission.getId());

		/**
		 * 3.3. Thesis Data Extraction Group
		 */
		groupManager.addPermissionById(thesisDataExtracionGroup.getId(), thesisRecordDataExtractionPermission.getId());

		/**
		 * 4. Users
		 */
		UserDto adminUser = userManager.createOneUser(firstName, lastName, email, password, true);

		/**
		 * 5. UserGroups
		 */
		groupManager.addUserById(adminGroup.getId(), adminUser.getId());

		/**
		 * 6. Others
		 */
		/**
		 * 6.1. CISIC OAI SET
		 */
		oaiSetManager.createOneOaiSet("col_123456789_40", "CISIC");

		/**
		 * 
		 */
		sysparamManager.createOneSysparam("IS_SYSTEM_SEEDED", "true");

		/**
		 * temp
		 */
		groupManager.addUserById(thesisRecordManagementGroup.getId(), adminUser.getId());
		groupManager.addUserById(thesisDataExtracionGroup.getId(), adminUser.getId());
	}

}
