package eu.planlos.anwesenheitsliste.controllers.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.ParticipantService;
import eu.planlos.anwesenheitsliste.viewhelper.GeneralAttributeCreator;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.URL_TEAMPHONELIST;

import static eu.planlos.anwesenheitsliste.ApplicationPaths.RES_TEAMPHONELIST;

@Controller
public class TeamPhonelist {

	public final String STR_MODULE = "Gruppenverwaltung";
	public final String STR_TITLE = "Telefonliste";
	
	@Autowired
    private ParticipantService participantService;

	@RequestMapping(value = URL_TEAMPHONELIST + "{idTeam}")
	public String markedTeamList(Model model, @PathVariable Long idTeam) {
		
		prepareContent(model, idTeam);
		return RES_TEAMPHONELIST;
	}
	
	private void prepareContent(Model model, Long idTeam) {
		
		List<String> headings = new ArrayList<String>();	
		headings.add("#");
		headings.add("Teilnehmer");
		headings.add("Telefonnummer");

		List<Participant> participants = participantService.findAllByTeamsIdTeam(idTeam);
		
		model.addAttribute("headings", headings);
		model.addAttribute("participants", participants);
	
		GeneralAttributeCreator.create(model, STR_MODULE, STR_TITLE);
	}
}
