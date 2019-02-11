package eu.planlos.anwesenheitsliste.controllers.admin;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_USERLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_USER;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_USERLIST;

import java.util.ArrayList;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.UserService;

@Controller
public class UserListController {

	@Autowired
	private BodyFillerService bf;

	@Autowired
    private UserService userService;
	
	@RequestMapping(value = URL_USERLIST + "{markedUserId}")
	@Secured ({"USER"})
	public String  markedUserList(Model model, Authentication auth, @PathVariable Long markedUserId) {

		prepareContent(model, auth, markedUserId);
		return RES_USERLIST;
	}

	@RequestMapping(value = URL_USERLIST)
	@Secured ({"USER"})
	public String  userList(Model model, Authentication auth) {
		
		prepareContent(model, auth, null);
		return RES_USERLIST;
	}
	
	private void prepareContent(Model model, Authentication auth, Long markedUserId) {
		
		List<String> headings = new ArrayList<>();	
		headings.add("#");
		headings.add("Vorname");
		headings.add("Nachname");
		headings.add("Loginname");
		headings.add("E-Mail");
		headings.add("Aktiv");
		headings.add("Admin");
		headings.add("Funktionen");
	
		model.addAttribute("headings", headings);
		model.addAttribute("users", userService.loadAllUsers());
		model.addAttribute("markedUserId", markedUserId);
		
		model.addAttribute("functionEdit", URL_USER);
		model.addAttribute("functionResetPW", "TODO"); //TODO NEW Password reset
		model.addAttribute("functionAdd", URL_USER);
		
		bf.fill(model, auth, "Benutzerverwaltung", "Liste der Benutzer");
	}
}
