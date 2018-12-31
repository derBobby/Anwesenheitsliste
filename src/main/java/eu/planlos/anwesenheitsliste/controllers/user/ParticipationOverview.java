package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.RES_PARTICIPATIONOVERVIEW;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_PARTICIPATIONOVERVIEW;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.model.ParticipantService;
import eu.planlos.anwesenheitsliste.model.TeamService;
import eu.planlos.anwesenheitsliste.viewhelper.BodyFiller;

@Controller
public class ParticipationOverview {

	public final String STR_MODULE = "Übersicht";
	public final String STR_TITLE = "Übersicht der Gruppenteilnahmen";

	@Autowired
	private BodyFiller bf;
	
	@Autowired
	private ParticipantService participantService;
	
	@Autowired
	private TeamService teamService;

	@RequestMapping(path = URL_PARTICIPATIONOVERVIEW)
	public String participationOverview(Model model) {
		
		model.addAttribute("teams", teamService.findAll());
		model.addAttribute("participants", participantService.findAll());
		
		bf.fill(model, STR_MODULE, STR_TITLE);
		
		return RES_PARTICIPATIONOVERVIEW;
	}
}
