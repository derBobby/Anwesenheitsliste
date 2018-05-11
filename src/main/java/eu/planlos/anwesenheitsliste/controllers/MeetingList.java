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
	
	@RequestMapping(path = "/meetinglist/{idMeeting}")
	public String markedMeetingList(Model model, @PathVariable Long idMeeting) {
		
		prepareContent(model, idMeeting);
		return "list/meetinglist";
	}
	
	@RequestMapping(path = "/meetinglist")
	public String meetingList(Model model) {
		
		prepareContent(model, null);
		return "list/meetinglist";
	}

	private void prepareContent(Model model, Long markedMeetingId) {
		
		List<String> headings = new ArrayList<String>();	
		headings.add("#");
		headings.add("Datum");
		//TODO Add group when no filter an group
		headings.add("Beschreibung");
		headings.add("Funktionen");
				
		List<Meeting> meetings = meetingService.findAll();

		model.addAttribute("headings", headings);
		model.addAttribute("meetings", meetings);
		model.addAttribute("markedTeamId", markedMeetingId);
		model.addAttribute("newButtonText", "Neuer Termin");
		model.addAttribute("newButtonUrl", "meetingadd");

		GeneralAttributeCreator generalAttributeCreator = new GeneralAttributeCreator();
		generalAttributeCreator.create(model, "Terminverwaltung", "Liste der Termine");
	}
}
