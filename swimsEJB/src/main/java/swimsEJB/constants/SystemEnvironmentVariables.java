package swimsEJB.constants;

public final class SystemEnvironmentVariables {
	private SystemEnvironmentVariables() {
	}

	public static final String LIMESURVEY_BASE_URL = System.getenv().getOrDefault("LIMESURVEY_BASE_URL", "");
	public static final Boolean LIMESURVE_ON_SAME_NETWORK = System.getenv("LIMESURVE_ON_SAME_NETWORK").equals("true")
			? true
			: false;
	public static final String LIMESURVEY_HOST = System.getenv("LIMESURVEY_HOST");
	public static final String LIMESURVEY_PUBLIC_HOST = System.getenv("LIMESURVEY_PUBLIC_HOST");
	public static final String LIMESURVEY_PUBLIC_PORT = System.getenv("LIMESURVEY_PUBLIC_PORT");
	public static final String LIMESURVEY_ADMIN_USER = System.getenv().getOrDefault("LIMESURVEY_ADMIN_USER", "admin");
	public static final String LIMESURVEY_FIRST_ADMIN_PASSWORD = System.getenv("LIMESURVEY_FIRST_ADMIN_PASSWORD");
	public static final String LIMESURVEY_PORT = System.getenv().getOrDefault("LIMESURVEY_PORT", "8080");
	public static final Boolean LIMESURVEY_USES_SSL = System.getenv("LIMESURVEY_USES_SSL").equals("true") ? true
			: false;
	public static final Boolean LIMESURVEY_REMOTECONTROL_USES_SSL = System.getenv("LIMESURVEY_REMOTECONTROL_USES_SSL")
			.equals("true") ? true : false;

	public static final String LIMESURVEY_REMOTECONTROL_PROTOCOL = LIMESURVEY_REMOTECONTROL_USES_SSL ? "https://"
			: "http://";
	public static final String LIMESURVEY_PROTOCOL = LIMESURVEY_USES_SSL ? "https://" : "http://";
}
