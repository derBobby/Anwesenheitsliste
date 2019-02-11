package eu.planlos.anwesenheitsliste.controllers.admin;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_PERMISSIONSOVERVIEW;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_PERMISSIONSOVERVIEW;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.TeamService;
import eu.planlos.anwesenheitsliste.service.UserService;

@Controller
public class PermissionOverviewController {

	@Autowired
	private BodyFillerService bf;

	@Autowired
	private UserService userService;
	
	@Autowired
	private TeamService teamService;
	
	@RequestMapping(path = URL_PERMISSIONSOVERVIEW)
	public String permissionOverview(Model model, Authentication auth) {
		
		model.addAttribute("teams", teamService.loadAllTeams());
		model.addAttribute("users", userService.loadAllUsers());
		
		bf.fill(model, auth, "Übersicht", "Übersicht der Berechtigungen");
		
		return RES_PERMISSIONSOVERVIEW;
	}
}
