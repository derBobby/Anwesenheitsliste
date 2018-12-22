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

import eu.planlos.anwesenheitsliste.model.ParticipantService;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.TeamService;
import eu.planlos.anwesenheitsliste.model.UserService;
import eu.planlos.anwesenheitsliste.model.exception.EmptyIdException;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_TEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_TEAMLIST;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_TEAM;

@Controller
public class TeamDetail {

	public final String STR_MODULE = "Gruppenverwaltung";
	public final String STR_TITLE_ADD_TEAM = "Gruppe hinzufügen";
	public final String STR_TITLE_EDIT_TEAM = "Gruppe ändern";
	
	@Autowired
	private TeamService teamService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private UserService userService;
	
	@RequestMapping(path = URL_TEAM + "{idTeam}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable Long idTeam) {

		Team team = teamService.findById(idTeam);
		model.addAttribute(team);
		prepareContent(model, team);
				
		return RES_TEAM;
	}
	
	@RequestMapping(path = URL_TEAM, method = RequestMethod.GET)
	public String add(Model model) {
		
		Team team = new Team();
		model.addAttribute(team);
		prepareContent(model, team);
				
		return RES_TEAM;
	}

	//TODO Transactional
	@RequestMapping(path = URL_TEAM, method = RequestMethod.POST)
	public String submit(Model model, @Valid @ModelAttribute Team team, Errors errors) {
		
		if(errors.hasErrors()) {
			prepareContent(model, team);
			return RES_TEAM;
		}
		
		try {
			Team savedTeam = teamService.save(team);

			userService.updateTeamForUsers(team, team.getUsers());
			participantService.updateTeamForParticipants(team, team.getParticipants());
			return "redirect:" + URL_TEAMLIST + savedTeam.getIdTeam();

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
			GeneralAttributeCreator.create(model, STR_MODULE, STR_TITLE_EDIT_TEAM);
		} else {
			GeneralAttributeCreator.create(model, STR_MODULE, STR_TITLE_ADD_TEAM);
		}
		
		model.addAttribute("users", userService.findAll());
		model.addAttribute("participants", participantService.findAll());
		model.addAttribute("formAction", URL_TEAM);
		model.addAttribute("formCancel", URL_TEAMLIST);
	}
}
