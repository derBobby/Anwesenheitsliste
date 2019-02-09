package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.DELIMETER;
import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_MEETINGLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGCHOOSETEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGFORTEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGLISTFULL;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_403;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.SessionAttributes;
import eu.planlos.anwesenheitsliste.model.Meeting;
import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.MeetingService;
import eu.planlos.anwesenheitsliste.service.SecurityService;

@Controller
public class MeetingListController {

	public final String STR_MODULE = "Terminverwaltung";
	public final String STR_TITLE = "Liste der Termine";

	private static final Logger logger = LoggerFactory.getLogger(MeetingListController.class);

	@Autowired
	private BodyFillerService bf;

	@Autowired
	private MeetingService meetingService;
	
	@Autowired
	private SecurityService securityService;
	
	// User
	@RequestMapping(path = URL_MEETINGLIST + "{idTeam}")
	public String meetingListForTeam(Model model, Principal principal, Session session, @PathVariable Long idTeam) {
		
		boolean isAdmin = session.getAttribute(SessionAttributes.ISADMIN);
		
		if(!isAdmin && !hasPermissionForTeam(idTeam, principal.getName())) {
			logger.error("Benutzer \"" + principal.getName() + "\" hat unauthorisiert versucht auf Terminliste von Gruppe id=" + idTeam + " zuzugreifen.");
			return "redirect:" + URL_ERROR_403;
		}
		
		prepareContentForTeam(model, idTeam);
		return RES_MEETINGLIST;
	}

	// User
	@RequestMapping(path = URL_MEETINGLIST + "{idTeam}" + DELIMETER + "{idMeeting}")
	public String meetingListForTeamMarked(Model model, Principal principal, Session session, @PathVariable Long idTeam, @PathVariable Long idMeeting) {

		model.addAttribute("markedMeetingId", idMeeting);
		return meetingListForTeam(model, principal, session, idTeam);
	}
	
	// Admin
	@RequestMapping(path = URL_MEETINGLISTFULL)
	public String meetingListFull(Model model) {
		
		model.addAttribute("functionEdit", URL_MEETINGFORTEAM);
		model.addAttribute("functionAdd", URL_MEETINGCHOOSETEAM);
		
		prepareContent(model, null);
		
		return RES_MEETINGLIST;
	}
	
	/*
	 * CONTENT PREPARATION
	 */
	
	private void prepareContentForTeam(Model model, Long idTeam) {
		
		model.addAttribute("idTeam", idTeam);
		
		model.addAttribute("functionEdit", URL_MEETINGFORTEAM);
		model.addAttribute("functionAdd", URL_MEETINGFORTEAM);
		
		prepareContent(model, idTeam);
	}

	private void prepareContent(Model model, Long idTeam) {

		model.addAttribute("headings", getHeadingsForTeam(idTeam));
		model.addAttribute("meetings", getMeetingsForTeam(idTeam));
		
		bf.fill(model, STR_MODULE, STR_TITLE);
	}
	
	private List<String> getHeadingsForTeam(Long idTeam) {
		
		List<String> headings = new ArrayList<String>();	
		headings.add("#");
		headings.add("Datum");

		if(idTeam == null) {
			headings.add("Gruppe");
		}

		headings.add("Beschreibung");
		headings.add("Funktionen");
		return headings;
	}
	
	private List<Meeting> getMeetingsForTeam(Long idTeam) {

		if(idTeam == null) {
			return meetingService.loadAllMeetings();
		}
		return meetingService.loadMeetingsForTeam(idTeam);
	}
	
	private boolean hasPermissionForTeam(long idTeam, String loginName) {
		return securityService.isUserAuthorizedForTeam(idTeam, loginName);
	}
}