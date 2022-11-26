package swimsEJB.model.core.managers;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
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
import swimsEJB.model.harvesting.entities.StudyVariable;
import swimsEJB.model.harvesting.entities.StudyVariableClass;
import swimsEJB.model.harvesting.managers.OaiSetManager;
import swimsEJB.model.harvesting.managers.StudyVariableClassManager;
import swimsEJB.model.harvesting.managers.StudyVariableManager;
import swimsEJB.model.harvesting.services.LimesurveyService;
import swimsEJB.utilities.ResourceUtilities;

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
	@EJB
	private StudyVariableClassManager studyVariableClassManager;
	@EJB
	private StudyVariableManager studyVariableManager;

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
				"Asignación de Tesis y Encuestas", webappPaths.get("HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH"));
		Permission thesisRecordDataExtractionPermission = permissionManager.createOnePermission(
				"Extracción de Datos de Tesis",
				webappPaths.get("HARVESTING_THESIS_RECORD_DATA_EXTRACTION_WEBAPP_PATH"));
		/**
		 * 1.2. AUTH
		 */
		Permission permissionManagementPermission = permissionManager.createOnePermission("Administración de Permisos",
				webappPaths.get("AUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH"));
		Permission userManagementPermission = permissionManager.createOnePermission("Administración de Usuarios",
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

	public void seed2() {
		try {
			InputStream inputStream = ResourceUtilities
					.getResourceInputStream("impact-indicators_limesurvey_survey.lss");

			String imageStr = Base64.getEncoder()
					.encodeToString(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).getBytes());

			System.out.println(imageStr);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void seedLimesurvey() throws Exception {
		/**
		 * 0. Study Variable Seeding
		 */
		/**
		 * 0.1. Study Variable Classes
		 */
		StudyVariableClass socialImpactIndicatorsStudyVariableClass = studyVariableClassManager
				.createOneStudyVariableClass("Indicadores de Impacto Social", "indicadoresImpactoSocial");
		StudyVariableClass economicImpactIndicatorsStudyVariableClass = studyVariableClassManager
				.createOneStudyVariableClass("Indicadores de Impacto Económico", "indicadoresImpactoEconomico");
		StudyVariableClass naturalEnvironmentImpactIndicatorsStudyVariableClass = studyVariableClassManager
				.createOneStudyVariableClass("Indicadores de Impacto Medioambiental",
						"indicadoresImpactoMedioambiental");
		StudyVariableClass successFactorsStudyVariableClass = studyVariableClassManager
				.createOneStudyVariableClass("Factores de Éxito", "factoresExito");
		StudyVariableClass failureFactorsStudyVariableClass = studyVariableClassManager
				.createOneStudyVariableClass("Factores de Fracaso", "factoresFracaso");
		/**
		 * 0.2. Study Variables
		 */
//		String longName, String shortName, Boolean isQuantitativeContinuous,
//		Boolean isQuantitativeDiscrete, boolean isQualitative, boolean isLikert, boolean isBoolean,
//		StudyVariableClass studyVariableClass
		/**
		 * 0.2.1. Social Impact Indicators
		 */
		StudyVariable employeeNumberStudyVariable = studyVariableManager.createOneStudyVariable(
				"Número de empleados de la empresa beneficiaria", "numeroEmpleados", false, true, false, false, false,
				socialImpactIndicatorsStudyVariableClass);
		StudyVariable companySizeStudyVariable = studyVariableManager.createOneStudyVariable(
				"Tamaño de la empresa beneficiaria", "tamanoEmpresa", false, false, true, false, false,
				socialImpactIndicatorsStudyVariableClass);
		StudyVariable propiedadCapitalStudyVariable = studyVariableManager.createOneStudyVariable(
				"Propiedad del capital", "propiedadCapital", false, false, true, false, false,
				socialImpactIndicatorsStudyVariableClass);
		StudyVariable economyFieldStudyVariable = studyVariableManager.createOneStudyVariable("Sector de la economía",
				"sectorEconomia", false, false, true, false, false, socialImpactIndicatorsStudyVariableClass);
		StudyVariable ambitoActuacionStudyVariable = studyVariableManager.createOneStudyVariable("Ámbito de actuación",
				"ambitoActuacion", false, false, true, false, false, socialImpactIndicatorsStudyVariableClass);
		StudyVariable conceptoEntregaStudyVariable = studyVariableManager.createOneStudyVariable(
				"Concepto de Entrega del Producto", "conceptoEntrega", false, false, true, false, false,
				socialImpactIndicatorsStudyVariableClass);
		/**
		 * 0.2.2. Economic Impact Indicators
		 */
		StudyVariable projectBudgetStudyVariable = studyVariableManager.createOneStudyVariable(
				"Presupuesto del Proyecto de Tesis", "presupuesto", true, false, false, false, false,
				economicImpactIndicatorsStudyVariableClass);
		StudyVariable netYearlyIncomeStudyVariable = studyVariableManager.createOneStudyVariable(
				"Ingreso bruto anual de la empresa", "ingresoBrutoEmpresa", true, false, false, false, false,
				economicImpactIndicatorsStudyVariableClass);
		/**
		 * 0.2.3. Natural Environment Impact Indicators
		 */
		StudyVariable naturalEnvironmentRelatedStudyVariable = studyVariableManager.createOneStudyVariable(
				"Relación con áreas del medio ambiente", "rlcnMedioambiente", false, false, true, false, true,
				economicImpactIndicatorsStudyVariableClass);
		/*
		 * 0.2.4. Success factors
		 */
		StudyVariable requirementsClarityStudyVariable = studyVariableManager.createOneStudyVariable(
				"Requisitos claros y bien definidos", "claridadRequisitos", false, false, false, true, false,
				successFactorsStudyVariableClass);
		StudyVariable objetivesClarityStudyVariable = studyVariableManager.createOneStudyVariable(
				"Objetivos y metas claras", "claridadObjetivos", false, false, false, true, false,
				successFactorsStudyVariableClass);
		StudyVariable cronogramaRealistaStudyVariable = studyVariableManager.createOneStudyVariable(
				"Cronograma realista", "cronogramaRealista", false, false, false, true, false,
				successFactorsStudyVariableClass);
		StudyVariable directivesSupportStudyVariable = studyVariableManager.createOneStudyVariable(
				"Soporte de directivos de la empresa beneficiaria", "soporteDirectivos", false, false, false, true,
				false, successFactorsStudyVariableClass);
		StudyVariable userInvolvementStudyVariable = studyVariableManager.createOneStudyVariable(
				"Involucramiento del usuario o del cliente", "involucramientoUsrio", false, false, false, true, false,
				successFactorsStudyVariableClass);

		InputStream inputStream;
		Encoder encoder = Base64.getEncoder();
		String limesurveySessionKey;
		try {
			limesurveySessionKey = LimesurveyService.getSessionKey();
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(
					"Imposible establecer conexión con la API de limesurvey. Por favor asegúrese de que la interfaz JSON-RPC está activada. Dirígase a https://manual.limesurvey.org/RemoteControl_2_API para obtener mas información.");
		}
		/**
		 * 1. Survey creation
		 */
		/**
		 * 1.1. Impact indicators survey
		 */
		inputStream = ResourceUtilities.getResourceInputStream("impact-indicators_limesurvey_survey.lss");
		int impactIndicatorsSurveyId = LimesurveyService.importSurvey(limesurveySessionKey,
				encoder.encodeToString(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).getBytes()));
		/**
		 * 1.2. Success or Failure Factors Survey
		 */
		inputStream = ResourceUtilities.getResourceInputStream("success-failure-factors_limesurvey_survey.lss");
		int successFailureFactorsSurveyId = LimesurveyService.importSurvey(limesurveySessionKey,
				encoder.encodeToString(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).getBytes()));
	}
}
