package eu.planlos.anwesenheitsliste.controllers.admin;

import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.RES_PERMISSIONSOVERVIEW;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_PERMISSIONSOVERVIEW;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.model.TeamService;
import eu.planlos.anwesenheitsliste.model.UserService;
import eu.planlos.anwesenheitsliste.viewhelper.BodyFiller;

@Controller
public class PermissionOverview {

	@Autowired
	private BodyFiller bf;

	@Autowired
	private UserService userService;
	
	@Autowired
	private TeamService teamService;
	
	@RequestMapping(path = URL_PERMISSIONSOVERVIEW)
	public String permissionOverview(Model model) {
		
		model.addAttribute("teams", teamService.findAll());
		model.addAttribute("users", userService.findAll());
		
		bf.fill(model, "Übersicht", "Übersicht der Berechtigungen");
		
		return RES_PERMISSIONSOVERVIEW;
	}
}
