package eu.planlos.anwesenheitsliste.controllers.anonymous;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_HOME;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_HOME;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;

@Controller
public class HomeController {

	@Autowired
	private BodyFillerService bf;
	
    @RequestMapping(path = URL_HOME)
    public String home(Model model, Authentication auth) {
    	   	
	    bf.fill(model, auth, "Öffentlich", "Startseite");
	    
    	return RES_HOME;
    }
}