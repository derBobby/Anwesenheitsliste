package eu.planlos.anwesenheitsliste.controllers.anonymous;

import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_DEFAULT;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_403;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_ERROR_UNKNOWN;
import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_ERROR_403;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.SecurityService;

@Controller
public class CustomErrorController implements ErrorController {

	private final ErrorAttributes errorAttributes;

	@Autowired
	private BodyFillerService bf;
	
	@Autowired
	private SecurityService securityService;

    @Autowired
    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }
    
	@GetMapping(path = URL_ERROR_403)
	public String forbidden(Model model) {
		
		bf.fill(model, "Fehler", "Verboten - 403");
		
		return RES_ERROR_403;
	}
	
	@GetMapping(path = URL_ERROR_DEFAULT)
	public String handleError(HttpServletRequest request, WebRequest webRequest, Model model) {
	
		//TODO generate error id and store in db
		//TODO keep only necessary
		if(securityService.isUserLoggedIn()) {
			
	        // Get error message.
	        String errorMessage = (String)request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

	        // Get exception object.
	        Exception errorException = (Exception)request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

	        // Get error stack trace map object. 
	        Map<String, Object> body = errorAttributes.getErrorAttributes(webRequest, true);
	        // Extract stack trace string.
	        String errorTrace = (String) body.get("trace");

	        model.addAttribute("errorMessage", errorMessage);
	        model.addAttribute("errorException", errorException);
	        model.addAttribute("errorTrace", errorTrace);
		}
		
		
		String title = "Unbekannter Fehler";
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	     
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	     
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	        	title = "Seite existiert nicht - 404";
	        }
	        else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	        	title = "Fehler im Server - 500";
	        }
	    }
	    
		bf.fill(model, "Fehler", title);
		
		return RES_ERROR_UNKNOWN;
	}

	@Override
	public String getErrorPath() {
		return URL_ERROR_DEFAULT;
	}
}
