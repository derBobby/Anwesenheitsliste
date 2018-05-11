package eu.planlos.anwesenheitsliste.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.planlos.anwesenheitsliste.model.Meeting;
import eu.planlos.anwesenheitsliste.model.MeetingService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class MeetingAdd {

	@Autowired
	private MeetingService meetingService;
	
	@RequestMapping(path = "/meetingadd", method = RequestMethod.GET)
	public String showForm(Model model) {

		model.addAttribute(new Meeting());
		prepareContent(model);
		
		return "detail/meetingdetail";
	}

	@RequestMapping(path = "/meetingadd", method = RequestMethod.POST)
	public String add(Model model, @Valid @ModelAttribute Meeting meeting, Errors errors) {
		
		if(errors.hasErrors()) {
			prepareContent(model);
			return "detail/meetingdetail";
		}
		
		Meeting newMeeting = meetingService.save(meeting);
		return "redirect:/meetinglist/" + newMeeting.getIdMeeting();
	}
	
	private void prepareContent(Model model) {
		
		model.addAttribute("newButtonText", "Termin hinzufügen");
		model.addAttribute("newButtonUrl", "/meetingadd");
		
		GeneralAttributeCreator generalAttributeCreator = new GeneralAttributeCreator();
		generalAttributeCreator.create(model, "Terminverwaltung", "Termin hinzufügen");	}
}
