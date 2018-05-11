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

import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.model.UserService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class UserEdit {

	@Autowired
	private UserService userService;
	
	@RequestMapping(path = "/useredit/{idUser}", method = RequestMethod.GET)
	public String showForm(Model model, @PathVariable Long idUser) {
		
		User user = userService.findById(idUser);
		model.addAttribute(user);
		prepareContent(model);
		
		return "detail/userdetail";
	}
	
	@RequestMapping(path = "/useredit", method = RequestMethod.POST)
	public String edit(Model model, @Valid @ModelAttribute User user, Errors errors) {
		
		if(errors.hasErrors()) {
			prepareContent(model);
			return "detail/userdetail";
		}
		
		userService.save(user);
		
		return "redirect:/userlist/" + user.getIdUser();
	}

	private void prepareContent(Model model) {
		
		model.addAttribute("newButtonText", "Benutzer ändern");
		model.addAttribute("newButtonUrl", "/useredit");

		GeneralAttributeCreator generalAttributeCreator = new GeneralAttributeCreator();
		generalAttributeCreator.create(model, "Benutzerverwaltung", "Benutzer ändern");
	}
}
