package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_PARTICIPANT;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_403;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_PARTICIPANT;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_PARTICIPANTLIST;

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
import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.ParticipantService;
import eu.planlos.anwesenheitsliste.service.SecurityService;
import eu.planlos.anwesenheitsliste.service.TeamService;

@Controller
public class ParticipantDetail {

	public final String STR_MODULE = "Teilnehmerverwaltung";
	public final String STR_TITLE_ADD_USER = "Teilnehmer hinzufügen";
	public final String STR_TITLE_EDIT_USER = "Teilnehmer ändern";

	@Autowired
	private BodyFillerService bf;
	
	@Autowired
	private ParticipantService participantService;

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private SecurityService securityService;
	
	// User
	@RequestMapping(path = URL_PARTICIPANT + "{idParticipant}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable Long idParticipant) {

		if(!securityService.isAdmin() && !hasPermissionForParticipant(idParticipant)) {
			return "redirect:" + URL_403;
		}
		
		Participant participant = participantService.findById(idParticipant);
		model.addAttribute(participant);
		prepareContent(model, participant);
		
		return RES_PARTICIPANT;
	}

	// User
	@RequestMapping(path = URL_PARTICIPANT, method = RequestMethod.GET)
	public String add(Model model) {
		
		Participant participant = new Participant();
		model.addAttribute(participant);
		prepareContent(model, participant);
		
		return RES_PARTICIPANT;
	}

	// User
	@RequestMapping(path = URL_PARTICIPANT, method = RequestMethod.POST)
	public String submit(Model model, @Valid @ModelAttribute Participant participant, Errors errors) {

		// Admin is always allowed, others if adding new or edited with permission
		if(! securityService.isAdmin() && ( participant.getIdParticipant() != null && !hasPermissionForParticipant(participant.getIdParticipant())) ) {
			return "redirect:" + URL_403;
		}
		
		if(errors.hasErrors()) {
			prepareContent(model, participant);
			return RES_PARTICIPANT; 
		}
		
		try {
			Participant savedParticipant = participantService.save(participant);
			//TODO where should admin be routed to?
			return "redirect:" + URL_PARTICIPANTLIST + savedParticipant.getIdParticipant();
			
		} catch (DuplicateKeyException e) {
			prepareContent(model, participant);
			model.addAttribute("customError", e.getMessage());
			return RES_PARTICIPANT;
		}
	}

	private void prepareContent(Model model, Participant participant) {

		if(participant.getIdParticipant() != null) {
			bf.fill(model, STR_MODULE, STR_TITLE_EDIT_USER);
		} else {
			bf.fill(model, STR_MODULE, STR_TITLE_ADD_USER);
		}
		
		//TODO only own teams
		model.addAttribute("teams", teamService.findAll());
		model.addAttribute("formAction", URL_PARTICIPANT);
		model.addAttribute("formCancel", URL_PARTICIPANTLIST);
	}
	
	private boolean hasPermissionForParticipant(Long idParticipant) {
		return securityService.isUserStaffForParticipant(idParticipant);
	}
}
