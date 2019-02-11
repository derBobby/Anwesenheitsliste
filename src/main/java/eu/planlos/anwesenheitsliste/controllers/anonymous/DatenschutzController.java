package eu.planlos.anwesenheitsliste.controllers.anonymous;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;

import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_DATENSCHUTZ;
import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_DATENSCHUTZ;

@Controller
public class DatenschutzController {

	@Autowired
	private BodyFillerService bf;

	@GetMapping(path = URL_DATENSCHUTZ)
	public String datenschutz(Model model, Authentication auth) {
		
		bf.fill(model, auth, "Öffentlich", "Datenschutz");
		
		return RES_DATENSCHUTZ;
	}
	
}
