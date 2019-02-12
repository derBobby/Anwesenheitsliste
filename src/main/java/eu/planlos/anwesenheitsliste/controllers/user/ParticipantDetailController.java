package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_PARTICIPANT;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_403;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_PARTICIPANT;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_PARTICIPANTLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_PARTICIPANTLISTFULL;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
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
public class ParticipantDetailController {

	public final String STR_MODULE = "Teilnehmerverwaltung";
	public final String STR_TITLE_ADD_USER = "Teilnehmer hinzufügen";
	public final String STR_TITLE_EDIT_USER = "Teilnehmer ändern";
	
	private static final Logger logger = LoggerFactory.getLogger(ParticipantDetailController.class);

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
	public String edit(Model model, HttpSession session, Authentication auth, @PathVariable Long idParticipant) {

		String loginName = securityService.getLoginName(session);
		
		if(isNotAuthorizedForParticipant(session, idParticipant)) {
			logger.error("Benutzer \"" + loginName + "\" hat unauthorisiert versucht auf Teilnehmer id=" + idParticipant + " zuzugreifen.");
			return "redirect:" + URL_ERROR_403;
		}

		/*
		 * LOGIC
		 */
		Participant participant = participantService.loadParticipant(idParticipant);
		model.addAttribute(participant);
		prepareContent(model, auth, participant, loginName);
		
		return RES_PARTICIPANT;
	}

	// User
	@RequestMapping(path = URL_PARTICIPANT, method = RequestMethod.GET)
	public String add(Model model, HttpSession session, Authentication auth) {
		Participant participant = new Participant();
		model.addAttribute(participant);
		prepareContent(model, auth, participant, securityService.getLoginName(session));
		return RES_PARTICIPANT;
	}

	// User
	@RequestMapping(path = URL_PARTICIPANT, method = RequestMethod.POST)
	public String submit(Model model, Authentication auth, HttpSession session, @Valid @ModelAttribute Participant participant, Errors errors) {

		String loginName = securityService.getLoginName(session);
		
		if(participant.getTeams().isEmpty()) {
			//TODO go in try ant throw error instead?
			//TODO NEW set own error for attribute "team"?
			logger.debug("Benutzer \"" + loginName + "\" hat versucht den Teilnehmer id=" + participant.getIdParticipant() + " ohne Gruppe zu speichern.");
			model.addAttribute("customError", "Es muss mindestens eine Gruppe gewählt werden");
			prepareContent(model, auth, participant, loginName);
			return RES_PARTICIPANT; 
		}
		
		if(errors.hasErrors()) {
			//TODO NEW go in try and throw error instead?
			logger.debug("Benutzer \"" + loginName + "\" hat Validierungsfehler verursacht beim Versuch Teilnehmer mit id=" + participant.getIdParticipant() + " zu speichern.");
			prepareContent(model, auth, participant, loginName);
			return RES_PARTICIPANT; 
		}
		
		boolean isAddTeam = participant.getIdParticipant() == null;
		boolean isAdmin = securityService.isAdmin(session);
		
		// Admin is always allowed, others if adding new or edited with permission
		if(isAddTeam && isNotAuthorizedForParticipant(session, participant.getIdParticipant()) ) {
			logger.error("Benutzer \"" + loginName + "\" hat unauthorisiert versucht auf Teilnehmer mit id=" + participant.getIdParticipant() + " zuzugreifen.");
			return "redirect:" + URL_ERROR_403;
		}
		
		/*
		 * LOGIC
		 */
		try {
			Participant savedParticipant = participantService.saveParticipant(participant);

			if(isAdmin && isNotAuthorizedForParticipant(session, participant.getIdParticipant()) ) {
				return "redirect:" + URL_PARTICIPANTLIST + savedParticipant.getIdParticipant();
			}
			return "redirect:" + URL_PARTICIPANTLISTFULL + savedParticipant.getIdParticipant();
			
		} catch (DuplicateKeyException e) {
			logger.error("Benutzer \"" + loginName + "\" hat versucht den Teilnehmer id=" + participant.getIdParticipant() + " zu speichern: " + e.getMessage());
			prepareContent(model, auth, participant, loginName);
			model.addAttribute("customError", e.getMessage());
			return RES_PARTICIPANT;
		}
	}

	private void prepareContent(Model model, Authentication auth, Participant participant, String loginName) {

		if(participant.getIdParticipant() != null) {
			bf.fill(model, auth, STR_MODULE, STR_TITLE_EDIT_USER);
		} else {
			bf.fill(model, auth, STR_MODULE, STR_TITLE_ADD_USER);
		}
		
		model.addAttribute("teams", teamService.loadTeamsForUser(loginName));
		model.addAttribute("formAction", URL_PARTICIPANT);
		model.addAttribute("formCancel", URL_PARTICIPANTLIST);
	}
	
	private boolean isNotAuthorizedForParticipant(HttpSession session, long idParticipant) {
		return !securityService.isUserAuthorizedForParticipant(session, idParticipant);
	}
}
