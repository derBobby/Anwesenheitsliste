package eu.planlos.anwesenheitsliste.viewhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;

@Service
public class BodyFiller {

	@Autowired
	private MenuFiller mc;
	
	public void fill(Model model, String module, String title) {
				
		model.addAttribute("auth", SecurityContextHolder.getContext().getAuthentication()); 
		model.addAttribute("session", RequestContextHolder.currentRequestAttributes().getSessionId());
		model.addAttribute("module", module);
		model.addAttribute("title", title);		
		
		mc.fill(model);
	}
}