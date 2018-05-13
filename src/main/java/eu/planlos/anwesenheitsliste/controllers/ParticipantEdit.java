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
public class ParticipantEdit {

	@Autowired
	private ParticipantService participantService;
	
	@RequestMapping(path = "/participantedit/{idParticipant}", method = RequestMethod.GET)
	public String showForm(Model model, @PathVariable Long idParticipant) {
		
		Participant partcipant = participantService.findById(idParticipant);
		model.addAttribute(partcipant);
		prepareContent(model);
		
		return "detail/participantdetail";
	}
	
	@RequestMapping(path = "/participantedit", method = RequestMethod.POST)
	public String edit(Model model, @Valid @ModelAttribute Participant participant, Errors errors) {
		
		if(errors.hasErrors()) {
			prepareContent(model);
			return "detail/participantdetail";
		}
		
		participantService.save(participant);
		
		return "redirect:/participantlist/" + participant.getIdParticipant();
	}

	private void prepareContent(Model model) {

		model.addAttribute("newButtonUrl", "/participantedit");
		model.addAttribute("newButtonText", "Teilnehmer hinzufügen");
		
		GeneralAttributeCreator attributeCreator = new GeneralAttributeCreator();
		attributeCreator.create(model, "Teilnehmerverwaltung", "Teilnehmer hinzufügen");
	}
}
