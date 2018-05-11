package eu.planlos.anwesenheitsliste.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.TeamService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class TeamAdd {

	@Autowired
	private TeamService teamService;
	
	@RequestMapping(path = "/teamadd", method = RequestMethod.GET)
	public String showForm(Model model) {
		
		model.addAttribute(new Team());
		prepareContent(model);
		
		return "detail/teamdetail";
	}
	
	@RequestMapping(path = "/teamadd", method = RequestMethod.POST)
	public String add(Model model, @Valid @ModelAttribute Team team, Errors errors) {
	
		if(errors.hasErrors()) {
			prepareContent(model);
			return "detail/teamdetail";
		}
		
		Team newTeam = teamService.save(team);
		return "redirect:/teamlist/" + newTeam.getIdTeam();
	}
	
	private void prepareContent(Model model) {

		model.addAttribute("newButtonText", "Gruppe hinzufügen");
		model.addAttribute("newButtonUrl", "/teamadd");
		
		GeneralAttributeCreator generalAttributeCreator = new GeneralAttributeCreator();
		generalAttributeCreator.create(model, "Gruppenverwaltung", "Gruppe hinzufügen");	
	}
}
