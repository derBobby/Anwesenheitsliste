package eu.planlos.anwesenheitsliste.controllers.anonymous;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_403;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_403;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;

@Controller
public class ErrorController {

	@Autowired
	private BodyFillerService bf;
	
	@GetMapping(path = URL_403)
	public String forbidden(Model model) {
		
		bf.fill(model, "Fehler", "Verboten - 403");
		
		return RES_403;
	}
}
