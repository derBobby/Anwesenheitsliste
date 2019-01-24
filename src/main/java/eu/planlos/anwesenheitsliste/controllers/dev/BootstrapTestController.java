package eu.planlos.anwesenheitsliste.controllers.dev;

import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_BS_TEST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_BS_TEST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;

@Controller
@Profile(value = "DEV")
public class BootstrapTestController {

	@Autowired
	private BodyFillerService bf;

	@RequestMapping(path = URL_BS_TEST)
	public String permissionOverview(Model model) {
		bf.fill(model, "Tests", "FontAwesome Testseite");
		return RES_BS_TEST;
	}
}