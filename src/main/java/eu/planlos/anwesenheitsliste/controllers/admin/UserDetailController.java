package eu.planlos.anwesenheitsliste.controllers.admin;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_USER;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_USER;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_USERLIST;

import java.security.Principal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.TeamService;
import eu.planlos.anwesenheitsliste.service.UserService;

@Controller
public class UserDetailController {

	public final String STR_MODULE = "Benutzerverwaltung";
	public final String STR_TITLE_ADD_USER = "Benutzer hinzufügen";
	public final String STR_TITLE_EDIT_USER = "Benutzer ändern";

	private static final Logger logger = LoggerFactory.getLogger(UserDetailController.class);

	@Autowired
	private BodyFillerService bf;
	
	@Autowired
	private UserService userService;

	@Autowired
	private TeamService teamService;
	
	@RequestMapping(path = URL_USER + "{idUser}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable Long idUser) {

		User user = userService.loadUser(idUser);
		model.addAttribute(user);
		prepareContent(model, user);
		
		return RES_USER;
	}
	
	@RequestMapping(path = URL_USER, method = RequestMethod.GET)
	public String add(Model model) {
		
		User user = new User();
		model.addAttribute(user);
		prepareContent(model, user);
		
		return RES_USER;
	}

	@RequestMapping(path = URL_USER, method = RequestMethod.POST)
	public String submit(Model model, Principal principal, @Valid @ModelAttribute User user, Errors errors) {
		
		if(errors.hasErrors()) {
			prepareContent(model, user);
			return RES_USER;
		}
		
		try {
			User savedUser = userService.save(user);
			logger.error("Benutzer " + user.getIdUser() + ": \"" + user.getLoginName() + "\" wurde von \"" + principal.getName() + "\" gespeichert");
			return "redirect:" + URL_USERLIST + savedUser.getIdUser();
			
		} catch (Exception e) {
			logger.error("Benutzer " + user.getIdUser() + ": \"" + user.getLoginName() + "\" konnte nicht von \"" + principal.getName() + "\" gespeichert werden: " + e.getMessage());
			e.printStackTrace();
			
			prepareContent(model, user);
			model.addAttribute("customError", e.getMessage());
			return RES_USER;
		}
	}

	private void prepareContent(Model model, User user) {
		
		if(user.getIdUser() != null) {
			bf.fill(model, STR_MODULE, STR_TITLE_EDIT_USER);
		} else {
			bf.fill(model, STR_MODULE, STR_TITLE_ADD_USER);
		}
		
		model.addAttribute("teams", teamService.loadAllTeams());
		model.addAttribute("formAction", URL_USER);
		model.addAttribute("formCancel", URL_USERLIST);
	}
}
