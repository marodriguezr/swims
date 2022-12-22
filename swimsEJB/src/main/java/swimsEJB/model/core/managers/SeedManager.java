package swimsEJB.model.core.managers;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.commons.validator.routines.EmailValidator;

import swimsEJB.model.auth.dtos.UserDto;
import swimsEJB.model.auth.entities.Group;
import swimsEJB.model.auth.entities.Permission;
import swimsEJB.model.auth.managers.GroupManager;
import swimsEJB.model.auth.managers.PermissionManager;
import swimsEJB.model.auth.managers.UserManager;
import swimsEJB.model.core.entities.Sysparam;
import swimsEJB.model.harvesting.dtos.LimesurveyQuestionDto;
import swimsEJB.model.harvesting.entities.StudyVariable;
import swimsEJB.model.harvesting.entities.StudyVariableClass;
import swimsEJB.model.harvesting.managers.OaiSetManager;
import swimsEJB.model.harvesting.managers.QuestionManager;
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
	@EJB
	private QuestionManager questionManager;

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
		Sysparam sysparam;
		try {
			sysparam = sysparamManager.findOneByKey("IS_SYSTEM_SEEDED");
		} catch (Exception e) {
			throw new Exception(
					"No se ha encontrado el esquema de base de datos. Por favor configure la base de datos antes de continuar.");
		}
		if (sysparam != null)
			if (sysparam.getValue() == "true")
				throw new Exception("System already seeded.");
		if (!userManager.findAllUsers().isEmpty())
			throw new Exception("System already seeded.");

		verifyWebappPathsMapCorrectness(webappPaths);

		if (!EmailValidator.getInstance().isValid(email))
			throw new Exception("El correo electrónico ingresado no es válido. Por favor ingrese un valor apropiado.");

		/**
		 * 0. Study Variables and Limesurvey Surveys
		 */
		seedStudyVariablesAndLimesurvey();

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
		Permission thesisRecordManagementPermission = permissionManager.createOnePermission(
				"Administración de Registros de Tesis",
				webappPaths.get("HARVESTING_THESIS_RECORD_MANAGEMENT_WEBAPP_PATH"));
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
		groupManager.addPermissionById(thesisRecordManagementGroup.getId(), thesisRecordManagementPermission.getId());
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

	public void seedStudyVariablesAndLimesurvey() throws Exception {
		String limesurveySessionKey;
		try {
			limesurveySessionKey = LimesurveyService.getSessionKey();
			LimesurveyService.releaseSessionKey(limesurveySessionKey);
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(
					"Imposible establecer conexión con la API de limesurvey. Por favor asegúrese de que la interfaz JSON-RPC está activada. Dirígase a https://manual.limesurvey.org/RemoteControl_2_API para obtener mas información.");
		}
		InputStream inputStream;
		Encoder encoder = Base64.getEncoder();

		/**
		 * 0. Study Variable Seeding
		 */
		/**
		 * 0.1. Study Variable Classes
		 */
		StudyVariableClass socialImpactIndicatorsStudyVariableClass = studyVariableClassManager
				.createOneStudyVariableClass("indicadoresImpactoSocial", "Indicadores de Impacto Social");
		StudyVariableClass economicImpactIndicatorsStudyVariableClass = studyVariableClassManager
				.createOneStudyVariableClass("indicadoresImpactoEconomico", "Indicadores de Impacto Económico");
		StudyVariableClass naturalEnvironmentImpactIndicatorsStudyVariableClass = studyVariableClassManager
				.createOneStudyVariableClass("indicadoresImpactoMedioambiental",
						"Indicadores de Impacto Medioambiental");
		StudyVariableClass otherIndicatorsStudyVariableClass = studyVariableClassManager
				.createOneStudyVariableClass("otrosIndicadores", "Otros Indicadores");
		StudyVariableClass successFactorsStudyVariableClass = studyVariableClassManager
				.createOneStudyVariableClass("factoresExito", "Factores de Éxito");
		StudyVariableClass failureFactorsStudyVariableClass = studyVariableClassManager
				.createOneStudyVariableClass("factoresFracaso", "Factores de Fracaso");
		StudyVariableClass devResourcesStudyVariableClass = studyVariableClassManager
				.createOneStudyVariableClass("recursosDesarrollo", "Recursos de Desarrollo");
		/**
		 * 0.2. Study Variables
		 */
		/**
		 * 0.2.1. Impact Indicators
		 */
		List<StudyVariable> impactStudyVariables = new ArrayList<>();
		/**
		 * 0.2.1.1. Social Impact Indicators
		 */
		StudyVariable organizationNameStudyVariable = studyVariableManager.createOneStudyVariable("nombreEntidadBenef",
				"Nombre de la entidad beneficiaria", false, false, true, false,
				socialImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(organizationNameStudyVariable);

		StudyVariable employeeNumberStudyVariable = studyVariableManager.createOneStudyVariable("numeroEmpleados",
				"Número de empleados de la empresa beneficiaria", false, true, false, false,
				socialImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(employeeNumberStudyVariable);

		StudyVariable companySizeStudyVariable = studyVariableManager.createOneStudyVariable("tamanoEmpresa",
				"Tamaño de la empresa beneficiaria", false, false, true, false,
				socialImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(companySizeStudyVariable);

		StudyVariable propiedadCapitalStudyVariable = studyVariableManager.createOneStudyVariable("propiedadCapital",
				"Propiedad del capital", false, false, true, false, socialImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(propiedadCapitalStudyVariable);

		StudyVariable economyFieldStudyVariable = studyVariableManager.createOneStudyVariable("sectorEconomia",
				"Sector de la economía", false, false, true, false, socialImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(economyFieldStudyVariable);

		StudyVariable ambitoActuacionStudyVariable = studyVariableManager.createOneStudyVariable("ambitoActuacion",
				"Ámbito de actuación", false, false, true, false, socialImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(ambitoActuacionStudyVariable);

		StudyVariable ubicacionAfeccionStudyVariable = studyVariableManager.createOneStudyVariable("ubicacionAfeccion",
				"Ubicación de Afección", false, false, true, false, socialImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(ubicacionAfeccionStudyVariable);

		StudyVariable conceptoEntregaStudyVariable = studyVariableManager.createOneStudyVariable("conceptoEntrega",
				"Concepto de Entrega del Producto", false, false, true, false,
				socialImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(conceptoEntregaStudyVariable);
		/**
		 * 0.2.1.2. Economic Impact Indicators
		 */
		StudyVariable projectBudgetStudyVariable = studyVariableManager.createOneStudyVariable("presupuesto",
				"Presupuesto del Proyecto de Tesis", true, false, false, false,
				economicImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(projectBudgetStudyVariable);
		StudyVariable netYearlyIncomeStudyVariable = studyVariableManager.createOneStudyVariable("ingresoBrutoEmpresa",
				"Ingreso bruto anual de la empresa", true, false, false, false,
				economicImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(netYearlyIncomeStudyVariable);
		/**
		 * 0.2.1.3. Natural Environment Impact Indicators
		 */
		StudyVariable naturalEnvironmentRelatedStudyVariable = studyVariableManager.createOneStudyVariable(
				"rlcnMedioambiente", "Relación con áreas del medio ambiente", false, false, true, false,
				naturalEnvironmentImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(naturalEnvironmentRelatedStudyVariable);
		/**
		 * 0.2.1.4. Other Impact Indicators
		 */
		StudyVariable startDateStudyVariable = studyVariableManager.createOneStudyVariable("fechaInicio",
				"Fecha de inicio del proyecto", false, true, false, false, otherIndicatorsStudyVariableClass);
		impactStudyVariables.add(startDateStudyVariable);

		/*
		 * 0.2.4. Factors
		 */
		List<StudyVariable> successFailureFactorsStudyVariables = new ArrayList<>();
		/**
		 * 0.2.4.1. Success Factors
		 */
		StudyVariable requirementsClarityStudyVariable = studyVariableManager.createOneStudyVariable(
				"claridadRequisitos", "Requisitos claros y bien definidos", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(requirementsClarityStudyVariable);
		StudyVariable objetivesClarityStudyVariable = studyVariableManager.createOneStudyVariable("claridadObjetivos",
				"Objetivos y metas claras", false, false, false, true, successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(objetivesClarityStudyVariable);
		StudyVariable cronogramaRealistaStudyVariable = studyVariableManager.createOneStudyVariable(
				"cronogramaRealista", "Cronograma realista", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(cronogramaRealistaStudyVariable);
		StudyVariable directivesSupportStudyVariable = studyVariableManager.createOneStudyVariable("soporteDirectivos",
				"Soporte de directivos de la empresa beneficiaria", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(directivesSupportStudyVariable);
		StudyVariable userInvolvementStudyVariable = studyVariableManager.createOneStudyVariable("involucramientoUsrio",
				"Involucramiento del usuario o del cliente", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(userInvolvementStudyVariable);
		StudyVariable effectiveComunicationStudyVariable = studyVariableManager.createOneStudyVariable(
				"comunicacionEfectiva", "Comunicación efectiva entre interesados y desarrollador", false, false, false,
				true, successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(effectiveComunicationStudyVariable);
		StudyVariable toolingFamiliarityStudyVariable = studyVariableManager.createOneStudyVariable(
				"familiaridadHerramnt", "Familiaridad con las herramientas de desarrollo", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(toolingFamiliarityStudyVariable);
		StudyVariable claridadAlcanceStudyVariable = studyVariableManager.createOneStudyVariable("claridadAlcance",
				"Alcance del proyecto bien definido", false, false, false, true, successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(claridadAlcanceStudyVariable);
		StudyVariable qualityAssuranceStudyVariable = studyVariableManager.createOneStudyVariable(
				"aseguramientoCalidad", "Aseguramiento de la calidad", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(qualityAssuranceStudyVariable);
		StudyVariable provisionCapacitacionStudyVariable = studyVariableManager.createOneStudyVariable(
				"provisionCapacitacio", "Provisión de tutorías o capacitaciones a los beneficiarios", false, false,
				true, false, successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(provisionCapacitacionStudyVariable);
		StudyVariable customerSatisfactionStudyVariable = studyVariableManager.createOneStudyVariable(
				"aseguramtoSatsfacsn", "Satisfacción del cliente", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(customerSatisfactionStudyVariable);
		StudyVariable upToDateProjectStatusReportStudyVariable = studyVariableManager.createOneStudyVariable(
				"reporteProgActualiz", "Reporte de progreso actualizado", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(upToDateProjectStatusReportStudyVariable);
		/**
		 * 0.2.4.2. Failure Factors
		 */
		StudyVariable testLackingStudyVariable = studyVariableManager.createOneStudyVariable("faltaPruebas",
				"Falta de pruebas de software", false, false, false, true, failureFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(testLackingStudyVariable);
		StudyVariable targetIdentificationLackingStudyVariable = studyVariableManager.createOneStudyVariable(
				"faltaIdentPobObjet", "Falta de identificación de la población objetivo", false, false, false, true,
				failureFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(targetIdentificationLackingStudyVariable);
		StudyVariable requirementChangeStudyVariable = studyVariableManager.createOneStudyVariable("cambioRequisitos",
				"Cambio de requisitos y especificaciones", false, false, false, true, failureFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(requirementChangeStudyVariable);
		StudyVariable improperBudgetStudyVariable = studyVariableManager.createOneStudyVariable("presupuestoInsufic",
				"Presupuesto insuficiente", false, false, false, true, failureFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(improperBudgetStudyVariable);
		StudyVariable unplannedScopeGrowthStudyVariable = studyVariableManager.createOneStudyVariable(
				"crecimiInespAlcanc", "Crecimiento inesperado del alcance", false, false, false, true,
				failureFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(unplannedScopeGrowthStudyVariable);
		StudyVariable restrictiveToolsStudyVariable = studyVariableManager.createOneStudyVariable("herramientRestric",
				"Herramientas restrictivas", false, false, false, true, failureFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(restrictiveToolsStudyVariable);
		StudyVariable managerChangeStudyVariable = studyVariableManager.createOneStudyVariable("cambioGestor",
				"Cambio de gestor del proyecto", false, false, true, false, failureFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(managerChangeStudyVariable);

		/**
		 * 0.2.5. Development Resources
		 */
		List<StudyVariable> devResourcesStudyVariables = new ArrayList<>();
		/**
		 * 0.2.5.1. Development tools
		 */
		StudyVariable programmingLanguageStudyVariable = studyVariableManager.createOneStudyVariable("lenguaje",
				"Lenguaje utilizado", false, false, true, false, devResourcesStudyVariableClass);
		devResourcesStudyVariables.add(programmingLanguageStudyVariable);
		StudyVariable frameworkStudyVariable = studyVariableManager.createOneStudyVariable("framework", "Framework",
				false, false, true, false, devResourcesStudyVariableClass);
		devResourcesStudyVariables.add(frameworkStudyVariable);
		StudyVariable libraryStudyVariable = studyVariableManager.createOneStudyVariable("libreria", "Librerías", false,
				false, true, false, devResourcesStudyVariableClass);
		devResourcesStudyVariables.add(libraryStudyVariable);
		StudyVariable devEnvironmentStudyVariable = studyVariableManager.createOneStudyVariable("entornoDesarrollo",
				"Entorno de desarrollo", false, false, true, false, devResourcesStudyVariableClass);
		devResourcesStudyVariables.add(devEnvironmentStudyVariable);
		StudyVariable dbmsStudyVariable = studyVariableManager.createOneStudyVariable("dbms",
				"Sistema gestor de base de datos", false, false, true, false, devResourcesStudyVariableClass);
		devResourcesStudyVariables.add(dbmsStudyVariable);
		StudyVariable deploymentServerStudyVariable = studyVariableManager.createOneStudyVariable("servidorDespliegue",
				"Servidor de despliegue", false, false, true, false, devResourcesStudyVariableClass);
		devResourcesStudyVariables.add(deploymentServerStudyVariable);
		StudyVariable deploymentOsStudyVariable = studyVariableManager.createOneStudyVariable("soDespliegue",
				"Sistema operativo de despliegue", false, false, true, false, devResourcesStudyVariableClass);
		devResourcesStudyVariables.add(deploymentOsStudyVariable);
		StudyVariable helpfulSoftwareStudyVariable = studyVariableManager.createOneStudyVariable("swApoyo",
				"Software de apoyo", false, false, true, false, devResourcesStudyVariableClass);
		devResourcesStudyVariables.add(helpfulSoftwareStudyVariable);
		StudyVariable iaasProviderStudyVariable = studyVariableManager.createOneStudyVariable("iaasProvider",
				"Proveedor de infraestructura como servicio", false, false, true, false,
				devResourcesStudyVariableClass);
		devResourcesStudyVariables.add(iaasProviderStudyVariable);
		/**
		 * 0.2.5.2. Development methodologies
		 */
		StudyVariable devMethodologyStudyVariable = studyVariableManager.createOneStudyVariable("metodologiaDesarroll",
				"Metodología de desarrollo", false, false, true, false, devResourcesStudyVariableClass);
		devResourcesStudyVariables.add(devMethodologyStudyVariable);

		/**
		 * 1. Survey creation
		 */
		/**
		 * 1.1. Impact indicators survey
		 */
		inputStream = ResourceUtilities.getResourceInputStream("impact-indicators_limesurvey-survey.lss");
		int impactIndicatorsSurveyId = LimesurveyService
				.importSurvey(new String(encoder.encode(inputStream.readAllBytes()), StandardCharsets.UTF_8));
		LimesurveyService.activateSurveyWithParticipants(impactIndicatorsSurveyId);
		/**
		 * 1.2. Success or Failure Factors Survey
		 */
		inputStream = ResourceUtilities.getResourceInputStream("success-failure-factors_limesurvey-survey.lss");
		int successFailureFactorsSurveyId = LimesurveyService
				.importSurvey(new String(encoder.encode(inputStream.readAllBytes()), StandardCharsets.UTF_8));
		LimesurveyService.activateSurveyWithParticipants(successFailureFactorsSurveyId);
		/**
		 * 1.3 Development Resources Survey
		 */
		inputStream = ResourceUtilities.getResourceInputStream("dev-resources_limesurvey-survey.lss");
		int devResourcesSurveyId = LimesurveyService
				.importSurvey(new String(encoder.encode(inputStream.readAllBytes()), StandardCharsets.UTF_8));
		LimesurveyService.activateSurveyWithParticipants(devResourcesSurveyId);
		/**
		 * 2. Questions
		 */
		/**
		 * 2.1. Impact Indicator Questions
		 */
		HashMap<String, LimesurveyQuestionDto> impactIndicatorsSurveyQuestionDtosMap = LimesurveyService
				.listQuestions(impactIndicatorsSurveyId);
		for (StudyVariable studyVariable : impactStudyVariables) {
			if (impactIndicatorsSurveyQuestionDtosMap.get(studyVariable.getId()) == null)
				throw new Exception("La pregunta correspondiente al indicador " + studyVariable.getName()
						+ " no se encuentra registrada.");
			LimesurveyQuestionDto limesurveyQuestionDto = impactIndicatorsSurveyQuestionDtosMap
					.get(studyVariable.getId());
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), studyVariable);
		}
		/**
		 * 2.2. Success or Failure Factors
		 */
		HashMap<String, LimesurveyQuestionDto> successFailureFactorsSurveyQuestionDtos = LimesurveyService
				.listQuestions(successFailureFactorsSurveyId);
		for (StudyVariable studyVariable : successFailureFactorsStudyVariables) {
			if (successFailureFactorsSurveyQuestionDtos.get(studyVariable.getId()) == null)
				throw new Exception("La pregunta correspondiente al factor " + studyVariable.getName()
						+ " no se encuentra registrada.");
			LimesurveyQuestionDto limesurveyQuestionDto = successFailureFactorsSurveyQuestionDtos
					.get(studyVariable.getId());
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), studyVariable);
		}
		/**
		 * 2.3. Development Resources
		 */
		HashMap<String, LimesurveyQuestionDto> devResourcesSurveyQuestionDtos = LimesurveyService
				.listQuestions(devResourcesSurveyId);
		for (StudyVariable studyVariable : devResourcesStudyVariables) {
			if (devResourcesSurveyQuestionDtos.get(studyVariable.getId()) == null)
				throw new Exception("La pregunta correspondiente al recurso " + studyVariable.getName()
						+ " no se encuentra registrada.");
			LimesurveyQuestionDto limesurveyQuestionDto = devResourcesSurveyQuestionDtos.get(studyVariable.getId());
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), studyVariable);
		}
		/**
		 * 2.3.1. Dev Tools
		 */
		/**
		 * 2.3.1.1. Programming Language
		 */
		LimesurveyQuestionDto programmingLanguagesQuestion = devResourcesSurveyQuestionDtos
				.get(programmingLanguageStudyVariable.getId());
		List<LimesurveyQuestionDto> programmingLanguagesSurveyQuestionDtos = devResourcesSurveyQuestionDtos.values()
				.stream().filter(arg0 -> arg0.getParentQid() == programmingLanguagesQuestion.getId())
				.collect(Collectors.toList());
		for (LimesurveyQuestionDto limesurveyQuestionDto : programmingLanguagesSurveyQuestionDtos) {
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), programmingLanguageStudyVariable);
		}
		/**
		 * 2.3.1.2. Framework
		 */
		LimesurveyQuestionDto frameworkQuestion = devResourcesSurveyQuestionDtos.get(frameworkStudyVariable.getId());
		List<LimesurveyQuestionDto> frameworkSurveyQuestionDtos = devResourcesSurveyQuestionDtos.values().stream()
				.filter(arg0 -> arg0.getParentQid() == frameworkQuestion.getId()).collect(Collectors.toList());
		for (LimesurveyQuestionDto limesurveyQuestionDto : frameworkSurveyQuestionDtos) {
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), frameworkStudyVariable);
		}
		/**
		 * 2.3.1.3. Library
		 */
		LimesurveyQuestionDto libraryQuestion = devResourcesSurveyQuestionDtos.get(libraryStudyVariable.getId());
		List<LimesurveyQuestionDto> librarySurveyQuestionDtos = devResourcesSurveyQuestionDtos.values().stream()
				.filter(arg0 -> arg0.getParentQid() == libraryQuestion.getId()).collect(Collectors.toList());
		for (LimesurveyQuestionDto limesurveyQuestionDto : librarySurveyQuestionDtos) {
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), libraryStudyVariable);
		}
		/**
		 * 2.3.1.3. Dev Environment
		 */
		LimesurveyQuestionDto devEnvironmentQuestion = devResourcesSurveyQuestionDtos
				.get(devEnvironmentStudyVariable.getId());
		List<LimesurveyQuestionDto> devEnviromentSurveyQuestionDtos = devResourcesSurveyQuestionDtos.values().stream()
				.filter(arg0 -> arg0.getParentQid() == devEnvironmentQuestion.getId()).collect(Collectors.toList());
		for (LimesurveyQuestionDto limesurveyQuestionDto : devEnviromentSurveyQuestionDtos) {
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), devEnvironmentStudyVariable);
		}
		/**
		 * 2.3.1.3. DBMS
		 */
		LimesurveyQuestionDto dbmsQuestion = devResourcesSurveyQuestionDtos.get(dbmsStudyVariable.getId());
		List<LimesurveyQuestionDto> dbmsSurveyQuestionDtos = devResourcesSurveyQuestionDtos.values().stream()
				.filter(arg0 -> arg0.getParentQid() == dbmsQuestion.getId()).collect(Collectors.toList());
		for (LimesurveyQuestionDto limesurveyQuestionDto : dbmsSurveyQuestionDtos) {
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), dbmsStudyVariable);
		}
		/**
		 * 2.3.1.3. Deployment Server
		 */
		LimesurveyQuestionDto deploymentServerQuestion = devResourcesSurveyQuestionDtos
				.get(deploymentServerStudyVariable.getId());
		List<LimesurveyQuestionDto> deploymentServerSurveyQuestionDtos = devResourcesSurveyQuestionDtos.values()
				.stream().filter(arg0 -> arg0.getParentQid() == deploymentServerQuestion.getId())
				.collect(Collectors.toList());
		for (LimesurveyQuestionDto limesurveyQuestionDto : deploymentServerSurveyQuestionDtos) {
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), deploymentServerStudyVariable);
		}
		/**
		 * 2.3.1.3. Deployment OS
		 */
		LimesurveyQuestionDto deploymentOsQuestion = devResourcesSurveyQuestionDtos
				.get(deploymentOsStudyVariable.getId());
		List<LimesurveyQuestionDto> deploymentOsSurveyQuestionDtos = devResourcesSurveyQuestionDtos.values().stream()
				.filter(arg0 -> arg0.getParentQid() == deploymentOsQuestion.getId()).collect(Collectors.toList());
		for (LimesurveyQuestionDto limesurveyQuestionDto : deploymentOsSurveyQuestionDtos) {
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), deploymentOsStudyVariable);
		}
		/**
		 * 2.3.1.3. Helpful Software
		 */
		LimesurveyQuestionDto helpfulSwQuestion = devResourcesSurveyQuestionDtos
				.get(helpfulSoftwareStudyVariable.getId());
		List<LimesurveyQuestionDto> helpfulSwSurveyQuestionDtos = devResourcesSurveyQuestionDtos.values().stream()
				.filter(arg0 -> arg0.getParentQid() == helpfulSwQuestion.getId()).collect(Collectors.toList());
		for (LimesurveyQuestionDto limesurveyQuestionDto : helpfulSwSurveyQuestionDtos) {
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), helpfulSoftwareStudyVariable);
		}
		/**
		 * 2.3.1.3. IAAS Provider
		 */
		LimesurveyQuestionDto iaasProviderQuestion = devResourcesSurveyQuestionDtos
				.get(iaasProviderStudyVariable.getId());
		List<LimesurveyQuestionDto> iaasProviderSurveyQuestionDtos = devResourcesSurveyQuestionDtos.values().stream()
				.filter(arg0 -> arg0.getParentQid() == iaasProviderQuestion.getId()).collect(Collectors.toList());
		for (LimesurveyQuestionDto limesurveyQuestionDto : iaasProviderSurveyQuestionDtos) {
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), iaasProviderStudyVariable);
		}

		/**
		 * 2.3.2. Dev Methodologies
		 * 
		 */
		LimesurveyQuestionDto devMethodologiesQuestion = devResourcesSurveyQuestionDtos
				.get(devMethodologyStudyVariable.getId());
		List<LimesurveyQuestionDto> devMethodologiesSurveyQuestionDtos = devResourcesSurveyQuestionDtos.values()
				.stream().filter(arg0 -> arg0.getParentQid() == devMethodologiesQuestion.getId())
				.collect(Collectors.toList());
		for (LimesurveyQuestionDto limesurveyQuestionDto : devMethodologiesSurveyQuestionDtos) {
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), devMethodologyStudyVariable);
		}
	}
}
