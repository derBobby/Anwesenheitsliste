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
import eu.planlos.anwesenheitsliste.model.ParticipantService;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.TeamService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class MeetingDetail {

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private ParticipantService participantService;
	
	@RequestMapping(path = "/meetingdetail/forteam/{idTeam}/{idMeeting}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable Long idTeam, @PathVariable Long idMeeting) {
	
		Team team = teamService.findById(idTeam);
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		
		model.addAttribute("teams", teams);		
		model.addAttribute(meetingService.findById(idMeeting));
		model.addAttribute("participants", participantService.findAllByTeamIdTeam(idTeam));
		GeneralAttributeCreator.create(model, "Terminverwaltung", "Termin ändern");
		
		return "detail/meetingdetail";
	}
	
	@RequestMapping(path = "/meetingdetail/forteam/{idTeam}", method = RequestMethod.GET)
	public String add(Model model, @PathVariable Long idTeam) {

		Team team = teamService.findById(idTeam);
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		
		model.addAttribute("teams", teams);
//		model.addAttribute("meeting", new Meeting(team));
		model.addAttribute("meeting", new Meeting());
		model.addAttribute("participants", participantService.findAllByTeamIdTeam(idTeam));
		GeneralAttributeCreator.create(model, "Terminverwaltung", "Termin hinzufügen");
		
		return "detail/meetingdetail";
	}
	
	@RequestMapping(path = "/meetingdetail/chooseteam", method = RequestMethod.GET)
	public String add(Model model) {
		
		model.addAttribute("teams", teamService.findAll());
		model.addAttribute("meeting", new Meeting());
		model.addAttribute("participants", new ArrayList<>()); //TODO No participants. Which to load? JQuery?
		GeneralAttributeCreator.create(model, "Terminverwaltung", "Termin hinzufügen");
			
		return "detail/meetingdetail";
	}

	@RequestMapping(path = "/meetingdetail", method = RequestMethod.POST)
	public String submit(Model model, @Valid @ModelAttribute Meeting meeting, Errors errors) {
		
		if(errors.hasErrors()) {
			
			if(meeting.getIdMeeting() != null) {
				GeneralAttributeCreator.create(model, "Terminverwaltung", "Termin ändern");
			} else {
				GeneralAttributeCreator.create(model, "Terminverwaltung", "Termin hinzufügen");
			}
			return "detail/meetingdetail"; 
		}
		
		Meeting newMeeting = meetingService.save(meeting);
		return "redirect:/meetinglist/forteam/" + newMeeting.getTeam().getIdTeam() + "/marked/" + newMeeting.getIdMeeting();
	}
}
