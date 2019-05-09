package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_TEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_403;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMADD;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMEDIT;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMLISTFULL;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMSUBMIT;

import java.security.Principal;

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

import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.exception.EmptyIdException;
import eu.planlos.anwesenheitsliste.model.exception.NotAuthorizedForTeamException;
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
	
	@RequestMapping(path = URL_TEAMEDIT + "{idTeam}", method = RequestMethod.GET)
	public String edit(Model model, Authentication auth, HttpSession session, @PathVariable Long idTeam) {

		String loginName = securityService.getLoginName(session);
		
		if(isUserNotAuthorizedForTeam(session, idTeam)) {
			logger.error("Benutzer \"" + loginName + "\" hat unauthorisiert versucht auf Gruppe id=" + idTeam + " zuzugreifen.");
			return "redirect:" + URL_ERROR_403;
		}
		
		/*
		 * LOGIC
		 */
		Team team = teamService.loadTeam(idTeam);
		model.addAttribute(team);
		prepareContent(model, auth, team, false, securityService.isAdmin(session));
				
		return RES_TEAM;
	}
	
	@RequestMapping(path = URL_TEAMADD, method = RequestMethod.GET)
	public String add(Model model, Authentication auth, Principal principal, HttpSession session) {

		Team team = new Team();
		model.addAttribute(team);
		prepareContent(model, auth, team, true, true);
				
		return RES_TEAM;
	}

	//TODO Transactional
	@RequestMapping(path = URL_TEAMSUBMIT, method = RequestMethod.POST)
	public String submit(Model model, Authentication auth, Principal principal, HttpSession session, @Valid @ModelAttribute Team team, Errors errors) {

		String loginName = principal.getName();
		
		boolean isAdmin = securityService.isAdmin(session);
		boolean isAddTeam = team.getIdTeam() == null;

		
		//TODO better to use CustomErrorController somehow?
		if(! isAdmin) {
			
			boolean isNotAuthorizedForTeam = false;
			
			try {
					
				if(! isAddTeam) {
					isNotAuthorizedForTeam = isUserNotAuthorizedForTeam(session, team.getIdTeam());
				}
				checkUserAuthorization(team, loginName, isAddTeam, isNotAuthorizedForTeam);
			} catch (NotAuthorizedForTeamException e) {
				return "redirect:" + URL_ERROR_403;
			}
		}
		
		if(errors.hasErrors()) {
			logger.error("Validierungsfehler beim Submit von Gruppe \"" + team.getTeamName() + "\" von Benutzer \"" + loginName + "\" .");
			prepareContent(model, auth, team, isAddTeam, isAdmin);
			return RES_TEAM;
		}

		/*
		 * LOGIC
		 */
		try {
			// Team doesn't know the P's or U's
			Team savedTeam = teamService.saveTeam(team);

			// Users must get the team set because they own the relation
			userService.updateTeamForUsers(team);
			// Participants must get the team set because they own the relation
			participantService.updateTeamForParticipants(team);
			
			if(isAdmin) {
				return "redirect:" + URL_TEAMLISTFULL + savedTeam.getIdTeam();
			}

			return "redirect:" + URL_TEAMLIST + savedTeam.getIdTeam();
			
		} catch (EmptyIdException e) {
			logger.error("Team "+ team.getIdTeam() +": " + team.getTeamName() + " konnte nicht gespeichert werden -> ID eines Benutzers oder Teilnehmers nicht gesetzt?.");
			model.addAttribute("customError", e.getMessage());
		} catch (DuplicateKeyException e) {
			logger.error("Team "+ team.getIdTeam() +": " + team.getTeamName() + " konnte nicht gespeichert werden -> Unique Constraint.");
			model.addAttribute("customError", e.getMessage());
		}
		
		prepareContent(model, auth, team, isAddTeam, isAdmin);
		return RES_TEAM;
	}

	private void checkUserAuthorization(Team team, String loginName, boolean isAddTeam, boolean isNotAuthorizedForTeam) throws NotAuthorizedForTeamException {
		
		boolean isEditTeam = ! isAddTeam;
		
		if(isAddTeam) {
			logger.error("Benutzer \"" + loginName + "\" hat unauthorisiert versucht eine neue Gruppe zu speichern.");
			throw new NotAuthorizedForTeamException();
		} else if (isEditTeam && isNotAuthorizedForTeam) {
			logger.error("Benutzer \"" + loginName + "\" hat unauthorisiert versucht die Gruppe mit id=" + team.getIdTeam() + " zu speichern.");
			throw new NotAuthorizedForTeamException();
		}
			
		logger.error("Benutzer \"" + loginName + "\" ist authorisiert die Gruppe mit id=" + team.getIdTeam() + " zu ändern.");
	}
	
	private void prepareContent(Model model, Authentication auth, Team team, boolean isAddTeam, boolean isAdmin) {
		
		if(team.getIdTeam() != null) {
			bf.fill(model, auth, STR_MODULE, STR_TITLE_EDIT_TEAM);
		} else {
			bf.fill(model, auth, STR_MODULE, STR_TITLE_ADD_TEAM);
		}
		
		model.addAttribute("users", userService.loadAllUsers());
		model.addAttribute("participants", participantService.loadAllParticipants());

		model.addAttribute("formAction", URL_TEAMSUBMIT);
		
		String formCancel = URL_TEAMLIST + team.getIdTeam();
		if(isAdmin) {
			formCancel = URL_TEAMLISTFULL;
		}
		model.addAttribute("formCancel", formCancel);
	}

	private boolean isUserNotAuthorizedForTeam(HttpSession session, Long idTeam) {
		return !securityService.isUserAuthorizedForTeam(session, idTeam);
	}
}
