package eu.planlos.anwesenheitsliste.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_FA_TEST;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_FA_TEST;

@Controller
public class FontAwesomeTester {

	@RequestMapping(path = URL_FA_TEST)
	public String permissionOverview(Model model) {
		GeneralAttributeCreator.create(model, "Tests", "FontAwesome Testseite");
		return RES_FA_TEST;
	}
}
