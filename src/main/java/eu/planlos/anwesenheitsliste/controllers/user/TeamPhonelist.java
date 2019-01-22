package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_TEAMPHONELIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_403;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMPHONELIST;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.ParticipantService;
import eu.planlos.anwesenheitsliste.service.SecurityService;
import eu.planlos.anwesenheitsliste.service.TeamService;

@Controller
public class TeamPhonelist {

	public final String STR_MODULE = "Gruppenverwaltung";
	public final String STR_TITLE = "Telefonliste";

	@Autowired
	private BodyFillerService bf;

	@Autowired
    private ParticipantService participantService;
	
	@Autowired
    private TeamService teamService;
	
	@Autowired
    private SecurityService securityService;

	@RequestMapping(value = URL_TEAMPHONELIST + "{idTeam}")
	public String markedTeamList(Model model, @PathVariable Long idTeam) {
				
		if(!securityService.isAdmin() && !securityService.isUserAuthorizedForTeam(idTeam)) {
			return "redirect:" + URL_403;
		}
		
		prepareContent(model, idTeam);
		return RES_TEAMPHONELIST;
	}
	
	private void prepareContent(Model model, Long idTeam) {
		
		List<String> headings = new ArrayList<String>();	
		headings.add("#");
		headings.add("Teilnehmer");
		headings.add("Telefonnummer");
		headings.add("Aktiv");
		model.addAttribute("headings", headings);

		List<Participant> participants = participantService.findAllByTeamsIdTeam(idTeam);
		model.addAttribute("participants", participants);

		Team team = teamService.findById(idTeam);
		
		bf.fill(model, STR_MODULE, STR_TITLE + " für " + team.getTeamName());
	}
}
