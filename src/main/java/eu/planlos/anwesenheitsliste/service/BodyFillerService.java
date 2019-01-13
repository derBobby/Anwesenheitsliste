package eu.planlos.anwesenheitsliste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;

@Service
public class BodyFillerService implements EnvironmentAware {

	@Autowired
	private Environment environment;

	@Autowired
	private MenuFillerService mc;
	
	public void fill(Model model, String module, String title) {
		
        for (final String profileName : environment.getActiveProfiles()) {
            if(profileName.equals("DEV")) {
        		model.addAttribute("auth", SecurityContextHolder.getContext().getAuthentication()); 
        		model.addAttribute("mysession", RequestContextHolder.currentRequestAttributes().getSessionId());
        		break;
            }
        }

		model.addAttribute("module", module);
		model.addAttribute("title", title);		
		
		mc.fill(model);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}