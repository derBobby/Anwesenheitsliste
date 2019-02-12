package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_TEAMLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMADD;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMEDIT;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMLISTFULL;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMPHONELIST;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
	private SecurityService securityService;
	
	@Autowired
	private BodyFillerService bf;
	
	@Autowired
    private TeamService teamService;
	
	// User
	@RequestMapping(value = URL_TEAMLIST + "{markedTeamId}")
	public String markedTeamList(Model model, HttpSession session, Authentication auth, @PathVariable Long markedTeamId) {
		List<Team> teams = teamService.loadTeamsForUser(securityService.getLoginName(session));
		prepareContent(model, auth, teams, markedTeamId);
		return RES_TEAMLIST;
	}
	
	// User
	@RequestMapping(value = URL_TEAMLIST)
	public String teamList(Model model, HttpSession session, Authentication auth) {
		List<Team> teams = teamService.loadTeamsForUser(securityService.getLoginName(session));
		prepareContent(model, auth, teams, null);
		return RES_TEAMLIST;
	}
	
	// Admin
	@RequestMapping(value = URL_TEAMLISTFULL)
	public String teamListFull(Model model, Authentication auth) {
		List<Team> teams = teamService.loadAllTeams();
		model.addAttribute("functionAdd", URL_TEAMADD);		
		prepareContent(model, auth, teams, null);
		return RES_TEAMLIST;
	}
	
	// Admin
	@RequestMapping(value = URL_TEAMLISTFULL + "{markedTeamId}")
	public String markedteamListFull(Model model, Authentication auth, @PathVariable Long markedTeamId) {
		List<Team> teams = teamService.loadAllTeams();
		model.addAttribute("functionAdd", URL_TEAMADD);
		prepareContent(model, auth, teams, markedTeamId);
		return RES_TEAMLIST;
	}

	private void prepareContent(Model model, Authentication auth, List<Team> teams, Long markedTeamId) {
		
		List<String> headings = new ArrayList<String>();	
		headings.add("#");
		headings.add("Gruppenname");
		headings.add("Funktionen");
		model.addAttribute("headings", headings);
		
		model.addAttribute("teams", teams);
		model.addAttribute("markedTeamId", markedTeamId);

		model.addAttribute("functionEdit", URL_TEAMEDIT);
		model.addAttribute("functionMeetings", URL_MEETINGLIST);
		model.addAttribute("functionPhonelist", URL_TEAMPHONELIST);
		
		bf.fill(model, auth, STR_MODULE, STR_TITLE);
	}
}
