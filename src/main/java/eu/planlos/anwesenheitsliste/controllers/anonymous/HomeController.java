package eu.planlos.anwesenheitsliste.controllers.anonymous;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_HOME;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_HOME;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;

@Controller
public class HomeController {

	@Autowired
	private BodyFillerService bf;
	
    @RequestMapping(path = URL_HOME)
    public String home(Model model) {
    	   	
	    bf.fill(model, "Öffentlich", "Startseite");
	    
    	return RES_HOME;
    }
}