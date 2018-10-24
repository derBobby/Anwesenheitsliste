package eu.planlos.anwesenheitsliste.controllers.anonymous;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_HOME;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_HOME;

@Controller
public class Start {

    @RequestMapping(path = URL_HOME)
    public String home(Model model) {
		
    	GeneralAttributeCreator.create(model, "Anwesenheitsliste", "Startseite");
    	return RES_HOME;
    }
}