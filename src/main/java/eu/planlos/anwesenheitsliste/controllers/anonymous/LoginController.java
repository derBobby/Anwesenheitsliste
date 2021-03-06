package eu.planlos.anwesenheitsliste.controllers.anonymous;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_LOGIN;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_LOGIN;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_LOGIN_FORM;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.planlos.anwesenheitsliste.model.LoginFormContainer;
import eu.planlos.anwesenheitsliste.service.BodyFillerService;

@Controller
public class LoginController {
	
	public static String STR_MODULE = "Login";
	public static String STR_TITLE = "Login";
	
	@Autowired
	private BodyFillerService bf;
	
	@GetMapping(path = URL_LOGIN_FORM)
	public String loginpage(Model model, Authentication auth, HttpServletRequest request, @RequestParam(defaultValue = "false") boolean error) {

		prepareContent(model, auth, error);
		return RES_LOGIN;
	}
	
	private void prepareContent(Model model, Authentication auth, boolean error) {

		if(error) {
			model.addAttribute("error", "Login fehlgeschlagen!");
		}
		
		model.addAttribute("loginFormContainer", new LoginFormContainer());
		model.addAttribute("formAction", URL_LOGIN);
		
		bf.fill(model, auth, STR_MODULE, STR_TITLE);
	}
}
