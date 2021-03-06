package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.DELIMETER;
import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_MEETING;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_403;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_HOME;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGADDPARTICIPANTS;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGCHOOSETEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGFORTEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGLISTFULL;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGSUBMIT;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.planlos.anwesenheitsliste.model.Meeting;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.MeetingService;
import eu.planlos.anwesenheitsliste.service.ParticipantService;
import eu.planlos.anwesenheitsliste.service.SecurityService;
import eu.planlos.anwesenheitsliste.service.TeamService;

//TODO may be simplified
@Controller
public class MeetingDetailController {

	public final String STR_MODULE = "Terminverwaltung";
	public final String STR_TITLE_ADD_MEETING = "Termin hinzufügen";
	public final String STR_TITLE_EDIT_MEETING = "Termin ändern";

	private static final Logger logger = LoggerFactory.getLogger(MeetingDetailController.class);
			
	@Autowired
	private BodyFillerService bf;
	
	@Autowired
	private MeetingService meetingService;

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private ParticipantService participantService;
	
	// For editing a given meeting for given team
	@RequestMapping(path = URL_MEETINGFORTEAM + "{idTeam}/{idMeeting}", method = RequestMethod.GET)
	public String edit(Model model, Authentication auth, HttpSession session, @PathVariable Long idTeam, @PathVariable Long idMeeting) {
	
		if(isNotAuthorizedForTeam(session, idTeam)) {
			logger.error("Benutzer \"" + securityService.getLoginName(session) + "\" hat unauthorisiert versucht auf Termin id=" + idMeeting + "von Gruppe id=" + idTeam + " zuzugreifen.");
			return "redirect:" + URL_ERROR_403;
		}
		
		/*
		 * LOGIC
		 */
		//TODO only authorization for team is tested, but not for meeting. What??
		//TODO idTeam even necessary here?
		Meeting meeting = meetingService.loadMeeting(idMeeting);
		model.addAttribute(meeting);
		prepareContent(model, auth, meeting);
		
		prepareContentWithTeam(model, idTeam);
		
		return RES_MEETING;
	}

	// For adding new meeting for given team
	@RequestMapping(path = URL_MEETINGFORTEAM + "{idTeam}", method = RequestMethod.GET)
	public String addForTeam(Model model,  Authentication auth, HttpSession session, @PathVariable Long idTeam) {

		if(isNotAuthorizedForTeam(session, idTeam)) {
			logger.error("Benutzer \"" + securityService.getLoginName(session) + "\" hat unauthorisiert versucht einen Termin für Gruppe id=" + idTeam + " anzulegen.");
			return "redirect:" + URL_ERROR_403;
		}
		
		/*
		 * LOGIC
		 */
		Meeting meeting = new Meeting();
		meeting.setMeetingDate(today());
		model.addAttribute(meeting);
		prepareContent(model, auth, meeting);
		
		prepareContentWithTeam(model, idTeam);

		return RES_MEETING;
	}

	// STEP 1 adding new meeting without a given team
	// -> View for choosing team
	@RequestMapping(path = URL_MEETINGCHOOSETEAM, method = RequestMethod.GET)
	public String addWithoutTeam(Model model, Authentication auth, HttpSession session) {
		
		Meeting meeting = new Meeting();
		meeting.setMeetingDate(today());
		model.addAttribute(meeting);
		prepareContent(model, auth, meeting);

		prepareContentWithoutTeam(model, securityService.getLoginName(session), securityService.isAdmin(session));
		
		return RES_MEETING;
	}

	private Date today() {
		return Calendar.getInstance().getTime();
	}

