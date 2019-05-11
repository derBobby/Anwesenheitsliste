package eu.planlos.anwesenheitsliste.controllers.dev;

import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_500_TEST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_500_TEST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.ApplicationProfile;

@Controller
@Profile(value = ApplicationProfile.DEV_PROFILE)
public class Always500Controller {
	
	private static final Logger logger = LoggerFactory.getLogger(Always500Controller.class);
			
	@RequestMapping(path = URL_500_TEST)
	public String permissionOverview(Model model) {
		logger.error("Testcontroller wurde aufgerufen: " + this.getClass().toString());
		return RES_500_TEST;
	}
}
