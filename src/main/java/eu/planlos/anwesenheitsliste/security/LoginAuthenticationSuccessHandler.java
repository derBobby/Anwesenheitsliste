package eu.planlos.anwesenheitsliste.security;

import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_HOME;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import eu.planlos.anwesenheitsliste.SampleDataCreater;

@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(SampleDataCreater.class);	

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication auth) throws IOException, ServletException {

		logger.error("Erfolgreicher Loginversuch für : \"" + auth.getName() + "\" auf " + request.getRequestURI());
		
		// set our response to OK status
		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect(URL_HOME);
	}

}
