package eu.planlos.anwesenheitsliste.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class Start {

    @RequestMapping(path = "/")
    public String home(Model model) {
		
    	GeneralAttributeCreator.create(model, "Anwesenheitsliste", "Startseite");
    	return "start";
    }
}