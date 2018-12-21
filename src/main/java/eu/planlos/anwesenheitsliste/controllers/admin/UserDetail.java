package eu.planlos.anwesenheitsliste.controllers.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.planlos.anwesenheitsliste.model.TeamService;
import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.model.UserService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_USER;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_USERLIST;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_USER;

@Controller
public class UserDetail {

	@Autowired
	private UserService userService;

	@Autowired
	private TeamService teamService;
	
	@RequestMapping(path = URL_USER + "{idUser}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable Long idUser) {

		model.addAttribute(userService.findById(idUser));
		prepareContent(model);
		GeneralAttributeCreator.create(model, "Benutzerverwaltung", "Benutzer ändern");
		
		return RES_USER;
	}
	
	@RequestMapping(path = URL_USER, method = RequestMethod.GET)
	public String add(Model model) {
		
		model.addAttribute(new User());
		prepareContent(model);
		GeneralAttributeCreator.create(model, "Benutzerverwaltung", "Benutzer hinzufügen");
		
		return RES_USER;
	}

	@RequestMapping(path = URL_USER, method = RequestMethod.POST)
	public String submit(Model model, @Valid @ModelAttribute User user, Errors errors) {
		
		if(errors.hasErrors()) {

			errorHandler(model, user);
			return RES_USER;
		}
		
		try {
			User savedUser = userService.save(user);
			return "redirect:" + URL_USERLIST + savedUser.getIdUser();
			
		} catch (DuplicateKeyException e) {
			errorHandler(model, user);
			model.addAttribute("customError", e.getMessage());
			return RES_USER;
		}
	}

	private void errorHandler(Model model, User user) {
		
		prepareContent(model);
		
		if(user.getIdUser() != null) {
			GeneralAttributeCreator.create(model, "Benutzerverwaltung", "Benutzer ändern");
		} else {
			GeneralAttributeCreator.create(model, "Benutzerverwaltung", "Benutzer hinzufügen");
		}
	}

	private void prepareContent(Model model) {
		
		model.addAttribute("teams", teamService.findAll());
		model.addAttribute("formAction", URL_USER);
		model.addAttribute("formCancel", URL_USERLIST);
	}
}
