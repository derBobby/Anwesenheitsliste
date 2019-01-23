package eu.planlos.anwesenheitsliste.controllers.dev;

import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_500_TEST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_500_TEST;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Profile(value = "DEV")
public class Always500Controller {

	@RequestMapping(path = URL_500_TEST)
	public String permissionOverview(Model model) {
		return RES_500_TEST;
	}
}
