package eu.planlos.anwesenheitsliste.controllers.dev;

import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_403_TEST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_403_TEST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;

@Controller
@Profile(value = "DEV")
public class Always403Controller {

	@Autowired
	private BodyFillerService bf;

	@RequestMapping(path = URL_403_TEST)
	public String permissionOverview(Model model) {
		bf.fill(model, "Tests", "Always 403 Seite");
		return RES_403_TEST;
	}
}
