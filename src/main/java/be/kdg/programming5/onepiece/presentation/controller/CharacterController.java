package be.kdg.programming5.onepiece.presentation.controller;

import be.kdg.programming3.onepiece.business.domain.Character;
import be.kdg.programming3.onepiece.business.domain.Crew;
import be.kdg.programming3.onepiece.business.domain.Powertype;
import be.kdg.programming3.onepiece.business.service.BattleService;
import be.kdg.programming3.onepiece.business.service.CharacterService;
import be.kdg.programming3.onepiece.presentation.viewmodel.CharacterViewModel;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import be.kdg.programming3.onepiece.business.exception.CharacterNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;

@Controller
public class CharacterController {

    private static final Logger logger = LoggerFactory.getLogger(CharacterController.class);

    private final CharacterService characterService;
    private final BattleService battleService;


    @GetMapping("/characters/search")
    public String searchCharacters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPower,
            @RequestParam(required = false) Integer minBattles,
            Model model) {

        logger.debug("Search page (name='{}', minPower={}, minBattles={})", name, minPower, minBattles);

        List<Character> results = null;

        if (name != null && !name.isBlank()) {
            results = characterService.findByNameContaining(name.trim());
        } else if (minPower != null) {
            results = characterService.findByMinPower(minPower);
        } else if (minBattles != null) {
            results = characterService.findByMinBattles(minBattles);
        }

        model.addAttribute("results", results);
        model.addAttribute("searchName", name);
        model.addAttribute("searchMinPower", minPower);
        model.addAttribute("searchMinBattles", minBattles);
        return "characterSearch";
    }


    public CharacterController(CharacterService characterService, BattleService battleService) {
        this.characterService = characterService;
        this.battleService = battleService;
    }

    @GetMapping({"/", "/characters"})
    public String showCharacters(@RequestParam(required = false) String crewName, Model model) {
        logger.debug("Loading characters page (crew filter = '{}')", crewName);

        List<Crew> crews = characterService.getAllCrews();
        List<Character> characters = filterCharacters(crewName, crews);

        model.addAttribute("characters", characters);
        model.addAttribute("crews", crews);
        model.addAttribute("selectedCrew", crewName);
        return "characters";
    }

    private List<Character> filterCharacters(String crewName, List<Crew> crews) {
        if (crewName == null || crewName.isBlank()) {
            return characterService.getAllCharacters();
        }
        return crews.stream()
                .filter(c -> c.getName().equals(crewName))
                .findFirst()
                .map(characterService::getCharactersByCrew)
                .orElseGet(List::of);
    }

    @GetMapping("/characters/add")
    public String showAddCharacterForm(Model model) {
        model.addAttribute("characterViewModel", new CharacterViewModel());
        model.addAttribute("crews", characterService.getAllCrews());
        model.addAttribute("powertypes", Powertype.values());
        return "addCharacter";
    }

    @PostMapping("/characters/add")
    public String addCharacter(@Valid @ModelAttribute("characterViewModel") CharacterViewModel viewModel,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.debug("Add character form has {} error(s)", bindingResult.getErrorCount());
            model.addAttribute("crews", characterService.getAllCrews());
            model.addAttribute("powertypes", Powertype.values());
            return "addCharacter";
        }
        characterService.addCharacter(viewModel.getName(), viewModel.getAge(),
                viewModel.getAppearance(), viewModel.getPowertype(),
                viewModel.getPower(), viewModel.getCrew().getName());
        return "redirect:/characters";
    }

    @GetMapping("/characters/{id}")
    public String showCharacterDetail(@PathVariable int id, Model model) {
        logger.debug("Loading detail page for character id={}", id);
        return characterService.getCharacterById(id)
                .map(character -> {
                    model.addAttribute("character", character);
                    model.addAttribute("battles", battleService.getBattlesForCharacter(id));
                    if (character instanceof be.kdg.programming3.onepiece.business.domain.Swordsman swordsman) {
                        model.addAttribute("swordName", swordsman.getSwordName());
                    }
                    return "characterDetail";
                })
                .orElse("redirect:/characters");
    }

    @PostMapping("/characters/{id}/sword")
    public String updateSwordName(@PathVariable int id, @RequestParam String swordName) {
        characterService.updateSwordName(id, swordName);
        return "redirect:/characters/" + id;
    }

    @PostMapping("/characters/{id}/delete")
    public String deleteCharacter(@PathVariable int id) {
        logger.debug("Deleting character id={}", id);
        characterService.deleteCharacter(id);
        return "redirect:/characters";
    }

    @ExceptionHandler(CharacterNotFoundException.class)
    public String handleCharacterNotFound(CharacterNotFoundException ex, Model model) {
        logger.warn("Character not found (id={}): {}", ex.getCharacterId(), ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/general";
    }

}