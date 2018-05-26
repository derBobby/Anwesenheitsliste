package eu.planlos.anwesenheitsliste.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.model.TeamService;
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
		
		GeneralAttributeCreator.create(model, "Übersicht", "Übersicht der Berechtigungen");
		
		return "overview/permissionoverview";
	}
}
