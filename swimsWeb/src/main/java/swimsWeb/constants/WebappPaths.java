package swimsWeb.constants;

import java.util.HashMap;

public final class WebappPaths {
	private WebappPaths() {
	}

	/**
	 * 0. INDEX
	 */
	public static final String INDEX_WEBAPP_PATH = "/index";
	/**
	 * 0.1 SIGN IN
	 */
	public static final String SIGN_IN_WEBAPP_PATH = "/iniciar-sesion";

	/**
	 * 1. HARVESTING MODULE
	 */
	public static final String HARVESTING_WEBAPP_PATH = "/recopilacion";
	/**
	 * 1.1. THESIS RECORDS INCLUSION
	 */
	public static final String HARVESTING_THESIS_RECORDS_INCLUSION_WEBAPP_PATH = HARVESTING_WEBAPP_PATH
			+ "/inclusion-tesis";
	// 1.1.1. THESIS SET SELECTION
	public static final String HARVESTING_THESIS_RECORDS_INCLUSION_SET_SELECTION_WEBAPP_PATH = HARVESTING_THESIS_RECORDS_INCLUSION_WEBAPP_PATH
			+ "/seleccion-set";
	// 1.1.2. THESIS RECORDS INCLUSION DATES SELECTION
	public static final String HARVESTING_THESIS_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH = HARVESTING_THESIS_RECORDS_INCLUSION_WEBAPP_PATH
			+ "/seleccion-fechas";
	// 1.1.3. THESIS RECORDS SELECTION
	public static final String HARVESTING_THESIS_RECORDS_INCLUSION_RECORDS_SELECTION_WEBAPP_PATH = HARVESTING_THESIS_RECORDS_INCLUSION_WEBAPP_PATH
			+ "/seleccion-registros";
	// 1.1.4. THESIS RECORDS INCLUSION CONFIRMATION
	public static final String HARVESTING_THESIS_RECORDS_INCLUSION_CONFIRMATION_WEBAPP_PATH = HARVESTING_THESIS_RECORDS_INCLUSION_WEBAPP_PATH
			+ "/confirmacion";

	/**
	 * 1.2. THESIS SETS MANAGEMENT
	 */
	public static final String HARVESTING_THESIS_SETS_MANAGEMENT_WEBAPP_PATH = HARVESTING_WEBAPP_PATH
			+ "/administracion-sets-tesis";

	/**
	 * 1.3. THESIS RECORD ASSIGNMENT
	 */
	public static final String HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH = HARVESTING_WEBAPP_PATH
			+ "/asignacion-tesis";
	/**
	 * 1.3.1. USER SELECTION
	 */
	public static final String HARVESTING_THESIS_RECORD_ASSIGNMENT_USER_SELECTION_WEBAPP_PATH = HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH
			+ "/seleccion-usuario";
	/**
	 * 1.3.2. USER SELECTION
	 */
	public static final String HARVESTING_THESIS_RECORD_ASSIGNMENT_THESIS_SELECTION_WEBAPP_PATH = HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH
			+ "/seleccion-registros";
	/**
	 * 1.3.4. SURVEYS SELECTION
	 */
	public static final String HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH = HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH
			+ "/seleccion-encuestas";
	/**
	 * 1.3.5. SURVEYS SELECTION
	 */
	public static final String HARVESTING_THESIS_RECORD_ASSIGNMENT_CONFIRMATION_WEBAPP_PATH = HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH
			+ "/confirmacion";

	/**
	 * 1.4 THESIS RECORD DATA COLLECTION AND DATA RECORDING
	 */
	public static final String HARVESTING_THESIS_RECORD_DATA_COLLECTION_WEBAPP_PATH = HARVESTING_WEBAPP_PATH
			+ "/datos-tesis";

	/**
	 * 1.5 THESIS RECORD MANAGEMENT
	 */
	public static final String HARVESTING_THESIS_RECORD_MANAGEMENT_WEBAPP_PATH = HARVESTING_WEBAPP_PATH
			+ "/gestion-registros-tesis";

	/**
	 * 1.6. STUDY VARIABLE MANAGEMENT
	 */
	public static final String HARVESTING_STUDY_VARIABLE_MANAGEMENT_WEBAPP_PATH = HARVESTING_WEBAPP_PATH
			+ "/administracion-variables-estudio";

	/**
	 * 1.7. LIMESURVEY QUESTION LINK
	 */
	public static final String HARVESTING_LIMESURVEY_QUESTION_LINK_WEBAPP_PATH = HARVESTING_WEBAPP_PATH
			+ "/enlace-preguntas-limesurvey";
	/**
	 * 1.7.1. SURVEY SELECTION
	 */
	public static final String HARVESTING_LIMESURVEY_QUESTION_LINK_SURVEY_SELECTION_WEBAPP_PATH = HARVESTING_LIMESURVEY_QUESTION_LINK_WEBAPP_PATH
			+ "/seleccion-encuesta";
	/**
	 * 1.7.2. STUDY VARIABLE SELECTION
	 */
	public static final String HARVESTING_LIMESURVEY_QUESTION_LINK_STUDY_VARIABLE_SELECTION_WEBAPP_PATH = HARVESTING_LIMESURVEY_QUESTION_LINK_WEBAPP_PATH
			+ "/seleccion-variable-estudio";
	/**
	 * 1.7.3. QUESTION SELECTION
	 */
	public static final String HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH = HARVESTING_LIMESURVEY_QUESTION_LINK_WEBAPP_PATH
			+ "/seleccion-preguntas";

