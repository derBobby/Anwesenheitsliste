package eu.planlos.anwesenheitsliste.viewhelper;

import org.springframework.ui.Model;

import eu.planlos.anwesenheitsliste.AvailableSites;

public final class GeneralAttributeCreator {

	public static void create(Model model, String module, String title) {
		
		model.addAttribute("module", module);
		model.addAttribute("title", title);
		model.addAttribute("availableSites", AvailableSites.values());
	}
}
