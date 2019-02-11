package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_TEAMPHONELIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_403;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_TEAMPHONELIST;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
import eu.planlos.anwesenheitsliste.SessionAttributes;

@Controller
public class TeamPhonelistController {

	public final String STR_MODULE = "Gruppenverwaltung";
	public final String STR_TITLE = "Telefonliste";

	private static final Logger logger = LoggerFactory.getLogger(TeamPhonelistController.class);
	
	@Autowired
	private BodyFillerService bf;

	@Autowired
    private ParticipantService participantService;
	
	@Autowired
    private TeamService teamService;
	
	@Autowired
    private SecurityService securityService;

	@RequestMapping(value = URL_TEAMPHONELIST + "{idTeam}")
	public String teamPhoneList(Model model, Authentication auth, HttpSession session, Principal principal, @PathVariable Long idTeam) {

		String loginName = principal.getName();
		boolean isAdmin = (boolean) session.getAttribute(SessionAttributes.ISADMIN);
		
		if(!isAdmin && !securityService.isUserAuthorizedForTeam(idTeam, loginName)) {
			logger.error("Benutzer \"" + loginName + "\" hat unauthorisiert versucht auf Telefonliste von Gruppe id=" + idTeam + " zuzugreifen.");
			return "redirect:" + URL_ERROR_403;
		}
		
		prepareContent(model, auth, idTeam);
		return RES_TEAMPHONELIST;
	}
	
	private void prepareContent(Model model, Authentication auth, Long idTeam) {
		
		List<String> headings = new ArrayList<String>();	
		headings.add("#");
		headings.add("Teilnehmer");
		headings.add("Telefonnummer");
		headings.add("Aktiv");
		model.addAttribute("headings", headings);

		List<Participant> participants = participantService.loadParticipantsForTeam(idTeam);
		model.addAttribute("participants", participants);

		Team team = teamService.loadTeam(idTeam);
		
		bf.fill(model, auth, STR_MODULE, STR_TITLE + " für " + team.getTeamName());
	}
}
