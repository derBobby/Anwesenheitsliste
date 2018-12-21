package eu.planlos.anwesenheitsliste.viewhelper;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;

public final class GeneralAttributeCreator {

	public static void create(Model model, String module, String title) {

//TODO
//		Map<String, > sites		
//		List<String> sites = new ArrayList<>();
//		model.addAttribute("URL_USERLIST", URL_USERLIST);
				
		model.addAttribute("auth", SecurityContextHolder.getContext().getAuthentication()); 
		model.addAttribute("session", RequestContextHolder.currentRequestAttributes().getSessionId());
		model.addAttribute("module", module);
		model.addAttribute("title", title);				
	}
}