package swimsEJB.constants;

public final class WebappPaths {
	private WebappPaths() {
	}
	
	/**
	 * 0. INDEX
	 */
	public static final String INDEX_WEBAPP_PATH = "/index";

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
	 * 3. AUTH MODULE
	 */
	public static final String AUTH_WEBAPP_PATH = "/auth";
	/**
	 * 3.1. PERMISSION MANAGEMENT
	 */
	public static final String AUTH_PERMISSION_MANAGEMENT_WEBAPP_PATH = AUTH_WEBAPP_PATH + "/administracion-permisos";
}
