package eu.planlos.anwesenheitsliste.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import eu.planlos.anwesenheitsliste.uicontainer.LoginFormContainer;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class Login {
	
	
	@GetMapping(path = "/login")
	public String loginpage(Model model) {
		
		prepareContent(model);
		return "session/login";
	}
	
//	@PostMapping(path = "/login")
//	public String submit(Model model, @Valid @ModelAttribute LoginFormContainer loginFormContainer, Errors errors) {
//		
//		if(errors.hasErrors()) {
//			return "session/loginpage"; 
//		}
//		
//		return "redirect:/";
//	}
//		
//	@PostMapping(path = "/loginsuccess")
//	public String loginsuccess(Model model) {
//		
//		prepareContent(model);
//		return "session/loginsuccess";
//	}
	
	private void prepareContent(Model model) {

		model.addAttribute("loginFormContainer", new LoginFormContainer());
		GeneralAttributeCreator.create(model, "Login", "Login");
	}
}
