package eu.planlos.anwesenheitsliste;

public class ApplicationPath {

	// Global Start delimeter
	public static final String DELIMETER = "/";
	
	
	
	/*
	 * ADMIN AREA
	 */
	// Web paths
	public static final String URL_AREA_ADMIN = "/admin";
	
	public static final String URL_TEAMLISTFULL = URL_AREA_ADMIN + DELIMETER + "teamlist" + DELIMETER;
	public static final String URL_MEETINGLISTFULL = URL_AREA_ADMIN + DELIMETER + "meetinglist" + DELIMETER;
	public static final String URL_PARTICIPANTLISTFULL = URL_AREA_ADMIN + DELIMETER + "participantlist" + DELIMETER;

	public static final String URL_PERMISSIONSOVERVIEW = URL_AREA_ADMIN + DELIMETER + "permissionoverview";
	public static final String URL_PARTICIPATIONOVERVIEW = URL_AREA_ADMIN + DELIMETER + "participationoverview";
	
	public static final String URL_USER = URL_AREA_ADMIN + DELIMETER + "userdetail" + DELIMETER;
	public static final String URL_USERLIST = URL_AREA_ADMIN + DELIMETER + "userlist" + DELIMETER;
	
	// Ressource paths
	public static final String RES_PERMISSIONSOVERVIEW = "overview/permissionoverview";
	public static final String RES_PARTICIPATIONOVERVIEW = "overview/participationoverview";

	public static final String RES_USER = "detail/userdetail";
	public static final String RES_USERLIST = "list/userlist";
	

	
	/*
	 * DEV AREA
	 */
	// Web paths
	public static final String URL_AREA_DEV = "/dev";
	
	public static final String URL_FA_TEST = URL_AREA_DEV + DELIMETER + "fontawesome";
	public static final String URL_BS_TEST = URL_AREA_DEV + DELIMETER + "bootstrap";
	public static final String URL_403_TEST = URL_AREA_DEV + DELIMETER + "always403";
	public static final String URL_404_TEST = URL_AREA_DEV + DELIMETER + "always404";
	public static final String URL_500_TEST = URL_AREA_DEV + DELIMETER + "always500";

	// Ressource paths
	public static final String RES_FA_TEST = "dev/fontawesome";
	public static final String RES_BS_TEST = "dev/bootstrap";
	public static final String RES_403_TEST = "dev/always403";
	public static final String RES_500_TEST = "dev/always500";
	
	
	
	/*
	 * USER AREA
	 */
	
	// Web paths
	public static final String URL_AREA_USER = "/user";
	
	public static final String URL_MEETING = URL_AREA_USER + DELIMETER + "meetingdetail" + DELIMETER;
	public static final String URL_MEETINGFORTEAM = URL_MEETING + "forteam" + DELIMETER;
	public static final String URL_MEETINGCHOOSETEAM = URL_MEETING + "chooseteam" + DELIMETER;
	public static final String URL_MEETINGADDPARTICIPANTS = URL_MEETINGCHOOSETEAM + "addparticipants";
	public static final String URL_MEETINGSUBMIT = URL_MEETING + "submit";
	public static final String URL_MEETINGLIST = URL_AREA_USER + DELIMETER + "meetinglist" + DELIMETER;
	
	public static final String URL_PARTICIPANT = URL_AREA_USER + DELIMETER + "participantdetail" + DELIMETER;
	public static final String URL_PARTICIPANTLIST = URL_AREA_USER + DELIMETER + "participantlist" + DELIMETER;

	public static final String URL_TEAM = URL_AREA_USER + DELIMETER + "teamdetail" + DELIMETER;
	public static final String URL_TEAMLIST = URL_AREA_USER + DELIMETER + "teamlist" + DELIMETER;
	public static final String URL_TEAMPHONELIST = URL_AREA_USER + DELIMETER + "teamphonelist" + DELIMETER;

	public static final String URL_CONFIG = URL_AREA_USER + DELIMETER + "config";
	public static final String URL_ACTIVITIES = URL_AREA_USER + DELIMETER + "activities";
	
	public static final String URL_LOGOUT = URL_AREA_USER + DELIMETER + "logout";
	public static final String URL_LOGIN = "/login_handler";
	public static final String URL_LOGIN_FORM = "/login_form";
	public static final String URL_LOGIN_FORM_ERROR = URL_LOGIN_FORM + "?error=true";
	
	// Ressource paths
	public static final String RES_MEETING = "detail/meetingdetail";
	public static final String RES_MEETINGLIST = "list/meetinglist";
	
	public static final String RES_PARTICIPANT = "detail/participantdetail";
	public static final String RES_PARTICIPANTLIST = "list/participantlist";
	
	public static final String RES_TEAM = "detail/teamdetail";
	public static final String RES_TEAMLIST = "list/teamlist";
	public static final String RES_TEAMPHONELIST = "list/teamphonelist";

	public static final String RES_LOGIN = "login";
	
	
	
	/*
	 * ANONYMOUS AREA
	 */
	
	// Web paths
	public static final String URL_AREA_ANONYMOUS = "/";
	
	public static final String URL_HOME = URL_AREA_ANONYMOUS;
	public static final String URL_IMPRESSUM = URL_AREA_ANONYMOUS + "impressum";
	public static final String URL_DATENSCHUTZ = URL_AREA_ANONYMOUS + "datenschutz";
	public static final String URL_ERROR_403 = URL_AREA_ANONYMOUS + "403";
	public static final String URL_ERROR_DEFAULT = URL_AREA_ANONYMOUS + "error";

	// Ressource paths
	public static final String RES_HOME = "home";
	public static final String RES_IMPRESSUM = "impressum";
	public static final String RES_DATENSCHUTZ = "datenschutz";
	public static final String RES_ERROR_403 = "error_403";
	public static final String RES_ERROR_UNKNOWN= "error_unknown";
	
	
	
	/*
	 * ACTUATOR AREA
	 */
	
	// Web paths
	public static final String URL_AREA_ACTUATOR = "/actuator";
}
