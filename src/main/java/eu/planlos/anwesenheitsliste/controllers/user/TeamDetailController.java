package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_403;
import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_TEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMLISTFULL;

import java.security.Principal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.planlos.anwesenheitsliste.SessionAttributes;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.exception.EmptyIdException;
import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.ParticipantService;
import eu.planlos.anwesenheitsliste.service.SecurityService;
import eu.planlos.anwesenheitsliste.service.TeamService;
import eu.planlos.anwesenheitsliste.service.UserService;

@Controller
public class TeamDetailController {

	public final String STR_MODULE = "Gruppenverwaltung";
	public final String STR_TITLE_ADD_TEAM = "Gruppe hinzufügen";
	public final String STR_TITLE_EDIT_TEAM = "Gruppe ändern";

	private static final Logger logger = LoggerFactory.getLogger(TeamDetailController.class);
	
	@Autowired
	private BodyFillerService bf;
	
	@Autowired
	private TeamService teamService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private SecurityService securityService;
	
	@RequestMapping(path = URL_TEAM + "{idTeam}", method = RequestMethod.GET)
	public String edit(Model model, Principal principal, Session session, @PathVariable Long idTeam) {

		String loginName = principal.getName();
		boolean isAdmin = isAdmin(session);
		
		if(!isAdmin && !securityService.isUserAuthorizedForTeam(idTeam, loginName)) {
			logger.error("Benutzer \"" + loginName + "\" hat unauthorisiert versucht auf Gruppe id=" + idTeam + " zuzugreifen.");
			return "redirect:" + URL_ERROR_403;
		}
		
		Team team = teamService.findById(idTeam);
		model.addAttribute(team);
		prepareContent(model, team, isAdmin);
				
		return RES_TEAM;
	}
	
	@RequestMapping(path = URL_TEAM, method = RequestMethod.GET)
	public String add(Model model, Principal principal, Session session) {

		boolean isAdmin = isAdmin(session);
		
		//TODO should be admin url
		if(!isAdmin) {
			logger.error("Benutzer \"" + principal.getName() + "\" hat unauthorisiert versucht eine Gruppe anzulegen.");
			return "redirect:" + URL_ERROR_403;
		}
		
		Team team = new Team();
		model.addAttribute(team);
		prepareContent(model, team, isAdmin);
				
		return RES_TEAM;
	}

	//TODO Transactional
	@RequestMapping(path = URL_TEAM, method = RequestMethod.POST)
	public String submit(Model model, Principal principal, Session session, @Valid @ModelAttribute Team team, Errors errors) {

		String loginName = principal.getName();
		
		boolean isAuthorizedForTeam = securityService.isUserAuthorizedForTeam(team.getIdTeam(), loginName);
		boolean isAdmin = isAdmin(session);
		boolean isAddTeam = team.getIdTeam() == null;
		
		// Admin is always allowed, others if it is edit and is authorized
		if(!isAdmin && ( isAddTeam || !isAuthorizedForTeam) ) {
			String teamInfo = team.getIdTeam() == null ? "" : "(id= " + team.getIdTeam() + ") " ;
			logger.error("Benutzer \"" + loginName + "\" hat unauthorisiert versucht eine Gruppe " + teamInfo + "zu speichern.");
			return "redirect:" + URL_ERROR_403;
		}
		
		if(errors.hasErrors()) {
			logger.error("Validierungsfehler beim Submit von Gruppe \"" + team.getTeamName() + "\" von Benutzer \"" + loginName + "\" .");
			prepareContent(model, team, isAdmin);
			return RES_TEAM;
		}
		
		try {
			Team savedTeam = teamService.save(team);

			//TODO what happens in this two methods?
			userService.updateTeamForUsers(team);
			participantService.updateTeamForParticipants(team);
			
			if(isAuthorizedForTeam) {
				return "redirect:" + URL_TEAMLIST + savedTeam.getIdTeam();
			}
			return "redirect:" + URL_TEAMLISTFULL + savedTeam.getIdTeam();

		} catch (EmptyIdException e) {
			logger.error("Team "+ team.getIdTeam() +": " + team.getTeamName() + " konnte nicht gespeichert werden -> ID eines Benutzers nicht gesetzt?.");
			model.addAttribute("customError", e.getMessage());
			
		} catch (DuplicateKeyException e) {
			logger.error("Team "+ team.getIdTeam() +": " + team.getTeamName() + " konnte nicht gespeichert werden -> Unique Constraint.");
			model.addAttribute("customError", e.getMessage());
		}
		
		prepareContent(model, team, isAdmin);
		return RES_TEAM;
	}
	
	private void prepareContent(Model model, Team team, boolean isAdmin) {
		
		if(team.getIdTeam() != null) {
			bf.fill(model, STR_MODULE, STR_TITLE_EDIT_TEAM);
		} else {
			bf.fill(model, STR_MODULE, STR_TITLE_ADD_TEAM);
		}
		
		model.addAttribute("users", userService.findAll());
		model.addAttribute("participants", participantService.findAll());
		model.addAttribute("formAction", URL_TEAM);
		
		if(isAdmin) {
			model.addAttribute("formCancel", URL_TEAMLISTFULL);
			return;
		}
		model.addAttribute("formCancel", URL_TEAMLIST + team.getIdTeam());
	}
	
	private boolean isAdmin(Session session) {
		return session.getAttribute(SessionAttributes.ISADMIN);
	}
}
