package eu.planlos.anwesenheitsliste.security;

import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.RES_LOGIN;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_LOGIN_FORM;

import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_LOGIN;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.planlos.anwesenheitsliste.uicontainer.LoginFormContainer;
import eu.planlos.anwesenheitsliste.viewhelper.BodyFiller;

@Controller
public class Login {
	
	public static String STR_MODULE = "Login";
	public static String STR_TITLE = "Login";
	
	@Autowired
	private BodyFiller bf;
	
	@GetMapping(path = URL_LOGIN_FORM)
	public String loginpage(Model model, HttpServletRequest request, @RequestParam(defaultValue = "false") Boolean error) {

		prepareContent(model, error);
		return RES_LOGIN;
	}
	
	private void prepareContent(Model model, Boolean error) {

		if(error) {
			model.addAttribute("error", "Login fehlgeschlagen!");
		}
		
		model.addAttribute("loginFormContainer", new LoginFormContainer());
		model.addAttribute("formAction", URL_LOGIN);
		
		bf.fill(model, STR_MODULE, STR_TITLE);
	}
}
