package eu.planlos.anwesenheitsliste.controllers.admin;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_PARTICIPATIONOVERVIEW;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_PARTICIPATIONOVERVIEW;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.ParticipantService;
import eu.planlos.anwesenheitsliste.service.TeamService;

@Controller
public class ParticipationOverviewController {

	public final String STR_MODULE = "Übersicht";
	public final String STR_TITLE = "Übersicht der Gruppenteilnahmen";

	@Autowired
	private BodyFillerService bf;
	
	@Autowired
	private ParticipantService participantService;
	
	@Autowired
	private TeamService teamService;

	@RequestMapping(path = URL_PARTICIPATIONOVERVIEW)
	public String participationOverview(Model model, Authentication auth) {
		
		model.addAttribute("teams", teamService.loadAllTeams());
		model.addAttribute("participants", participantService.loadAllParticipants());
		
		bf.fill(model, auth, STR_MODULE, STR_TITLE);
		
		return RES_PARTICIPATIONOVERVIEW;
	}
}
