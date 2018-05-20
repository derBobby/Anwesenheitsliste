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

import eu.planlos.anwesenheitsliste.model.ParticipantService;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.TeamService;
import eu.planlos.anwesenheitsliste.model.UserService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class TeamDetail {

	@Autowired
	private TeamService teamService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private UserService userService;
	
	@RequestMapping(path = "/teamdetail/{idTeam}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable Long idTeam) {

		model.addAttribute(teamService.findById(idTeam));
		model.addAttribute("participants", participantService.findAll());
		model.addAttribute("users", userService.findAll());
		
		GeneralAttributeCreator.create(model, "Gruppenverwaltung", "Gruppe ändern");
		
		return "detail/teamdetail";
	}
	
	@RequestMapping(path = "/teamdetail", method = RequestMethod.GET)
	public String add(Model model) {
		
		model.addAttribute(new Team());
		model.addAttribute("participants", participantService.findAll());
		model.addAttribute("users", userService.findAll());
		GeneralAttributeCreator.create(model, "Gruppenverwaltung", "Gruppe hinzufügen");
		
		return "detail/teamdetail";
	}

	//TODO Transactional
	@RequestMapping(path = "/teamdetail", method = RequestMethod.POST)
	public String submit(Model model, @Valid @ModelAttribute Team team, Errors errors) {
		
		if(errors.hasErrors()) {
			
			if(team.getIdTeam() != null) {
				GeneralAttributeCreator.create(model, "Gruppenverwaltung", "Gruppe ändern");
			} else {
				GeneralAttributeCreator.create(model, "Gruppenverwaltung", "Gruppe hinzufügen");
			}
			return "detail/teamdetail"; 
		}
		
		userService.updateTeamForUsers(team, team.getUsers());
		participantService.updateTeamForParticipants(team, team.getParticipants());
		
		Team savedTeam = teamService.save(team);
		return "redirect:/teamlist/" + savedTeam.getIdTeam();
	}
}