	// STEP 2 adding new meeting without a given team
	// -> View with participants for chosen team
	@RequestMapping(path = URL_MEETINGADDPARTICIPANTS, method = RequestMethod.POST)
	public String addWithoutTeam(Model model, Authentication auth, HttpSession session, @Valid @ModelAttribute Meeting meeting, Errors errors) {
		
		String loginName = securityService.getLoginName(session);
		
		if(errors.hasErrors()) {
			logger.error("Validierungsfehler in Schritt zwei beim Anlegen ohne Team von Benutzer \"" + loginName + "\".");
			handleValidationErrors(model, auth, meeting, loginName, securityService.isAdmin(session));
			return RES_MEETING; 
		}
		
		// Team not null was already validated
		if(isNotAuthorizedForTeam(session, meeting.getTeam().getIdTeam())) {
			logger.error("Benutzer \"" + loginName + "\" hat unauthorisiert versucht einen Termin für Gruppe id=" + meeting.getTeam().getIdTeam() + " anzulegen.");
			return "redirect:" + URL_ERROR_403;
		}
		
		prepareContent(model, auth, meeting);
		prepareContentWithTeam(model, meeting.getTeam().getIdTeam());
		
		return RES_MEETING; 
	}

	// For submitting the added/edited meeting
	@RequestMapping(path = URL_MEETINGSUBMIT, method = RequestMethod.POST)
	public String submit(Model model, Authentication auth, HttpSession session, @Valid @ModelAttribute Meeting meeting, Errors errors) {
		
		String loginName = securityService.getLoginName(session);
		
		if(errors.hasErrors()) {
			logger.error("Validierungsfehler beim Submit eines Termins von Benutzer \"" + loginName + "\".");
			handleValidationErrors(model, auth, meeting, loginName, securityService.isAdmin(session));
			return RES_MEETING; 
		}

		// Team not null was already validated
		if(isNotAuthorizedForTeam(session, meeting.getTeam().getIdTeam())) {
			logger.error("Benutzer \"" + loginName + "\" hat unauthorisiert versucht einen Termin für Gruppe id=" + meeting.getTeam().getIdTeam() + " zu speichern.");
			return "redirect:" + URL_ERROR_403;
		}

		/*
		 * LOGIC
		 */
		// Meeting owns relationship so Participants must be corrected before saving meeting
		meetingService.correctInactiveParticipants(meeting);		
		meeting = meetingService.saveMeeting(meeting);

		return "redirect:" + URL_MEETINGLIST + meeting.getTeam().getIdTeam() + DELIMETER + meeting.getIdMeeting();
	}
	
	private void handleValidationErrors(Model model, Authentication auth, Meeting meeting, String loginName, boolean isAdmin) {
		
		prepareContent(model, auth, meeting);

		if(meeting.getTeam() != null) {
			prepareContentWithTeam(model, meeting.getTeam().getIdTeam());
		} else {
			prepareContentWithoutTeam(model, loginName, isAdmin);
		}
	}

	private void prepareContent(Model model, Authentication auth, Meeting meeting) {
		
		if(meeting.getIdMeeting() != null) {
			bf.fill(model, auth, STR_MODULE, STR_TITLE_EDIT_MEETING);
		} else {
			bf.fill(model, auth, STR_MODULE, STR_TITLE_ADD_MEETING);
		}
	}
	
	private void prepareContentWithoutTeam(Model model, String loginName, boolean isAdmin) {
		
		model.addAttribute("teams", teamService.loadTeamsForUser(loginName));
		model.addAttribute("formAction", URL_MEETINGADDPARTICIPANTS);

		if(isAdmin) {
			model.addAttribute("formCancel", URL_MEETINGLISTFULL);
			return;
		}
		model.addAttribute("formCancel", URL_HOME);
	}
	
	private void prepareContentWithTeam(Model model, Long idTeam) {
		
		Team team = teamService.loadTeam(idTeam);
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		
		model.addAttribute("teams", teams);
		model.addAttribute("participants", participantService.loadParticipantsForTeam(idTeam));
		model.addAttribute("formAction", URL_MEETINGSUBMIT);
		model.addAttribute("formCancel", URL_MEETINGLIST + idTeam);
	}

	private boolean isNotAuthorizedForTeam(HttpSession session, Long idTeam) {
		return !securityService.isUserAuthorizedForTeam(session, idTeam);
	}
}
