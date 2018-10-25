package eu.planlos.anwesenheitsliste.controllers.user;

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

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_PARTICIPANTLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_PARTICIPANT;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_PARTICIPANTLIST;

@Controller
public class ParticipantList {

	public final String STR_MODULE = "Teilnehmerverwaltung";
	public final String STR_TITLE = "Liste der Teilnehmer";
	
	@Autowired
	private ParticipantService participantService;
	
	@RequestMapping(path = URL_PARTICIPANTLIST + "{markedParticipantId}")
	public String markedParticipantList(Model model, @PathVariable Long markedParticipantId) {
		
		prepareContent(model, markedParticipantId);
		return RES_PARTICIPANTLIST;		
	}
	
	@RequestMapping(path = URL_PARTICIPANTLIST)
	public String participantList(Model model) {
		
		prepareContent(model, null);
		return RES_PARTICIPANTLIST;		
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
			
		model.addAttribute("functionEdit", URL_PARTICIPANT);
		model.addAttribute("functionAdd", URL_PARTICIPANT);
		
		GeneralAttributeCreator.create(model, STR_MODULE, STR_TITLE);
	}
}