	/**
	 * 3. AUTH MODULE
	 */
	public static final String AUTH_WEBAPP_PATH = "/auth";
	/**
	 * 3.1. PERMISSION MANAGEMENT
	 */
	public static final String AUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH = AUTH_WEBAPP_PATH + "/administracion-permisos";
	/**
	 * 3.2. USER MANAGEMENT
	 */
	public static final String AUTH_USER_MANAGEMENT_WEBAPP_PATH = AUTH_WEBAPP_PATH + "/administracion-usuarios";
	/**
	 * 3.3. UPDATE PASSWORD
	 */
	public static final String AUTH_UPDATE_PASSWORD_WEBAPP_PATH = AUTH_WEBAPP_PATH + "/actualizar-contrasena";
	/**
	 * 3.4. GROUP MANAGEMENT
	 */
	public static final String AUTH_GROUP_MANAGEMENT_WEBAPP_PATH = AUTH_WEBAPP_PATH + "/administracion-grupos";

	/**
	 * 
	 */
	public static HashMap<String, String> getAllWebappPathsAsMap() {
		HashMap<String, String> webAppPathsHashMap = new HashMap<>();

		webAppPathsHashMap.put("INDEX_WEBAPP_PATH", INDEX_WEBAPP_PATH);
		webAppPathsHashMap.put("SIGN_IN_WEBAPP_PATH", SIGN_IN_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_WEBAPP_PATH", HARVESTING_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_THESIS_RECORDS_INCLUSION_WEBAPP_PATH",
				HARVESTING_THESIS_RECORDS_INCLUSION_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_THESIS_RECORDS_INCLUSION_SET_SELECTION_WEBAPP_PATH",
				HARVESTING_THESIS_RECORDS_INCLUSION_SET_SELECTION_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_THESIS_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH",
				HARVESTING_THESIS_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_THESIS_RECORDS_INCLUSION_RECORDS_SELECTION_WEBAPP_PATH",
				HARVESTING_THESIS_RECORDS_INCLUSION_RECORDS_SELECTION_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_THESIS_RECORDS_INCLUSION_CONFIRMATION_WEBAPP_PATH",
				HARVESTING_THESIS_RECORDS_INCLUSION_CONFIRMATION_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_THESIS_SETS_MANAGEMENT_WEBAPP_PATH",
				HARVESTING_THESIS_SETS_MANAGEMENT_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH",
				HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_THESIS_RECORD_ASSIGNMENT_USER_SELECTION_WEBAPP_PATH",
				HARVESTING_THESIS_RECORD_ASSIGNMENT_USER_SELECTION_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_THESIS_RECORD_ASSIGNMENT_THESIS_SELECTION_WEBAPP_PATH",
				HARVESTING_THESIS_RECORD_ASSIGNMENT_THESIS_SELECTION_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH",
				HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_THESIS_RECORD_ASSIGNMENT_CONFIRMATION_WEBAPP_PATH",
				HARVESTING_THESIS_RECORD_ASSIGNMENT_CONFIRMATION_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_THESIS_RECORD_DATA_COLLECTION_WEBAPP_PATH",
				HARVESTING_THESIS_RECORD_DATA_COLLECTION_WEBAPP_PATH);
		webAppPathsHashMap.put("AUTH_WEBAPP_PATH", AUTH_WEBAPP_PATH);
		webAppPathsHashMap.put("AUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH", AUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH);
		webAppPathsHashMap.put("AUTH_USER_MANAGEMENT_WEBAPP_PATH", AUTH_USER_MANAGEMENT_WEBAPP_PATH);
		webAppPathsHashMap.put("AUTH_UPDATE_PASSWORD_WEBAPP_PATH", AUTH_UPDATE_PASSWORD_WEBAPP_PATH);
		webAppPathsHashMap.put("AUTH_GROUP_MANAGEMENT_WEBAPP_PATH", AUTH_GROUP_MANAGEMENT_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_THESIS_RECORD_MANAGEMENT_WEBAPP_PATH",
				HARVESTING_THESIS_RECORD_MANAGEMENT_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_STUDY_VARIABLE_MANAGEMENT_WEBAPP_PATH",
				HARVESTING_STUDY_VARIABLE_MANAGEMENT_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_LIMESURVEY_QUESTION_LINK_WEBAPP_PATH",
				HARVESTING_LIMESURVEY_QUESTION_LINK_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_LIMESURVEY_QUESTION_LINK_SURVEY_SELECTION_WEBAPP_PATH",
				HARVESTING_LIMESURVEY_QUESTION_LINK_SURVEY_SELECTION_WEBAPP_PATH);
		webAppPathsHashMap.put("HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH",
				HARVESTING_LIMESURVEY_QUESTION_LINK_QUESTION_SELECTION_WEBAPP_PATH);

		return webAppPathsHashMap;
	}
}
