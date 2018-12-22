package eu.planlos.anwesenheitsliste.controllers.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.ParticipantService;
import eu.planlos.anwesenheitsliste.model.TeamService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_PARTICIPANT;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_PARTICIPANTLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_PARTICIPANT;

@Controller
public class ParticipantDetail {

	public final String STR_MODULE = "Teilnehmerverwaltung";
	public final String STR_TITLE_ADD_USER = "Teilnehmer hinzufügen";
	public final String STR_TITLE_EDIT_USER = "Teilnehmer ändern";
	
	@Autowired
	private ParticipantService participantService;

	@Autowired
	private TeamService teamService;
	
	@RequestMapping(path = URL_PARTICIPANT + "{idParticipant}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable Long idParticipant) {

		Participant participant = participantService.findById(idParticipant);
		model.addAttribute(participant);
		prepareContent(model, participant);
		
		return RES_PARTICIPANT;
	}
	
	@RequestMapping(path = URL_PARTICIPANT, method = RequestMethod.GET)
	public String add(Model model) {
		
		Participant participant = new Participant();
		model.addAttribute(participant);
		prepareContent(model, participant);
		
		return RES_PARTICIPANT;
	}

	@RequestMapping(path = URL_PARTICIPANT, method = RequestMethod.POST)
	public String submit(Model model, @Valid @ModelAttribute Participant participant, Errors errors) {
		
		if(errors.hasErrors()) {
			prepareContent(model, participant);
			return RES_PARTICIPANT; 
		}
		
		try {
			Participant savedParticipant = participantService.save(participant);
			return "redirect:" + URL_PARTICIPANTLIST + savedParticipant.getIdParticipant();
			
		} catch (DuplicateKeyException e) {
			prepareContent(model, participant);
			model.addAttribute("customError", e.getMessage());
			return RES_PARTICIPANT;
		}
	}

	private void prepareContent(Model model, Participant participant) {

		if(participant.getIdParticipant() != null) {
			GeneralAttributeCreator.create(model, STR_MODULE, STR_TITLE_EDIT_USER);
		} else {
			 GeneralAttributeCreator.create(model, STR_MODULE, STR_TITLE_ADD_USER);
		}
		
		model.addAttribute("teams", teamService.findAll());
		model.addAttribute("formAction", URL_PARTICIPANT);
		model.addAttribute("formCancel", URL_PARTICIPANTLIST);
	}
}
