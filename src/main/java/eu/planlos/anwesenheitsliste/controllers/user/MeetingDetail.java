package eu.planlos.anwesenheitsliste.controllers.user;

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

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_MEETINGFORTEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_MEETINGCHOOSETEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_MEETINGADDPARTICIPANTS;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_MEETINGSUBMIT;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_MEETINGLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_MEETINGLISTFULL;
import static eu.planlos.anwesenheitsliste.ApplicationPaths.DELIMETER;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_MEETING;

//TODO may be simplified
@Controller
public class MeetingDetail {

	public final String STR_MODULE = "Terminverwaltung";
	public final String STR_TITLE_ADD_MEETING = "Termin hinzufügen";
	public final String STR_TITLE_EDIT_MEETING = "Termin ändern";
	
	@Autowired
	private MeetingService meetingService;

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private ParticipantService participantService;
	
	//For editing a given meeting for given team
	@RequestMapping(path = URL_MEETINGFORTEAM + "{idTeam}/{idMeeting}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable Long idTeam, @PathVariable Long idMeeting) {
	
		Meeting meeting = meetingService.findById(idMeeting);
		model.addAttribute(meeting);
		prepareContent(model, meeting);
		
		prepareContentWithTeam(model, idTeam); //###############
		
		return RES_MEETING;
	}

	//For adding new meeting for given team
	@RequestMapping(path = URL_MEETINGFORTEAM + "{idTeam}", method = RequestMethod.GET)
	public String addForTeam(Model model, @PathVariable Long idTeam) {
		
		Meeting meeting = new Meeting();
		model.addAttribute(meeting);
		prepareContent(model, meeting);
		
		prepareContentWithTeam(model, idTeam); //###############

		return RES_MEETING;
	}
	
	//STEP 1 adding new meeting without a given team
	@RequestMapping(path = URL_MEETINGCHOOSETEAM, method = RequestMethod.GET)
	public String addWithoutTeam(Model model) {
		
		Meeting meeting = new Meeting();
		model.addAttribute(meeting);
		prepareContent(model, meeting);

		prepareContentWithoutTeam(model); //###############
		
		return RES_MEETING;
	}

	//STEP 2 adding new meeting without a given team
	@RequestMapping(path = URL_MEETINGADDPARTICIPANTS, method = RequestMethod.POST)
	public String addWithoutTeam(Model model, @Valid @ModelAttribute Meeting meeting, Errors errors) {

		if(errors.hasErrors()) {
			handleValidationErrors(model, meeting);
			return RES_MEETING; 
		}
		
		prepareContent(model, meeting);
		prepareContentWithTeam(model, meeting.getTeam().getIdTeam());
		
		return RES_MEETING; 
	}
	
	//For submitting the added/edited meeting
	@RequestMapping(path = URL_MEETINGSUBMIT, method = RequestMethod.POST)
	public String submit(Model model, @Valid @ModelAttribute Meeting meeting, Errors errors) {
		
		if(errors.hasErrors()) {
			handleValidationErrors(model, meeting);
			return RES_MEETING; 
		}
		
		//HTML currently doesn't send disabled checkboxes so we need to correct these
		//because inactive participants should not be modifiable at the moment 
		meeting.addParticipants(participantService.getInactiveParticipantsForMeeting(meeting));
		
		meeting = meetingService.save(meeting);

		return "redirect:" + URL_MEETINGLIST + meeting.getTeam().getIdTeam() + DELIMETER + meeting.getIdMeeting();
	}
	
	private void handleValidationErrors(Model model, Meeting meeting) {
		
		prepareContent(model, meeting);

		if(meeting.getTeam() != null) {
			prepareContentWithTeam(model, meeting.getTeam().getIdTeam());
		} else {
			prepareContentWithoutTeam(model);
		}
	}

	private void prepareContent(Model model, Meeting meeting) {
		
		if(meeting.getIdMeeting() != null) {
			GeneralAttributeCreator.create(model, STR_MODULE, STR_TITLE_EDIT_MEETING);
		} else {
			GeneralAttributeCreator.create(model, STR_MODULE, STR_TITLE_ADD_MEETING);
		}
	}
	
	private void prepareContentWithoutTeam(Model model) {
		
		model.addAttribute("teams", teamService.findAll());
		model.addAttribute("participants", new ArrayList<>()); //TODO No participants. Which to load? JQuery?
		model.addAttribute("formAction", URL_MEETINGADDPARTICIPANTS);
		model.addAttribute("formCancel", URL_MEETINGLISTFULL);
	}
	
	private void prepareContentWithTeam(Model model, Long idTeam) {
		
		Team team = teamService.findById(idTeam);
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		
		model.addAttribute("teams", teams);
		model.addAttribute("participants", participantService.findAllByTeamsIdTeam(idTeam));
		model.addAttribute("formAction", URL_MEETINGSUBMIT);
		model.addAttribute("formCancel", URL_MEETINGLIST + idTeam);
	}
}
