package eu.planlos.anwesenheitsliste.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.ParticipantService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class ParticipantList {

	@Autowired
	private ParticipantService participantService;
	
	@RequestMapping(path = "/participantlist/{markedParticipantId}")
	public String markedParticipantList(Model model, @PathVariable Long markedParticipantId) {
		
		prepareContent(model, markedParticipantId);
		return "list/participantlist";		
	}
	
	@RequestMapping(path = "/participantlist")
	public String participantList(Model model) {
		
		prepareContent(model, null);
		return "list/participantlist";		
	}

	private void prepareContent(Model model, Long markedParticipantId) {
		
		List<String> headings = new ArrayList<>();	
		headings.add("#");
		headings.add("Vorname");
		headings.add("Nachname");
		headings.add("Telefon");
		headings.add("E-Mail");
		headings.add("Aktiv");
		headings.add("Funktionen");

		List<Participant> participants = participantService.findAll();
		
		model.addAttribute("headings", headings);
		model.addAttribute("participants", participants);
		model.addAttribute("markedParticipantId", markedParticipantId);
				
		GeneralAttributeCreator.create(model, "Teilnehmerverwaltung", "Liste der Teilnehmer");
	}
}
