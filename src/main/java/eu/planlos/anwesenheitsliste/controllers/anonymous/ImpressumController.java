package eu.planlos.anwesenheitsliste.controllers.anonymous;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_IMPRESSUM;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_IMPRESSUM;

@Controller
public class ImpressumController {

	@Autowired
	private BodyFillerService bf;

	@GetMapping(path = URL_IMPRESSUM)
	public String impressum(Model model) {

		bf.fill(model, "Öffentlich", "Impressum");
		
		return RES_IMPRESSUM;
	}
}
