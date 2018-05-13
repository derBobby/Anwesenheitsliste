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
public class MeetingAdd {

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private ParticipantService participantService;
	
	@RequestMapping(path = "/meetingadd/forteam/{idTeam}", method = RequestMethod.GET)
	public String meetingAddForTeamForm(Model model, @PathVariable Long idTeam) {

		Team team = teamService.findById(idTeam);
		
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		
		model.addAttribute("teams", teams);
		model.addAttribute("meeting", new Meeting(team));
		model.addAttribute("participants", participantService.findAllByTeamIdTeam(idTeam));
		prepareContent(model);
		
		return "detail/meetingdetail";
	}
	
	@RequestMapping(path = "/meetingadd/chooseteam", method = RequestMethod.GET)
	public String meetingAdd(Model model) {

		model.addAttribute("teams", teamService.findAll());
		model.addAttribute("meeting", new Meeting());
		model.addAttribute("participants", new ArrayList<Participant>());
		prepareContent(model);
		
		return "detail/meetingdetail";
	}

	@RequestMapping(path = "/meetingadd", method = RequestMethod.POST)
	public String meetingAddForTeamSubmit(Model model, @Valid @ModelAttribute Meeting meeting, Errors errors) {
		
		if(errors.hasErrors()) {
			prepareContent(model);
			return "detail/meetingdetail";
		}
		
		Meeting newMeeting = meetingService.save(meeting);
		return "redirect:/meetinglist/forteam/" + newMeeting.getTeam().getIdTeam() + "/marked/" + newMeeting.getIdMeeting();
	}
	
	private void prepareContent(Model model) {
	
		model.addAttribute("buttonText", "Termin hinzufügen");
		model.addAttribute("buttonFunction", "meetingadd");
		
		GeneralAttributeCreator generalAttributeCreator = new GeneralAttributeCreator();
		generalAttributeCreator.create(model, "Terminverwaltung", "Termin hinzufügen");
	}
}
