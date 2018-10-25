package eu.planlos.anwesenheitsliste.controllers.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.model.Meeting;
import eu.planlos.anwesenheitsliste.model.MeetingService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_MEETINGLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_MEETINGFORTEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_MEETINGCHOOSETEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_MEETINGLISTFULL;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.DELIMETER;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_MEETINGLIST;

@Controller
public class MeetingList {

	public final String STR_MODULE = "Terminverwaltung";
	public final String STR_TITLE = "Liste der Termine";
	
	
	@Autowired
	private MeetingService meetingService;
	
	@RequestMapping(path = URL_MEETINGLIST + "{idTeam}")
	public String meetingListForTeam(Model model, @PathVariable Long idTeam) {
			
		prepareContentForTeam(model, idTeam);
		return RES_MEETINGLIST;
	}
	
	@RequestMapping(path = URL_MEETINGLIST + "{idTeam}" + DELIMETER + "{idMeeting}")
	public String meetingListForTeamMarked(Model model, @PathVariable Long idTeam, @PathVariable Long idMeeting) {
		
		model.addAttribute("markedMeetingId", idMeeting);
		prepareContentForTeam(model, idTeam);
		return RES_MEETINGLIST;
	}
	
	@RequestMapping(path = URL_MEETINGLISTFULL)
	public String meetingListFull(Model model) {
		

		model.addAttribute("functionEdit", URL_MEETINGCHOOSETEAM);
		model.addAttribute("functionAdd", URL_MEETINGCHOOSETEAM);
		
		prepareContent(model, null);
		
		return RES_MEETINGLIST;
	}
	
	// CONTENT PREPARATION
	
	private void prepareContentForTeam(Model model, Long idTeam) {
		
		model.addAttribute("idTeam", idTeam);
		
		model.addAttribute("functionEdit", URL_MEETINGFORTEAM);
		model.addAttribute("functionAdd", URL_MEETINGFORTEAM);
		
		prepareContent(model, idTeam);
	}

	private void prepareContent(Model model, Long idTeam) {

		model.addAttribute("headings", getHeadingsForTeam(idTeam));
		model.addAttribute("meetings", getMeetingsForTeam(idTeam));
		
		GeneralAttributeCreator.create(model, STR_MODULE, STR_TITLE);
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
			return meetingService.findAll();
		}
		return meetingService.findAllByTeam(idTeam);
	}
}
