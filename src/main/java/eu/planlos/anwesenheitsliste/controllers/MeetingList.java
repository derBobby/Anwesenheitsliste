package eu.planlos.anwesenheitsliste.controllers;

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

@Controller
public class MeetingList {

	@Autowired
	private MeetingService meetingService;
	
	@RequestMapping(path = "/meetinglist/forteam/{idTeam}")
	public String meetingListForTeam(Model model, @PathVariable Long idTeam) {
			
		prepareContentForTeam(model, idTeam);
		return "list/meetinglist";
	}
	
	@RequestMapping(path = "/meetinglist/forteam/{idTeam}/marked/{idMeeting}")
	public String meetingListForTeamMarked(Model model, @PathVariable Long idTeam, @PathVariable Long idMeeting) {
		
		model.addAttribute("markedMeetingId", idMeeting);
		prepareContentForTeam(model, idTeam);
		return "list/meetinglist";
	}
	
	@RequestMapping(path = "/meetinglist/full")
	public String meetingListFull(Model model) {
		
		model.addAttribute("newButtonUrl", "meetingadd/chooseteam");
		prepareContent(model, null);
		return "list/meetinglist";
	}
	
	// CONTENT PREPARATION
	
	private void prepareContentForTeam(Model model, Long idTeam) {
		
		model.addAttribute("newButtonUrl", "meetingadd/forteam/" + idTeam);
		prepareContent(model, idTeam);
	}

	private void prepareContent(Model model, Long idTeam) {

		model.addAttribute("headings", getHeadingsForTeam(idTeam));
		model.addAttribute("meetings", getMeetingsForTeam(idTeam));
		model.addAttribute("newButtonText", "Neuer Termin");
		
		GeneralAttributeCreator generalAttributeCreator = new GeneralAttributeCreator();
		generalAttributeCreator.create(model, "Terminverwaltung", "Liste der Termine");
	}
	
	private List<Meeting> getMeetingsForTeam(Long idTeam) {

		if(idTeam == null) {
			return meetingService.findAll();
		}
		return meetingService.findAllByTeam(idTeam);
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
}
