package eu.planlos.anwesenheitsliste.controllers.admin;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_USERLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_USER;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_USERLIST;

import java.util.ArrayList;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.UserService;

@Controller
public class UserList {

	@Autowired
	private BodyFillerService bf;

	@Autowired
    UserService userService;
	
	@RequestMapping(value = URL_USERLIST + "{markedUserId}")
	@Secured ({"USER"})
	public String  markedUserList(Model model, @PathVariable Long markedUserId) {

		prepareContent(model, markedUserId);
		return RES_USERLIST;
	}

	@RequestMapping(value = URL_USERLIST)
	@Secured ({"USER"})
	public String  userList(Model model) {
		
		prepareContent(model, null);
		return RES_USERLIST;
	}
	
	private void prepareContent(Model model, Long markedUserId) {
		
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
		model.addAttribute("users", userService.findAll());
		model.addAttribute("markedUserId", markedUserId);
		
		model.addAttribute("functionEdit", URL_USER);
		model.addAttribute("functionResetPW", "TODO"); //TODO NEW Password reset
		model.addAttribute("functionAdd", URL_USER);
		
		bf.fill(model, "Benutzerverwaltung", "Liste der Benutzer");
	}
}
