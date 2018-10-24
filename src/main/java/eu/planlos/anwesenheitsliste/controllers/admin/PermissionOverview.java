package eu.planlos.anwesenheitsliste.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.model.TeamService;
import eu.planlos.anwesenheitsliste.model.UserService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_PERMISSIONS;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_PERMISSIONS;

@Controller
public class PermissionOverview {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TeamService teamService;
	
	@RequestMapping(path = URL_PERMISSIONS)
	public String permissionOverview(Model model) {
		
		model.addAttribute("teams", teamService.findAll());
		model.addAttribute("users", userService.findAll());
		
		GeneralAttributeCreator.create(model, "Übersicht", "Übersicht der Berechtigungen");
		
		return RES_PERMISSIONS;
	}
}
