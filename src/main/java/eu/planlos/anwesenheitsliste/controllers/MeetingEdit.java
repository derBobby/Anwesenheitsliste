package eu.planlos.anwesenheitsliste.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.planlos.anwesenheitsliste.model.Meeting;
import eu.planlos.anwesenheitsliste.model.MeetingService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Service
public class MeetingEdit {

	@Autowired
	private MeetingService meetingService;
	
	@RequestMapping(path = "/meetingedit/{idmeeting}", method = RequestMethod.GET)
	public String showForm(Model model, @PathVariable Long idMeeting) {
		
		Meeting meeting = meetingService.findById(idMeeting);
		model.addAttribute(meeting);
		prepareContent(model);
		
		return "detail/meetingdetail";
	}
	
	@RequestMapping(path = "/meetingedit", method = RequestMethod.POST)
	public String edit(Model model, @Valid @ModelAttribute Meeting meeting, Errors errors) {
		
		if(errors.hasErrors()) {
			prepareContent(model);
			return "detail/meetingdetail";
		}
		
		meetingService.save(meeting);
		
		return "redirect:/meetinglist/" + meeting.getIdMeeting();
	}
	
	private void prepareContent(Model model) {
		
		model.addAttribute("newButtonUrl", "/meetingedit");
		model.addAttribute("newButtonText", "Termin ändern");
		
		GeneralAttributeCreator generalAttributeCreator = new GeneralAttributeCreator();
		generalAttributeCreator.create(model, "Terminverwaltung", "Termin hinzufügen");
	}
}
