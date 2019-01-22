package eu.planlos.anwesenheitsliste.controllers.admin;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_PERMISSIONSOVERVIEW;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_PERMISSIONSOVERVIEW;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.TeamService;
import eu.planlos.anwesenheitsliste.service.UserService;

@Controller
public class PermissionOverview {

	@Autowired
	private BodyFillerService bf;

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
