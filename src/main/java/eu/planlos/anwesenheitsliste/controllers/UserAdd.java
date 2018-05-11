package eu.planlos.anwesenheitsliste.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.model.UserService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class UserAdd {

	@Autowired
	private UserService userService;
	
	@RequestMapping(path = "/useradd", method = RequestMethod.GET)
	public String showForm(Model model) {
				
		model.addAttribute(new User());
		prepareContent(model);
		
		return "detail/userdetail";
	}
	
	@RequestMapping(path = "/useradd", method = RequestMethod.POST)
	public String add(Model model, @Valid @ModelAttribute User user, Errors errors) {
		
		if(errors.hasErrors()) {
			prepareContent(model);
			return "detail/userdetail"; 
		}
		
		User newUser = userService.save(user);
		return "redirect:/userlist/" + newUser.getIdUser();
	}

	private void prepareContent(Model model) {
		
		model.addAttribute("newButtonText", "Benutzer hinzufügen");
		model.addAttribute("newButtonUrl", "/useradd");
		
		GeneralAttributeCreator generalAttributeCreator = new GeneralAttributeCreator();
		generalAttributeCreator.create(model, "Benutzerverwaltung", "Benutzer hinzufügen");
	}
}
