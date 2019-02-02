package eu.planlos.anwesenheitsliste.controllers.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_PARTICIPANTLISTFULL;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_PARTICIPANT;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_PARTICIPANTLIST;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_PARTICIPANTLIST;

@Controller
public class ParticipantList {

	public final String STR_MODULE = "Teilnehmerverwaltung";
	public final String STR_TITLE = "Liste der Teilnehmer";

	@Autowired
	private BodyFillerService bf;

	@Autowired
	private ParticipantService participantService;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private SecurityService securityService;

	// User
	@RequestMapping(path = URL_PARTICIPANTLIST + "{markedParticipantId}")
	public String markedParticipantList(Model model, @PathVariable Long markedParticipantId) {
	
		prepareContent(model, participantsForUser(), markedParticipantId);
		return RES_PARTICIPANTLIST;
	}

	// User
	@RequestMapping(path = URL_PARTICIPANTLIST)
	public String participantList(Model model) {

		prepareContent(model, participantsForUser(), null);
		return RES_PARTICIPANTLIST;
	}

	private List<Participant> participantsForUser() {
		
		String loginName = securityService.getLoginName();
		List<Team> teams = teamService.findTeamsForUser(loginName);
		
		Set<Participant> participants = new HashSet<>();
		
		for(Team team : teams) {
			participants.addAll(team.getParticipants());
		}
				
		return new ArrayList<Participant>(participants);
	}

	// Admin
	@RequestMapping(path = URL_PARTICIPANTLISTFULL)
	public String participantListFull(Model model) {

		List<Participant> participants = participantService.findAll();

		prepareContent(model, participants, null);
		return RES_PARTICIPANTLIST;
	}
	
	// Admin
	@RequestMapping(path = URL_PARTICIPANTLISTFULL + "{markedParticipantId}")
	public String markedParticipantListFull(Model model, @PathVariable Long markedParticipantId) {

		List<Participant> participants = participantService.findAll();

		prepareContent(model, participants, markedParticipantId);
		return RES_PARTICIPANTLIST;
	}

	private void prepareContent(Model model, List<Participant> participants, Long markedParticipantId) {

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

		bf.fill(model, STR_MODULE, STR_TITLE);
	}
}
