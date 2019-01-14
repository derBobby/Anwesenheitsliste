package eu.planlos.anwesenheitsliste.controllers.anonymous;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_PRIVACY;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_PRIVACY;

@Controller
public class DatenschutzController {

	@Autowired
	private BodyFillerService bf;

	@GetMapping(path = URL_PRIVACY)
	public String datenschutz(Model model) {
		
		bf.fill(model, "Öffentlich", "Datenschutz");
		
		return RES_PRIVACY;
	}
	
}
