package eu.planlos.anwesenheitsliste.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.model.ParticipantService;
import eu.planlos.anwesenheitsliste.model.TeamService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class ParticipationOverview {

	@Autowired
	private ParticipantService participantService;
	
	@Autowired
	private TeamService teamService;

	@RequestMapping(path = "/participationoverview")
	public String participationOverview(Model model) {
		
		model.addAttribute("teams", teamService.findAll());
		model.addAttribute("participants", participantService.findAll());
		
		GeneralAttributeCreator.create(model, "Übersicht", "Übersicht der Gruppenteilnahmen");
		
		return "overview/participationoverview";
	}
}
