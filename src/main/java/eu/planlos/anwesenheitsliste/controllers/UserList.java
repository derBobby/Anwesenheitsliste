package eu.planlos.anwesenheitsliste.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.model.UserService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class UserList {

	@Autowired
    UserService userService;
	
	@RequestMapping(value = "/userlist/{markedUserId}")
	public String  markedUserList(Model model, @PathVariable Long markedUserId) {

		prepareContent(model, markedUserId);
		return "list/userlist";
	}
	
	@RequestMapping(value = "/userlist")
	public String  userList(Model model) {
		
		prepareContent(model, null);
		return "list/userlist";
	}
	
	private void prepareContent(Model model, Long markedUserId) {
		
		List<String> headings = new ArrayList<String>();	
		headings.add("#");
		headings.add("Vorname");
		headings.add("Nachname");
		headings.add("Loginname");
		headings.add("E-Mail");
		headings.add("Aktiv");
		headings.add("Admin");
		headings.add("Funktionen");

		List<User> users = userService.findAll();
		
		model.addAttribute("listtemplate", "userlist");
		model.addAttribute("headings", headings);
		model.addAttribute("users", users);
		model.addAttribute("markedUserId", markedUserId);
		model.addAttribute("newButtonText", "Neuer Benutzer");
		model.addAttribute("newButtonUrl", "/useradd");
		
		GeneralAttributeCreator generalAttributeCreator = new GeneralAttributeCreator();
		generalAttributeCreator.create(model, "Benutzerverwaltung", "Liste der Benutzer");
	}
}
