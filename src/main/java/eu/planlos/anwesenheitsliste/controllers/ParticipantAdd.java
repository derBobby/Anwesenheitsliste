package eu.planlos.anwesenheitsliste.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.ParticipantService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class ParticipantAdd {

	@Autowired
	private ParticipantService participantService;
	
	@RequestMapping(path = "/participantadd", method = RequestMethod.GET)
	public String showForm(Model model) {
		
		model.addAttribute(new Participant());
		prepareContent(model);
		
		return "detail/participantdetail";
	}
	
	@RequestMapping(path = "/participantadd", method = RequestMethod.POST)
	public String add(Model model, @Valid @ModelAttribute Participant participant, Errors errors) {
		
		if(errors.hasErrors()) {
			prepareContent(model);
			return "detail/participantdetail";
		}
		
		Participant newParticipant = participantService.save(participant);
		return "redirect:/participantlist" + newParticipant.getIdParticipant();
	}

	private void prepareContent(Model model) {
		
		model.addAttribute("newButtonUrl", "/participantadd");
		model.addAttribute("newButtonText", "Teilnehmer hinzufügen");
		
		GeneralAttributeCreator generalAttributeCreator = new GeneralAttributeCreator();
		generalAttributeCreator.create(model, "Teilnehmerverwalung", "Teilnehmer hinzufügen");
	}
}
