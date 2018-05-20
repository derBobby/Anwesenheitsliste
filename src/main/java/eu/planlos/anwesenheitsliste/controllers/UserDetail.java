package eu.planlos.anwesenheitsliste.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class UserDetail {

	@Autowired
	private UserService userService;

	@Autowired
	private TeamService teamService;
	
	@RequestMapping(path = "/userdetail/{idUser}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable Long idUser) {

		model.addAttribute(userService.findById(idUser));
		model.addAttribute("teams", teamService.findAll());
		GeneralAttributeCreator.create(model, "Benutzerverwaltung", "Benutzer ändern");
		
		return "detail/userdetail";
	}
	
	@RequestMapping(path = "/userdetail", method = RequestMethod.GET)
	public String add(Model model) {
		
		model.addAttribute(new User());
		model.addAttribute("teams", teamService.findAll());
		GeneralAttributeCreator.create(model, "Benutzerverwaltung", "Benutzer hinzufügen");
		
		return "detail/userdetail";
	}

	@RequestMapping(path = "/userdetail", method = RequestMethod.POST)
	public String submit(Model model, @Valid @ModelAttribute User user, Errors errors) {
		
		if(errors.hasErrors()) {

			model.addAttribute("teams", teamService.findAll());
			
			if(user.getIdUser() != null) {
				GeneralAttributeCreator.create(model, "Benutzerverwaltung", "Benutzer ändern");
			} else {
				GeneralAttributeCreator.create(model, "Benutzerverwaltung", "Benutzer hinzufügen");
			}
			return "detail/userdetail"; 
		}
		
		User savedUser = userService.save(user);
		return "redirect:/userlist/" + savedUser.getIdUser();
	}
}
