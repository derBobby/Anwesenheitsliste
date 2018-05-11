package eu.planlos.anwesenheitsliste.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.TeamService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

@Controller
public class TeamEdit {

	@Autowired
	private TeamService teamService;
	
	@RequestMapping(path = "/teamedit/{idTeam}", method = RequestMethod.GET)
	public String showForm(Model model, @PathVariable Long idTeam) {
		
		Team team = teamService.findById(idTeam);
		model.addAttribute(team);
		prepareContent(model);
		
		return "detail/teamdetail";
	}
	
	@RequestMapping(path = "/teamedit", method = RequestMethod.POST)
	public String edit(Model model, @Valid @ModelAttribute Team team, Errors errors) {
		
		if(errors.hasErrors()) {
			prepareContent(model);
			return "detail/teamdetail";
		}
		
		teamService.save(team);
		
		return "redirect:/teamlist/" + team.getIdTeam();
	}
	
	private void prepareContent(Model model) {
		
		model.addAttribute("newButtonUrl", "/teamedit");
		model.addAttribute("newButtonText", "Gruppe ändern");
		
		GeneralAttributeCreator generalAttributeCreator = new GeneralAttributeCreator();
		generalAttributeCreator.create(model, "Gruppenverwaltung", "Gruppe ändern");
	}
}
