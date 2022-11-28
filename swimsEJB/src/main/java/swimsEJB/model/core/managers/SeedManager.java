package swimsEJB.model.core.managers;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		StudyVariableClass devToolsStudyVariableClass = studyVariableClassManager
				.createOneStudyVariableClass("Herramientas de Desarrollo", "heramientasDesarrollo");
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
		StudyVariable employeeNumberStudyVariable = studyVariableManager.createOneStudyVariable(
				"Número de empleados de la empresa beneficiaria", "numeroEmpleados", false, true, false, false,
				socialImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(employeeNumberStudyVariable);
		StudyVariable companySizeStudyVariable = studyVariableManager.createOneStudyVariable(
				"Tamaño de la empresa beneficiaria", "tamanoEmpresa", false, false, true, false,
				socialImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(companySizeStudyVariable);
		StudyVariable propiedadCapitalStudyVariable = studyVariableManager.createOneStudyVariable(
				"Propiedad del capital", "propiedadCapital", false, false, true, false,
				socialImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(propiedadCapitalStudyVariable);
		StudyVariable economyFieldStudyVariable = studyVariableManager.createOneStudyVariable("Sector de la economía",
				"sectorEconomia", false, false, true, false, socialImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(economyFieldStudyVariable);
		StudyVariable ambitoActuacionStudyVariable = studyVariableManager.createOneStudyVariable("Ámbito de actuación",
				"ambitoActuacion", false, false, true, false, socialImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(ambitoActuacionStudyVariable);
		StudyVariable conceptoEntregaStudyVariable = studyVariableManager.createOneStudyVariable(
				"Concepto de Entrega del Producto", "conceptoEntrega", false, false, true, false,
				socialImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(conceptoEntregaStudyVariable);
		/**
		 * 0.2.1.2. Economic Impact Indicators
		 */
		StudyVariable projectBudgetStudyVariable = studyVariableManager.createOneStudyVariable(
				"Presupuesto del Proyecto de Tesis", "presupuesto", true, false, false, false,
				economicImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(projectBudgetStudyVariable);
		StudyVariable netYearlyIncomeStudyVariable = studyVariableManager.createOneStudyVariable(
				"Ingreso bruto anual de la empresa", "ingresoBrutoEmpresa", true, false, false, false,
				economicImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(netYearlyIncomeStudyVariable);
		/**
		 * 0.2.1.3. Natural Environment Impact Indicators
		 */
		StudyVariable naturalEnvironmentRelatedStudyVariable = studyVariableManager.createOneStudyVariable(
				"Relación con áreas del medio ambiente", "rlcnMedioambiente", false, false, true, false,
				naturalEnvironmentImpactIndicatorsStudyVariableClass);
		impactStudyVariables.add(naturalEnvironmentRelatedStudyVariable);
		/*
		 * 0.2.4. Factors
		 */
		List<StudyVariable> successFailureFactorsStudyVariables = new ArrayList<>();
		/**
		 * 0.2.4.1. Success Factors
		 */
		StudyVariable requirementsClarityStudyVariable = studyVariableManager.createOneStudyVariable(
				"Requisitos claros y bien definidos", "claridadRequisitos", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(requirementsClarityStudyVariable);
		StudyVariable objetivesClarityStudyVariable = studyVariableManager.createOneStudyVariable(
				"Objetivos y metas claras", "claridadObjetivos", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(objetivesClarityStudyVariable);
		StudyVariable cronogramaRealistaStudyVariable = studyVariableManager.createOneStudyVariable(
				"Cronograma realista", "cronogramaRealista", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(cronogramaRealistaStudyVariable);
		StudyVariable directivesSupportStudyVariable = studyVariableManager.createOneStudyVariable(
				"Soporte de directivos de la empresa beneficiaria", "soporteDirectivos", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(directivesSupportStudyVariable);
		StudyVariable userInvolvementStudyVariable = studyVariableManager.createOneStudyVariable(
				"Involucramiento del usuario o del cliente", "involucramientoUsrio", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(userInvolvementStudyVariable);
		StudyVariable effectiveComunicationStudyVariable = studyVariableManager.createOneStudyVariable(
				"Comunicación efectiva entre interesados y desarrollador", "comunicacionEfectiva", false, false, false,
				true, successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(effectiveComunicationStudyVariable);
		StudyVariable toolingFamiliarityStudyVariable = studyVariableManager.createOneStudyVariable(
				"Familiaridad con las herramientas de desarrollo", "familiaridadHerramnt", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(toolingFamiliarityStudyVariable);
		StudyVariable claridadAlcanceStudyVariable = studyVariableManager.createOneStudyVariable(
				"Alcance del proyecto bien definido", "claridadAlcance", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(claridadAlcanceStudyVariable);
		StudyVariable qualityAssuranceStudyVariable = studyVariableManager.createOneStudyVariable(
				"Aseguramiento de la calidad", "aseguramientoCalidad", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(qualityAssuranceStudyVariable);
		StudyVariable provisionCapacitacionStudyVariable = studyVariableManager.createOneStudyVariable(
				"Provisión de tutorías o capacitaciones a los beneficiarios", "provisionCapacitacio", false, false,
				true, false, successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(provisionCapacitacionStudyVariable);
		StudyVariable customerSatisfactionStudyVariable = studyVariableManager.createOneStudyVariable(
				"Satisfacción del cliente", "aseguramtoSatsfacsn", false, false, false, true,
				successFactorsStudyVariableClass);
		successFailureFactorsStudyVariables.add(customerSatisfactionStudyVariable);

		/**
		 * Development tools
		 */
		StudyVariable programmingLanguageStudyVariable = studyVariableManager.createOneStudyVariable(
				"Lenguaje de Programación", "lenguajeProgramacion", false, false, true, false,
				devToolsStudyVariableClass);
		StudyVariable frameworkStudyVariable = studyVariableManager.createOneStudyVariable("Framework", "framework",
				false, false, true, false, devToolsStudyVariableClass);
		StudyVariable libraryStudyVariable = studyVariableManager.createOneStudyVariable("Librerías", "libreria", false,
				false, true, false, devToolsStudyVariableClass);

		/**
		 * 1. Survey creation
		 */
		/**
		 * 1.1. Impact indicators survey
		 */
		inputStream = ResourceUtilities.getResourceInputStream("impact-indicators_limesurvey-survey.lss");
		int impactIndicatorsSurveyId = LimesurveyService.importSurvey(
				encoder.encodeToString(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).getBytes()));
		LimesurveyService.activateSurveyWithParticipants(impactIndicatorsSurveyId);
		/**
		 * 1.2. Success or Failure Factors Survey
		 */
		inputStream = ResourceUtilities.getResourceInputStream("success-failure-factors_limesurvey-survey.lss");
		int successFailureFactorsSurveyId = LimesurveyService.importSurvey(
				encoder.encodeToString(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).getBytes()));
		LimesurveyService.activateSurveyWithParticipants(successFailureFactorsSurveyId);
		/**
		 * 1.3 Tools Survey
		 */
		inputStream = ResourceUtilities.getResourceInputStream("tools_limesurvey-survey.lss");
		int toolsSurveyId = LimesurveyService.importSurvey(
				encoder.encodeToString(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).getBytes()));
		LimesurveyService.activateSurveyWithParticipants(toolsSurveyId);
		/**
		 * 2. Questions
		 */
		/**
		 * 2.1. Impact Indicator Questions
		 */
		HashMap<String, LimesurveyQuestionDto> impactIndicatorsSurveyQuestionDtosMap = LimesurveyService
				.listQuestions(impactIndicatorsSurveyId);
		Map<Integer, LimesurveyQuestionDto> impactIndicatorsSurveyQuestionDtosMap2 = impactIndicatorsSurveyQuestionDtosMap
				.values().stream().collect(Collectors.toMap(LimesurveyQuestionDto::getId, item -> item));
		for (StudyVariable studyVariable : impactStudyVariables) {
			if (impactIndicatorsSurveyQuestionDtosMap.get(studyVariable.getShortName()) == null)
				throw new Exception("La pregunta correspondiente al indicador " + studyVariable.getLongName()
						+ " no se encuentra registrada.");
			LimesurveyQuestionDto limesurveyQuestionDto = impactIndicatorsSurveyQuestionDtosMap
					.get(studyVariable.getShortName());
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), studyVariable, limesurveyQuestionDto.getParentQid(),
					impactIndicatorsSurveyQuestionDtosMap2.get(limesurveyQuestionDto.getParentQid()) == null ? null
							: impactIndicatorsSurveyQuestionDtosMap2.get(limesurveyQuestionDto.getParentQid())
									.getTitle());
		}
		/**
		 * 2.2. Success or Failure Factors
		 */
		HashMap<String, LimesurveyQuestionDto> successFailureFactorsSurveyQuestionDtos = LimesurveyService
				.listQuestions(successFailureFactorsSurveyId);
		Map<Integer, LimesurveyQuestionDto> successFailureFactorsSurveyQuestionDtos2 = successFailureFactorsSurveyQuestionDtos
				.values().stream().collect(Collectors.toMap(arg0 -> arg0.getId(), arg0 -> arg0));
		for (StudyVariable studyVariable : successFailureFactorsStudyVariables) {
			if (successFailureFactorsSurveyQuestionDtos.get(studyVariable.getShortName()) == null)
				throw new Exception("La pregunta correspondiente al factor " + studyVariable.getLongName()
						+ " no se encuentra registrada.");
			LimesurveyQuestionDto limesurveyQuestionDto = successFailureFactorsSurveyQuestionDtos
					.get(studyVariable.getShortName());
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), studyVariable, limesurveyQuestionDto.getParentQid(),
					successFailureFactorsSurveyQuestionDtos2.get(limesurveyQuestionDto.getParentQid()) == null ? null
							: successFailureFactorsSurveyQuestionDtos2.get(limesurveyQuestionDto.getParentQid())
									.getTitle());
		}
		/**
		 * 2.3. Tools
		 */
		HashMap<String, LimesurveyQuestionDto> toolsSurveyQuestionDtos = LimesurveyService.listQuestions(toolsSurveyId);
		/**
		 * 2.3.1. Programming Language
		 */
		LimesurveyQuestionDto programmingLanguagesQuestion = toolsSurveyQuestionDtos
				.get(programmingLanguageStudyVariable.getShortName());
		List<LimesurveyQuestionDto> programmingLanguagesSurveyQuestionDtos = toolsSurveyQuestionDtos.values().stream()
				.filter(arg0 -> arg0.getParentQid() == programmingLanguagesQuestion.getId())
				.collect(Collectors.toList());
		for (LimesurveyQuestionDto limesurveyQuestionDto : programmingLanguagesSurveyQuestionDtos) {
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), programmingLanguageStudyVariable,
					limesurveyQuestionDto.getParentQid(), programmingLanguagesQuestion.getTitle());
		}
		/**
		 * 2.3.2. Framework
		 */
		LimesurveyQuestionDto frameworkQuestion = toolsSurveyQuestionDtos.get(frameworkStudyVariable.getShortName());
		List<LimesurveyQuestionDto> frameworkSurveyQuestionDtos = toolsSurveyQuestionDtos.values().stream()
				.filter(arg0 -> arg0.getParentQid() == frameworkQuestion.getId()).collect(Collectors.toList());
		for (LimesurveyQuestionDto limesurveyQuestionDto : frameworkSurveyQuestionDtos) {
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), frameworkStudyVariable, limesurveyQuestionDto.getParentQid(),
					frameworkQuestion.getTitle());
		}
		/**
		 * 2.3.3. Library
		 */
		LimesurveyQuestionDto libraryQuestion = toolsSurveyQuestionDtos.get(libraryStudyVariable.getShortName());
		List<LimesurveyQuestionDto> librarySurveyQuestionDtos = toolsSurveyQuestionDtos.values().stream()
				.filter(arg0 -> arg0.getParentQid() == libraryQuestion.getId()).collect(Collectors.toList());
		for (LimesurveyQuestionDto limesurveyQuestionDto : librarySurveyQuestionDtos) {
			questionManager.createOneQuestion(limesurveyQuestionDto.getTitle(), limesurveyQuestionDto.getSid(),
					limesurveyQuestionDto.getId(), libraryStudyVariable, limesurveyQuestionDto.getParentQid(),
					libraryQuestion.getTitle());
		}
	}
}
