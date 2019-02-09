package eu.planlos.anwesenheitsliste.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import eu.planlos.anwesenheitsliste.ApplicationRole;

@Component
public class LoginAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
 
	private static final Logger logger = LoggerFactory.getLogger(LoginAuthenticationSuccessHandler.class);	

	public static final String REDIRECT_URL_SESSION_ATTRIBUTE_NAME = "REDIRECT_URL";
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(ApplicationRole.ROLE_ADMIN))) {
			request.getSession().setAttribute("isAdmin", true);
		}
		
		logger.error("Erfolgreicher Loginversuch für : \"" + authentication.getName() + "\"");

		super.onAuthenticationSuccess(request, response, authentication);
	}

}
