package eu.planlos.anwesenheitsliste.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.planlos.anwesenheitsliste.model.Meeting;
import eu.planlos.anwesenheitsliste.model.MeetingService;
import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.ParticipantService;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.TeamService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class MeetingEdit {

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private ParticipantService participantService;
	
	@RequestMapping(path = "/meetingedit/{idMeeting}", method = RequestMethod.GET)
	public String showForm(Model model, @PathVariable Long idMeeting) {
		
		prepareContent(model, idMeeting);
		return "detail/meetingdetail";
	}
	
	@RequestMapping(path = "/meetingedit", method = RequestMethod.POST)
	public String edit(Model model, @Valid @ModelAttribute Meeting meeting, Errors errors) {
		
		if(errors.hasErrors()) {
			prepareContent(model, meeting.getIdMeeting());
			return "detail/meetingdetail";
		}
		
		meetingService.save(meeting);
		
		return "redirect:/meetinglist/forteam/" + meeting.getTeam().getIdTeam() + "/marked/" + meeting.getIdMeeting();
	}
	
	private void prepareContent(Model model, Long idMeeting) {

		Meeting meeting = meetingService.findById(idMeeting);
		model.addAttribute(meeting);
		
		Long idTeam = meeting.getTeam().getIdTeam();
		
		Team team = teamService.findById(idTeam);
		
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		
		List<Participant> participants = participantService.findAllByTeamIdTeam(idTeam);
		
		model.addAttribute("teams", teams);
		model.addAttribute("participants", participants);
		
		model.addAttribute("buttonText", "Termin ändern");
		model.addAttribute("buttonFunction", "meetingedit");
		
		GeneralAttributeCreator generalAttributeCreator = new GeneralAttributeCreator();
		generalAttributeCreator.create(model, "Terminverwaltung", "Termin hinzufügen");
	}
}
