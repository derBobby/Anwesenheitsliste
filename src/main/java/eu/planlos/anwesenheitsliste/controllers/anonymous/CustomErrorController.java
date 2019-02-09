package eu.planlos.anwesenheitsliste.controllers.anonymous;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_403;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_DEFAULT;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_ERROR_UNKNOWN;
import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_ERROR_403;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.MailService;

@Controller
public class CustomErrorController implements ErrorController {

	private final ErrorAttributes errorAttributes;

	private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

	@Autowired
	private BodyFillerService bf;	
	
	@Autowired
	private MailService errorMailNotificationService;

    @Autowired
    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }
    
    /**
     * Will be used from Spring Boot if user is authenticated but not authorised.
     * Method call is triggered/configured in @see SecurityConfiguration
     * @param model
     * @param auth
     * @return
     */
	@GetMapping(path = URL_ERROR_403)
	public String forbidden(Model model, Authentication auth) {
			
		if(auth != null) {
			logger.error("Benutzer \"" + auth.getName() + "\" wollte unauthorisiert auf eine Seite zugreifen.");
		} else {
			logger.error("Ein nicht authentifizierter Benutzer wollte unauthorisiert auf eine Seite zugreifen.");			
		}
		
		bf.fill(model, "Fehler", "Verboten - 403");
		
		return RES_ERROR_403;
	}
	
	//TODO NEW prevent stacktrace from being written to log
	@GetMapping(path = URL_ERROR_DEFAULT)
	public String handleError(HttpServletRequest request, Authentication auth, WebRequest webRequest, Model model) {
	
        String errorMessage = (String)request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Exception errorException = (Exception)request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        // Get error stack trace map object
        Map<String, Object> body = errorAttributes.getErrorAttributes(webRequest, true);
        // Extract stack trace string
        String errorTrace = (String) body.get("trace");
	        
		if(auth != null) {
			
			model.addAttribute("printTrace", true);
	        model.addAttribute("errorMessage", errorMessage);
	        model.addAttribute("errorException", errorException);
	        //TODO not for 404
	        model.addAttribute("errorTrace", errorTrace);
		}
		
		String title = "Unbekannter Fehler";
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	     
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());

	        if(statusCode == HttpStatus.FORBIDDEN.value()) {
	        	return forbidden(model, auth);
	        }
	        
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	        	title = "Seite existiert nicht - 404";
	        }
	        
	        if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	        	title = "Fehler im Server - 500";
	        }
	    }
	    
	    // Send email notification
	    errorMailNotificationService.sendErrorNotification(title, errorMessage, errorException, errorTrace);
	    
	    model.addAttribute("status", status);
		bf.fill(model, "Fehler", title);
		
		return RES_ERROR_UNKNOWN;
	}

	@Override
	public String getErrorPath() {
		return URL_ERROR_DEFAULT;
	}
}
