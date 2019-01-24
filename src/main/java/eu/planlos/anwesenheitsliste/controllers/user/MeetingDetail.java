package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.DELIMETER;
import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_MEETING;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_403;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGADDPARTICIPANTS;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGCHOOSETEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGFORTEAM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGLIST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGLISTFULL;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_MEETINGSUBMIT;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.MeetingService;
import eu.planlos.anwesenheitsliste.service.ParticipantService;
import eu.planlos.anwesenheitsliste.service.SecurityService;
import eu.planlos.anwesenheitsliste.service.TeamService;

//TODO may be simplified
@Controller
public class MeetingDetail {

	public final String STR_MODULE = "Terminverwaltung";
	public final String STR_TITLE_ADD_MEETING = "Termin hinzufügen";
	public final String STR_TITLE_EDIT_MEETING = "Termin ändern";

	@Autowired
	private BodyFillerService bf;
	
	@Autowired
	private MeetingService meetingService;

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private ParticipantService participantService;
	
	// For editing a given meeting for given team
	@RequestMapping(path = URL_MEETINGFORTEAM + "{idTeam}/{idMeeting}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable Long idTeam, @PathVariable Long idMeeting) {
	
		if(!securityService.isAdmin() && !securityService.isUserAuthorizedForTeam(idTeam)) {
			return "redirect:" + URL_ERROR_403;
		}
		
		Meeting meeting = meetingService.findById(idMeeting);
		model.addAttribute(meeting);
		prepareContent(model, meeting);
		
		prepareContentWithTeam(model, idTeam); //###############
		
		return RES_MEETING;
	}

	// For adding new meeting for given team
	@RequestMapping(path = URL_MEETINGFORTEAM + "{idTeam}", method = RequestMethod.GET)
	public String addForTeam(Model model, @PathVariable Long idTeam) {

		if(!securityService.isAdmin() && !securityService.isUserAuthorizedForTeam(idTeam)) {
			return "redirect:" + URL_ERROR_403;
		}
		
		Meeting meeting = new Meeting();
		meeting.setMeetingDate(today());
		model.addAttribute(meeting);
		prepareContent(model, meeting);
		
		prepareContentWithTeam(model, idTeam); //###############

		return RES_MEETING;
	}
	
	//TODO load only permitted!!!
	// STEP 1 adding new meeting without a given team
	@RequestMapping(path = URL_MEETINGCHOOSETEAM, method = RequestMethod.GET)
	public String addWithoutTeam(Model model) {
		
		Meeting meeting = new Meeting();
		meeting.setMeetingDate(today());
		model.addAttribute(meeting);
		prepareContent(model, meeting);

		prepareContentWithoutTeam(model);
		
		return RES_MEETING;
	}

	private Date today() {
		return Calendar.getInstance().getTime();
	}

	// STEP 2 adding new meeting without a given team
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
	
	// For submitting the added/edited meeting
	@RequestMapping(path = URL_MEETINGSUBMIT, method = RequestMethod.POST)
	public String submit(Model model, @Valid @ModelAttribute Meeting meeting, Errors errors) {
		
		if(! securityService.isAdmin() && ( meeting.getIdMeeting() != null && !securityService.isUserAuthorizedForTeam(meeting.getTeam().getIdTeam())) ) {
			return "redirect:" + URL_ERROR_403;
		}
		
		if(errors.hasErrors()) {
			handleValidationErrors(model, meeting);
			return RES_MEETING; 
		}
	
		//TODO check post data: authorized?
		
	// TODO Handling in Controller?
	// TODO Transaction
	// {
		// Disabled checkboxes can be manipulated so that they could be set.
		// Don't trust the frontend! :-P
		// Save only editable Participants
		List<Participant> trustedParticipants = meetingService.getOnlyEditableParticipantsForMeeting(meeting);
		
		// HTML currently doesn't send disabled checkboxes so we need to correct these
		// because inactive participants should not be modifiable at the moment 
		List <Participant> notTransmittedParticipants = participantService.getInactiveParticipantsForMeeting(meeting);
		
		meeting.setParticipants(trustedParticipants);
		meeting.addParticipants(notTransmittedParticipants);

		meeting = meetingService.save(meeting);
	// }
		
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
			bf.fill(model, STR_MODULE, STR_TITLE_EDIT_MEETING);
		} else {
			bf.fill(model, STR_MODULE, STR_TITLE_ADD_MEETING);
		}
	}
	
	private void prepareContentWithoutTeam(Model model) {
		
		//TODO only own teams
		model.addAttribute("teams", teamService.findAll());
		model.addAttribute("participants", new ArrayList<>());
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
