package be.kdg.programming5.onepiece.presentation.controller;

import be.kdg.programming3.onepiece.business.service.CharacterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CrewController {

    private static final Logger logger = LoggerFactory.getLogger(CrewController.class);

    private final CharacterService characterService;

    public CrewController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping("/crews")
    public String showCrews(Model model) {
        logger.debug("Loading crews page");
        model.addAttribute("crews", characterService.getAllCrews());
        return "crews";
    }

    @GetMapping("/crews/{name}")
    public String showCrewDetail(@PathVariable String name, Model model) {
        logger.debug("Loading detail page for crew '{}'", name);
        return characterService.getCrewByName(name)
                .map(crew -> {
                    model.addAttribute("crew", crew);
                    model.addAttribute("members", characterService.getCharactersByCrew(crew));
                    return "crewDetail";
                })
                .orElse("redirect:/crews");
    }
}