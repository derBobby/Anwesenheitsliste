package eu.planlos.anwesenheitsliste.controllers.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_TEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_TEAMLIST;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_TEAM;

@Controller
public class TeamDetail {

	@Autowired
	private TeamService teamService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private UserService userService;
	
	@RequestMapping(path = URL_TEAM + "{idTeam}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable Long idTeam) {

		model.addAttribute(teamService.findById(idTeam));
		model.addAttribute("participants", participantService.findAll());
		
		prepareContent(model);
		
		GeneralAttributeCreator.create(model, "Gruppenverwaltung", "Gruppe ändern");
		
		return RES_TEAM;
	}
	
	@RequestMapping(path = URL_TEAM, method = RequestMethod.GET)
	public String add(Model model) {
		
		model.addAttribute(new Team());
		model.addAttribute("participants", participantService.findAll());
		prepareContent(model);
		
		GeneralAttributeCreator.create(model, "Gruppenverwaltung", "Gruppe hinzufügen");
		
		return RES_TEAM;
	}

	//TODO Transactional
	@RequestMapping(path = URL_TEAM, method = RequestMethod.POST)
	public String submit(Model model, @Valid @ModelAttribute Team team, Errors errors) {
		
		if(errors.hasErrors()) {
			
			prepareContent(model);
			
			if(team.getIdTeam() != null) {
				GeneralAttributeCreator.create(model, "Gruppenverwaltung", "Gruppe ändern");
			} else {
				GeneralAttributeCreator.create(model, "Gruppenverwaltung", "Gruppe hinzufügen");
			}
			return RES_TEAM; 
		}

		Team savedTeam = teamService.save(team);
		
		userService.updateTeamForUsers(team, team.getUsers());
		participantService.updateTeamForParticipants(team, team.getParticipants());
		
		return "redirect:" + URL_TEAMLIST + savedTeam.getIdTeam();
	}
	
	private void prepareContent(Model model) {
		
		model.addAttribute("users", userService.findAll());

		model.addAttribute("formAction", URL_TEAM);
		model.addAttribute("formCancel", URL_TEAMLIST);
	}
}
