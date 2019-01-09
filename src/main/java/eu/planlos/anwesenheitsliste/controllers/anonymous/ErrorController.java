package eu.planlos.anwesenheitsliste.controllers.anonymous;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import eu.planlos.anwesenheitsliste.viewhelper.BodyFiller;

import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_403;

import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.RES_403;

@Controller
public class ErrorController {

	@Autowired
	private BodyFiller bf;
	
	@GetMapping(path = URL_403)
	public String forbidden(Model model) {
		
		bf.fill(model, "Fehler", "Verboten - 403");
		
		return RES_403;
	}
}
