package eu.planlos.anwesenheitsliste.service;

import static eu.planlos.anwesenheitsliste.ApplicationPath.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class MenuFillerService implements EnvironmentAware {

	private static final Logger logger = LoggerFactory.getLogger(MenuFillerService.class);
	
	@Autowired
	private Environment environment;
	
	public void fill(Model model) {

		model.addAttribute("URL_HOME", URL_HOME);	
		
		/*
		 * User Menu
		 */
		model.addAttribute("URL_MEETINGCHOOSETEAM", URL_MEETINGCHOOSETEAM);
		model.addAttribute("URL_PARTICIPANTLIST", URL_PARTICIPANTLIST);	
		model.addAttribute("URL_TEAMLIST", URL_TEAMLIST);
		
		
		/*
		 * Admin Menu
		 */
		model.addAttribute("URL_TEAMLISTFULL", URL_TEAMLISTFULL);
		model.addAttribute("URL_PARTICIPANTLISTFULL", URL_PARTICIPANTLISTFULL);	
		model.addAttribute("URL_MEETINGLISTFULL", URL_MEETINGLISTFULL);	
		model.addAttribute("URL_USERLIST", URL_USERLIST);	

		model.addAttribute("URL_PERMISSIONSOVERVIEW", URL_PERMISSIONSOVERVIEW);	
		model.addAttribute("URL_PARTICIPATIONOVERVIEW", URL_PARTICIPATIONOVERVIEW);	
		
		// URLs for DEV profile
		for (final String profileName : environment.getActiveProfiles()) {
			if(profileName.equals("DEV")) {
            	logger.debug("Preparing menu model for DEV profile.");

				model.addAttribute("URL_FA_TEST", URL_FA_TEST);
				model.addAttribute("URL_403_TEST", URL_403_TEST);
				model.addAttribute("URL_500_TEST", URL_500_TEST);
				model.addAttribute("URL_BS_TEST", URL_BS_TEST);
				break;
		}	}

		
		/*
		 * Account Menu
		 */
		//TODO NEW Activities missing
		//TODO NEW Config missing
		model.addAttribute("URL_LOGOUT", URL_LOGOUT);	
		
		
		/*
		 * Login URL only if not logged in
		 */
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    
    	if(authentication == null
    			|| ! authentication.isAuthenticated()
    			|| authentication instanceof AnonymousAuthenticationToken) {
    		
        	logger.debug("Adding login form URL.");
	    	model.addAttribute("URL_LOGIN_FORM", URL_LOGIN_FORM);
    	}
	    
		/*
		 * Info Menu
		 */
		model.addAttribute("URL_IMPRESSUM", URL_IMPRESSUM);	
		model.addAttribute("URL_DATENSCHUTZ", URL_DATENSCHUTZ);
		
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}