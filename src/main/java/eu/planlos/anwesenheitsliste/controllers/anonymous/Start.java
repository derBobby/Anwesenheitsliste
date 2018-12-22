package eu.planlos.anwesenheitsliste.controllers.anonymous;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_HOME;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_HOME;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_LOGIN;

@Controller
public class Start {

    @RequestMapping(path = URL_HOME)
    public String home(Model model) {
		
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	
    	Boolean isNull = (authentication == null);
    	//TODO check if possible: when Anonymous Authentication is enabled
    	Boolean isNotAuthenticated = ! authentication.isAuthenticated();
    	Boolean isAnonymous = authentication instanceof AnonymousAuthenticationToken;
    	
	    if(isNull || isNotAuthenticated || isAnonymous) {
	    	model.addAttribute("functionLogin", URL_LOGIN);
    	}
    	   	
    	GeneralAttributeCreator.create(model, "Anwesenheitsliste", "Startseite");
    	return RES_HOME;
    }
}