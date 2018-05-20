package eu.planlos.anwesenheitsliste.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.TeamService;
import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.model.UserService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class PermissionOverview {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TeamService teamService;
	
	@RequestMapping(path = "/permissionoverview")
	public String permissionOverview(Model model) {
		
		model.addAttribute("teams", teamService.findAll());
		model.addAttribute("users", userService.findAll());

		//TEMP
		List<Team> teams = teamService.findAll();
		List<User> users = userService.findAll();
		
		for(User user : users) {
			for(Team team : teams) {
				if(user.getTeams().contains(team)) System.out.println(user.getIdUser() + "<"+ team.getIdTeam());
			}
		}
		
		GeneralAttributeCreator.create(model, "Berechtigung", "Übersicht der Berechtigungen");
		
		return "overview/permissionoverview";
	}
}
