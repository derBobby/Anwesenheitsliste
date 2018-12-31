package eu.planlos.anwesenheitsliste.security;

import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.RES_LOGIN;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_LOGIN;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import eu.planlos.anwesenheitsliste.uicontainer.LoginFormContainer;
import eu.planlos.anwesenheitsliste.viewhelper.BodyFiller;

@Controller
public class Login {
	
	public static String STR_MODULE = "Login";
	public static String STR_TITLE = "Login";
	
	@Autowired
	private BodyFiller bf;
	
	@GetMapping(path = URL_LOGIN)
	public String loginpage(Model model) {
		
		prepareContent(model);
		return RES_LOGIN;
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
		model.addAttribute("formAction", URL_LOGIN);
		bf.fill(model, STR_MODULE, STR_TITLE);
	}
}
