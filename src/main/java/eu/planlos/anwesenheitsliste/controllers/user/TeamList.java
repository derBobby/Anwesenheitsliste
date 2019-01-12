package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_TEAMLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_MEETINGLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_TEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_TEAMLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_TEAMPHONELIST;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.TeamService;

@Controller
public class TeamList {

	public final String STR_MODULE = "Gruppenverwaltung";
	public final String STR_TITLE = "Liste der Gruppen";

	@Autowired
	private BodyFillerService bf;
	
	@Autowired
    private TeamService teamService;

	@RequestMapping(value = URL_TEAMLIST + "{markedTeamId}")
	public String markedTeamList(Model model, @PathVariable Long markedTeamId) {
		
		prepareContent(model, markedTeamId);
		return RES_TEAMLIST;
	}
	
	@RequestMapping(value = URL_TEAMLIST)
	public String teamList(Model model) {
		
		prepareContent(model, null);
		return RES_TEAMLIST;
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

		model.addAttribute("functionEdit", URL_TEAM);
		model.addAttribute("functionMeetings", URL_MEETINGLIST);
		model.addAttribute("functionPhonelist", URL_TEAMPHONELIST);
		model.addAttribute("functionAdd", URL_TEAM);
		
		bf.fill(model, STR_MODULE, STR_TITLE);
	}
}
