package eu.planlos.anwesenheitsliste.controllers.admin;

import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.RES_FA_TEST;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_FA_TEST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.viewhelper.BodyFiller;

@Controller
public class FontAwesomeTester {

	@Autowired
	private BodyFiller bf;

	@RequestMapping(path = URL_FA_TEST)
	public String permissionOverview(Model model) {
		bf.fill(model, "Tests", "FontAwesome Testseite");
		return RES_FA_TEST;
	}
}
