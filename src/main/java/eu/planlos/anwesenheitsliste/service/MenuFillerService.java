package eu.planlos.anwesenheitsliste.service;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class MenuFillerService implements EnvironmentAware {

	@Autowired
	private Environment environment;
	
	public void fill(Model model) {

		model.addAttribute("URL_PERMISSIONSOVERVIEW", URL_PERMISSIONSOVERVIEW);	
		model.addAttribute("URL_PARTICIPATIONOVERVIEW", URL_PARTICIPATIONOVERVIEW);	
		model.addAttribute("URL_USERLIST", URL_USERLIST);	
		model.addAttribute("URL_MEETINGLISTFULL", URL_MEETINGLISTFULL);	
		model.addAttribute("URL_PARTICIPANTLIST", URL_PARTICIPANTLIST);	
		model.addAttribute("URL_TEAMLIST", URL_TEAMLIST);	
		model.addAttribute("URL_LOGOUT", URL_LOGOUT);	
		model.addAttribute("URL_LOGIN", URL_LOGIN);	
		model.addAttribute("URL_HOME", URL_HOME);	
		model.addAttribute("URL_ABOUT", URL_ABOUT);	
		model.addAttribute("URL_PRIVACY", URL_PRIVACY);
		
        for (final String profileName : environment.getActiveProfiles()) {
            if(profileName.equals("DEV")) {
            	model.addAttribute("URL_FA_TEST", URL_FA_TEST);
            	break;
            }
        }
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}