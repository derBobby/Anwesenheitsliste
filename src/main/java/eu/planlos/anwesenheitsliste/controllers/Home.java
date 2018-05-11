package eu.planlos.anwesenheitsliste.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Home {

    @RequestMapping("/")
    public String home() {
		
    	return "index";
    }
}