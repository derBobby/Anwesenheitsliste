package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_PARTICIPANTLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_PARTICIPANT;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_PARTICIPANTLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_PARTICIPANTLISTFULL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

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

@Controller
public class ParticipantListController {

	public final String STR_MODULE = "Teilnehmerverwaltung";
	public final String STR_TITLE = "Liste der Teilnehmer";

	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private BodyFillerService bf;

	@Autowired
	private ParticipantService participantService;
	
	@Autowired
	private TeamService teamService;

	// User
	@RequestMapping(path = URL_PARTICIPANTLIST + "{markedParticipantId}")
	public String markedParticipantList(Model model, HttpSession session, Authentication auth, @PathVariable Long markedParticipantId) {
		prepareContent(model, auth, participantsForUser(securityService.getLoginName(session)), markedParticipantId);
		return RES_PARTICIPANTLIST;
	}

	// User
	@RequestMapping(path = URL_PARTICIPANTLIST)
	public String participantList(Model model, HttpSession session, Authentication auth) {
		prepareContent(model, auth, participantsForUser(securityService.getLoginName(session)), null);
		return RES_PARTICIPANTLIST;
	}

	private List<Participant> participantsForUser(String loginName) {
		List<Team> teams = teamService.loadTeamsForUser(loginName);
		Set<Participant> participants = new HashSet<>();
		for(Team team : teams) {
			participants.addAll(team.getParticipants());
		}
		return new ArrayList<Participant>(participants);
	}

	// Admin
	@RequestMapping(path = URL_PARTICIPANTLISTFULL)
	public String participantListFull(Model model, Authentication auth) {
		List<Participant> participants = participantService.loadAllParticipants();
		prepareContent(model, auth, participants, null);
		return RES_PARTICIPANTLIST;
	}
	
	// Admin
	@RequestMapping(path = URL_PARTICIPANTLISTFULL + "{markedParticipantId}")
	public String markedParticipantListFull(Model model, Authentication auth, @PathVariable Long markedParticipantId) {
		List<Participant> participants = participantService.loadAllParticipants();
		prepareContent(model, auth, participants, markedParticipantId);
		return RES_PARTICIPANTLIST;
	}

	private void prepareContent(Model model, Authentication auth, List<Participant> participants, Long markedParticipantId) {

		List<String> headings = new ArrayList<>();
		headings.add("#");
		headings.add("Vorname");
		headings.add("Nachname");
		headings.add("Telefon");
		headings.add("E-Mail");
		headings.add("Aktiv");
		headings.add("Funktionen");
		model.addAttribute("headings", headings);

		model.addAttribute("participants", participants);
		model.addAttribute("markedParticipantId", markedParticipantId);

		model.addAttribute("functionEdit", URL_PARTICIPANT);
		model.addAttribute("functionAdd", URL_PARTICIPANT);

		bf.fill(model, auth, STR_MODULE, STR_TITLE);
	}
}
