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
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_MEETING;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_MEETINGLISTFULL;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_MEETINGLIST;

@Controller
public class MeetingList {

	@Autowired
	private MeetingService meetingService;
	
	@RequestMapping(path = URL_MEETINGLIST + "{idTeam}")
	public String meetingListForTeam(Model model, @PathVariable Long idTeam) {
			
		prepareContentForTeam(model, idTeam);
		return RES_MEETINGLIST;
	}
	
	@RequestMapping(path = URL_MEETINGLIST + "{idTeam}/{idMeeting}")
	public String meetingListForTeamMarked(Model model, @PathVariable Long idTeam, @PathVariable Long idMeeting) {
		
		model.addAttribute("markedMeetingId", idMeeting);
		prepareContentForTeam(model, idTeam);
		return RES_MEETINGLIST;
	}
	
	@RequestMapping(path = URL_MEETINGLISTFULL)
	public String meetingListFull(Model model) {
		
		prepareContent(model, null);
		return RES_MEETINGLIST;
	}
	
	// CONTENT PREPARATION
	
	private void prepareContentForTeam(Model model, Long idTeam) {
		
		model.addAttribute("idTeam", idTeam);
		prepareContent(model, idTeam);
	}

	private void prepareContent(Model model, Long idTeam) {

		model.addAttribute("headings", getHeadingsForTeam(idTeam));
		model.addAttribute("meetings", getMeetingsForTeam(idTeam));

		model.addAttribute("functionEdit", URL_MEETING);
		model.addAttribute("functionAdd", URL_MEETING);
		
		GeneralAttributeCreator.create(model, "Terminverwaltung", "Liste der Termine");
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
