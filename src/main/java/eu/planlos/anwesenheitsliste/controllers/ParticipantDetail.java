package eu.planlos.anwesenheitsliste.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.ParticipantService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class ParticipantDetail {

	@Autowired
	private ParticipantService participantService;
	
	@RequestMapping(path = "/participantdetail/{idParticipant}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable Long idParticipant) {

		model.addAttribute(participantService.findById(idParticipant));
		GeneralAttributeCreator.create(model, "Teilnehmerverwaltung", "Teilnehmer ändern");
		
		return "detail/participantdetail";
	}
	
	@RequestMapping(path = "/participantdetail", method = RequestMethod.GET)
	public String add(Model model) {
		
		model.addAttribute(new Participant());
		 GeneralAttributeCreator.create(model, "Teilnehmerverwaltung", "Teilnehmer hinzufügen");
		
		return "detail/participantdetail";
	}

	@RequestMapping(path = "/participantdetail", method = RequestMethod.POST)
	public String submit(Model model, @Valid @ModelAttribute Participant participant, Errors errors) {
		
		if(errors.hasErrors()) {
			
			if(participant.getIdParticipant() != null) {
				GeneralAttributeCreator.create(model, "Teilnehmerverwaltung", "Teilnehmer ändern");
			} else {
				 GeneralAttributeCreator.create(model, "Teilnehmerverwaltung", "Teilnehmer hinzufügen");
			}
			return "detail/participantdetail"; 
		}
		
		Participant savedParticipant = participantService.save(participant);
		return "redirect:/participantlist/" + savedParticipant.getIdParticipant();
	}
}
