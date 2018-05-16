package eu.planlos.anwesenheitsliste.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.TeamService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class TeamList {

	@Autowired
    private TeamService teamService;

	@RequestMapping(value = "/teamlist/{markedTeamId}")
	public String markedTeamList(Model model, @PathVariable Long markedTeamId) {
		
		prepareContent(model, markedTeamId);
		return "list/teamlist";
	}
	
	@RequestMapping(value = "/teamlist")
	public String teamList(Model model) {
		
		prepareContent(model, null);
		return "list/teamlist";
	}

	private void prepareContent(Model model, Long markedTeamId) {
		
		List<String> headings = new ArrayList<String>();	
		headings.add("#");
		headings.add("Gruppenname");
		headings.add("Funktionen");

		List<Team> teams = teamService.findAll();
		
		model.addAttribute("headings", headings);
		model.addAttribute("teams", teams);
		model.addAttribute("markedTeamId", markedTeamId);
		model.addAttribute("newButtonText", "Neue Gruppe");
		model.addAttribute("newButtonUrl", "teamadd");
		
		GeneralAttributeCreator.create(model, "Gruppenverwaltung", "Liste der Gruppen");
	}
}
