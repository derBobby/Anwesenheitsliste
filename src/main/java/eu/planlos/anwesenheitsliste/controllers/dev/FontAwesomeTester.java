package eu.planlos.anwesenheitsliste.controllers.dev;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_FA_TEST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_FA_TEST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;

@Controller
public class FontAwesomeTester {

	@Autowired
	private BodyFillerService bf;

	@RequestMapping(path = URL_FA_TEST)
	public String permissionOverview(Model model) {
		bf.fill(model, "Tests", "FontAwesome Testseite");
		return RES_FA_TEST;
	}
}
