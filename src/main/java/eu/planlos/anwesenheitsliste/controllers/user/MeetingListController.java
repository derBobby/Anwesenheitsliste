package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.DELIMETER;
import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_MEETINGLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_403;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGCHOOSETEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGFORTEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGLISTFULL;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String meetingListForTeam(Model model, Authentication auth, HttpSession session, @PathVariable Long idTeam) {
			
		if(isNotAuthorizedForTeam(session, idTeam)) {
			logger.error("Benutzer \"" + securityService.getLoginName(session) + "\" hat unauthorisiert versucht auf Terminliste von Gruppe id=" + idTeam + " zuzugreifen.");
			return "redirect:" + URL_ERROR_403;
		}
		
		prepareContentForTeam(model, auth, idTeam);
		return RES_MEETINGLIST;
	}

	// User
	@RequestMapping(path = URL_MEETINGLIST + "{idTeam}" + DELIMETER + "{idMeeting}")
	public String meetingListForTeamMarked(Model model, Authentication auth, HttpSession session, @PathVariable Long idTeam, @PathVariable Long idMeeting) {

		model.addAttribute("markedMeetingId", idMeeting);
		return meetingListForTeam(model, auth, session, idTeam);
	}
	
	// Admin
	@RequestMapping(path = URL_MEETINGLISTFULL)
	public String meetingListFull(Model model, Authentication auth) {
		
		model.addAttribute("functionEdit", URL_MEETINGFORTEAM);
		model.addAttribute("functionAdd", URL_MEETINGCHOOSETEAM);
		
		prepareContent(model, auth, null);
		
		return RES_MEETINGLIST;
	}
	
	/*
	 * CONTENT PREPARATION
	 */
	private void prepareContentForTeam(Model model, Authentication auth, Long idTeam) {
		
		model.addAttribute("idTeam", idTeam);
		
		model.addAttribute("functionEdit", URL_MEETINGFORTEAM);
		model.addAttribute("functionAdd", URL_MEETINGFORTEAM);
		
		prepareContent(model, auth, idTeam);
	}

	private void prepareContent(Model model, Authentication auth, Long idTeam) {

		model.addAttribute("headings", getHeadingsForTeam(idTeam));
		model.addAttribute("meetings", getMeetingsForTeam(idTeam));
		
		bf.fill(model, auth, STR_MODULE, STR_TITLE);
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

	private boolean isNotAuthorizedForTeam(HttpSession session, Long idTeam) {
		return !securityService.isUserAuthorizedForTeam(session, idTeam);
	}
}
