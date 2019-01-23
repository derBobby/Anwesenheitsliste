package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_TEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_403;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMLISTFULL;

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

import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.exception.EmptyIdException;
import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.ParticipantService;
import eu.planlos.anwesenheitsliste.service.SecurityService;
import eu.planlos.anwesenheitsliste.service.TeamService;
import eu.planlos.anwesenheitsliste.service.UserService;

@Controller
public class TeamDetail {

	public final String STR_MODULE = "Gruppenverwaltung";
	public final String STR_TITLE_ADD_TEAM = "Gruppe hinzufügen";
	public final String STR_TITLE_EDIT_TEAM = "Gruppe ändern";

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
	public String edit(Model model, @PathVariable Long idTeam) {

		if(!securityService.isAdmin() && !securityService.isUserAuthorizedForTeam(idTeam)) {
			return "redirect:" + URL_ERROR_403;
		}
		
		Team team = teamService.findById(idTeam);
		model.addAttribute(team);
		prepareContent(model, team);
				
		return RES_TEAM;
	}
	
	@RequestMapping(path = URL_TEAM, method = RequestMethod.GET)
	public String add(Model model) {

		if(!securityService.isAdmin()) {
			return "redirect:" + URL_ERROR_403;
		}
		
		Team team = new Team();
		model.addAttribute(team);
		prepareContent(model, team);
				
		return RES_TEAM;
	}

	//TODO Transactional
	@RequestMapping(path = URL_TEAM, method = RequestMethod.POST)
	public String submit(Model model, @Valid @ModelAttribute Team team, Errors errors) {

		//Admin is always allowed, others if it is edit and is authorized
		if(!securityService.isAdmin() && ( team.getIdTeam() == null || !securityService.isUserAuthorizedForTeam(team.getIdTeam()) ) ) {
			return "redirect:" + URL_ERROR_403;
		}
		
		if(errors.hasErrors()) {
			prepareContent(model, team);
			return RES_TEAM;
		}
		
		try {
			Team savedTeam = teamService.save(team);

			userService.updateTeamForUsers(team);
			participantService.updateTeamForParticipants(team);
			
			//TODO normal user routed to admin page
			return "redirect:" + URL_TEAMLISTFULL + savedTeam.getIdTeam();

		} catch (EmptyIdException e) {
			//TODO Logger
			model.addAttribute("customError", e.getMessage());
			prepareContent(model, team);
			
		} catch (DuplicateKeyException e) {
			//TODO Logger
			model.addAttribute("customError", e.getMessage());
			prepareContent(model, team);
		}
		
		return RES_TEAM;
	}
	
	private void prepareContent(Model model, Team team) {
		
		if(team.getIdTeam() != null) {
			bf.fill(model, STR_MODULE, STR_TITLE_EDIT_TEAM);
		} else {
			bf.fill(model, STR_MODULE, STR_TITLE_ADD_TEAM);
		}
		
		model.addAttribute("users", userService.findAll());
		model.addAttribute("participants", participantService.findAll());
		model.addAttribute("formAction", URL_TEAM);
		model.addAttribute("formCancel", URL_TEAMLISTFULL);
	}
}
