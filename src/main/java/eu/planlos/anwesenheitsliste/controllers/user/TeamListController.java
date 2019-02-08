package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_TEAMLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMLISTFULL;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMPHONELIST;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.SecurityService;
import eu.planlos.anwesenheitsliste.service.TeamService;

@Controller
public class TeamListController {

	public final String STR_MODULE = "Gruppenverwaltung";
	public final String STR_TITLE = "Liste der Gruppen";

	@Autowired
	private BodyFillerService bf;
	
	@Autowired
    private TeamService teamService;
	
	@Autowired
	private SecurityService securityService;

	// User
	@RequestMapping(value = URL_TEAMLIST + "{markedTeamId}")
	public String markedTeamList(Model model, @PathVariable Long markedTeamId) {
		
		String loginName = securityService.getLoginName();
		List<Team> teams = teamService.findTeamsForUser(loginName);

		prepareContent(model, teams, markedTeamId);
		return RES_TEAMLIST;
	}
	
	// User
	@RequestMapping(value = URL_TEAMLIST)
	public String teamList(Model model) {

		String loginName = securityService.getLoginName();
		List<Team> teams = teamService.findTeamsForUser(loginName);
		
		prepareContent(model, teams, null);
		return RES_TEAMLIST;
	}
	
	// Admin
	@RequestMapping(value = URL_TEAMLISTFULL)
	public String teamListFull(Model model) {
		
		List<Team> teams = teamService.findAll();
		
		//Admin function add
		model.addAttribute("functionAdd", URL_TEAM);
		
		prepareContent(model, teams, null);
		return RES_TEAMLIST;
	}
	
	// Admin
	@RequestMapping(value = URL_TEAMLISTFULL + "{markedTeamId}")
	public String markedteamListFull(Model model, @PathVariable Long markedTeamId) {
		
		List<Team> teams = teamService.findAll();
		
		//Admin function add
		model.addAttribute("functionAdd", URL_TEAM);
		
		prepareContent(model, teams, markedTeamId);
		return RES_TEAMLIST;
	}

	private void prepareContent(Model model, List<Team> teams, Long markedTeamId) {
		
		List<String> headings = new ArrayList<String>();	
		headings.add("#");
		headings.add("Gruppenname");
		headings.add("Funktionen");
		model.addAttribute("headings", headings);
		
		model.addAttribute("teams", teams);
		model.addAttribute("markedTeamId", markedTeamId);

		model.addAttribute("functionEdit", URL_TEAM);
		model.addAttribute("functionMeetings", URL_MEETINGLIST);
		model.addAttribute("functionPhonelist", URL_TEAMPHONELIST);
		
		bf.fill(model, STR_MODULE, STR_TITLE);
	}
}
