package swimsEJB.constants;

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
	public static final String HARVESTING_WEBAPP_PATH = "/adquisicion-datos";
	/**
	 * 1.1. OAI RECORDS INCLUSION
	 */
	public static final String HARVESTING_OAI_RECORDS_INCLUSION_WEBAPP_PATH = HARVESTING_WEBAPP_PATH
			+ "/inclusion-registros-oai";
	// 1.1.1. OAI SET SELECTION
	public static final String HARVESTING_OAI_RECORDS_INCLUSION_SET_SELECTION_WEBAPP_PATH = HARVESTING_OAI_RECORDS_INCLUSION_WEBAPP_PATH
			+ "/seleccion-set";
	// 1.1.2. OAI RECORDS INCLUSION DATES SELECTION
	public static final String HARVESTING_OAI_RECORDS_INCLUSION_DATES_SELECTION_WEBAPP_PATH = HARVESTING_OAI_RECORDS_INCLUSION_WEBAPP_PATH
			+ "/seleccion-fechas";
	// 1.1.3. OAI RECORDS SELECTION
	public static final String HARVESTING_OAI_RECORDS_INCLUSION_RECORDS_SELECTION_WEBAPP_PATH = HARVESTING_OAI_RECORDS_INCLUSION_WEBAPP_PATH
			+ "/seleccion-registros";
	// 1.1.4. OAI RECORDS INCLUSION CONFIRMATION
	public static final String HARVESTING_OAI_RECORDS_INCLUSION_CONFIRMATION_WEBAPP_PATH = HARVESTING_OAI_RECORDS_INCLUSION_WEBAPP_PATH
			+ "/confirmacion";

	/**
	 * 1.2. OAI SETS MANAGEMENT
	 */
	public static final String HARVESTING_OAI_SETS_MANAGEMENT_WEBAPP_PATH = HARVESTING_WEBAPP_PATH
			+ "/administracion-sets-oai";

	/**
	 * 1.3. THESIS RECORD ASSIGNMENT
	 */
	public static final String HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH = HARVESTING_WEBAPP_PATH
			+ "/asignacion-registro-tesis";
	/**
	 * 1.3.1. USER SELECTION
	 */
	public static final String HARVESTING_THESIS_RECORD_ASSIGNMENT_USER_SELECTION_WEBAPP_PATH = HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH
			+ "/seleccion-usuario";
	/**
	 * 1.3.2. USER SELECTION
	 */
	public static final String HARVESTING_THESIS_RECORD_ASSIGNMENT_THESIS_SELECTION_WEBAPP_PATH = HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH
			+ "/seleccion-tesis";
	/**
	 * 1.3.4. SURVEYS SELECTION
	 */
	public static final String HARVESTING_THESIS_RECORD_ASSIGNMENT_SURVEYS_SELECTION_WEBAPP_PATH = HARVESTING_THESIS_RECORD_ASSIGNMENT_WEBAPP_PATH
			+ "/seleccion-encuestas";

	/**
	 * 1.4 THESIS RECORD DATA EXTRACTION AND DATA RECORDING
	 */
	public static final String HARVESTING_THESIS_RECORD_DATA_EXTRACTION_WEBAPP_PATH = HARVESTING_WEBAPP_PATH
			+ "/extraccion";

	/**
	 * 3. AUTH MODULE
	 */
	public static final String AUTH_WEBAPP_PATH = "/auth";
	/**
	 * 3.1. PERMISSION MANAGEMENT
	 */
	public static final String AUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH = AUTH_WEBAPP_PATH + "/administracion-permisos";
	/**
	 * 3.2 USER MANAGEMENT
	 */
	public static final String AUTH_USER_MANAGEMENT_WEBAPP_PATH = AUTH_WEBAPP_PATH + "/administracion-usuarios";
	/**
	 * 3.3 UPDATE PASSWORD
	 */
	public static final String AUTH_UPDATE_PASSWORD_WEBAPP_PATH = AUTH_WEBAPP_PATH + "/actualizar-contrasena";
}
