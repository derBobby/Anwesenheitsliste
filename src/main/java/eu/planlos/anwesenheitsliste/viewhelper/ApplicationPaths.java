package eu.planlos.anwesenheitsliste.viewhelper;

public class ApplicationPaths {

	// Global Start delimeter
	public static final String DELIMETER = "/";
	
	
	
	/*
	 * ADMIN AREA
	 */
	
	// Web paths
	public static final String URL_AREA_ADMIN = "/admin";
	
	public static final String URL_PERMISSIONSOVERVIEW = URL_AREA_ADMIN + DELIMETER + "permissionoverview";
	public static final String URL_PARTICIPATIONOVERVIEW = URL_AREA_ADMIN + DELIMETER + "participationoverview";
	
	public static final String URL_USER = URL_AREA_ADMIN + DELIMETER + "userdetail" + DELIMETER;
	public static final String URL_USERLIST = URL_AREA_ADMIN + DELIMETER + "userlist" + DELIMETER;
	
	public static final String URL_FA_TEST = URL_AREA_ADMIN + DELIMETER + "fontawesome";
	
	// Ressource paths
	public static final String RES_PERMISSIONSOVERVIEW = "overview/permissionoverview";
	public static final String RES_PARTICIPATIONOVERVIEW = "overview/participationoverview";

	public static final String RES_USER = "detail/userdetail";
	public static final String RES_USERLIST = "list/userlist";
	
	public static final String RES_FA_TEST = "test/fontawesome";
	
	
	
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
	public static final String URL_MEETINGLISTFULL = URL_MEETINGLIST + "full";
	
	public static final String URL_PARTICIPANT = URL_AREA_USER + DELIMETER + "participantdetail" + DELIMETER;
	public static final String URL_PARTICIPANTLIST = URL_AREA_USER + DELIMETER + "participantlist" + DELIMETER;

	public static final String URL_TEAM = URL_AREA_USER + DELIMETER + "teamdetail" + DELIMETER;
	public static final String URL_TEAMLIST = URL_AREA_USER + DELIMETER + "teamlist" + DELIMETER;
	public static final String URL_TEAMPHONELIST = URL_AREA_USER + DELIMETER + "teamphonelist" + DELIMETER;
	
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

	public static final String RES_LOGIN = "session/login"; //TODO
	
	
	
	/*
	 * ANONYMOUS AREA
	 */
	
	// Web paths
	public static final String URL_AREA_ANONYMOUS = "/";
	
	public static final String URL_HOME = URL_AREA_ANONYMOUS;
	public static final String URL_ABOUT = URL_AREA_ANONYMOUS + "about";
	public static final String URL_PRIVACY = URL_AREA_ANONYMOUS + "datenschutz";
	public static final String URL_403 = URL_AREA_ANONYMOUS + "403";

	// Ressource paths
	public static final String RES_HOME = "home";
	public static final String RES_ABOUT = "about";
	public static final String RES_PRIVACY = "datenschutz";
	
	public static final String RES_403 = "403"; //TODO
	
	/*
	 * ACTUATOR AREA
	 */
	
	// Web paths
	public static final String URL_AREA_ACTUATOR = "/actuator";
}
