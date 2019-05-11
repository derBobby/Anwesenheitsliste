package eu.planlos.anwesenheitsliste.controllers.dev;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_FA_TEST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_FA_TEST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.ApplicationProfile;
import eu.planlos.anwesenheitsliste.service.BodyFillerService;

@Controller
@Profile(value = ApplicationProfile.DEV_PROFILE)
public class FontAwesomeTester {

	private static final Logger logger = LoggerFactory.getLogger(FontAwesomeTester.class);
			
	@Autowired
	private BodyFillerService bf;

	@RequestMapping(path = URL_FA_TEST)
	public String permissionOverview(Model model, Authentication auth) {
		logger.error("Testcontroller wurde aufgerufen: " + this.getClass().toString());
		bf.fill(model, auth, "Tests", "FontAwesome Testseite");
		return RES_FA_TEST;
	}
}
