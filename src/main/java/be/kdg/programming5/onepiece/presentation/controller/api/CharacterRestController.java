package be.kdg.programming5.onepiece.presentation.controller.api;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.exception.CharacterNotFoundException;
import be.kdg.programming5.onepiece.business.service.BattleService;
import be.kdg.programming5.onepiece.business.service.CharacterService;
import be.kdg.programming5.onepiece.presentation.dto.BattleDto;
import be.kdg.programming5.onepiece.presentation.dto.CharacterDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/characters", produces = MediaType.APPLICATION_JSON_VALUE)
public class CharacterRestController {

    private final CharacterService characterService;
    private final BattleService battleService;

    public CharacterRestController(CharacterService characterService, BattleService battleService) {
        this.characterService = characterService;
        this.battleService = battleService;
    }

    @GetMapping
    public List<CharacterDto> searchCharacters(@RequestParam(required = false) String name,
                                               @RequestParam(required = false) Double minPower) {
        List<Character> characters;
        if (name != null && !name.isBlank()) {
            characters = characterService.findByNameContaining(name.trim());
        } else if (minPower != null) {
            characters = characterService.findByMinPower(minPower);
        } else {
            characters = characterService.getAllCharacters();
        }
        return characters.stream().map(CharacterDto::fromEntity).toList();
    }

    @GetMapping("/{id}")
    public CharacterDto getCharacter(@PathVariable int id) {
        Character character = characterService.getCharacterById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));
        return CharacterDto.fromEntity(character);
    }

    @GetMapping("/{id}/battles")
    public List<BattleDto> getCharacterBattles(@PathVariable int id) {
        if (characterService.getCharacterById(id).isEmpty()) {
            throw new CharacterNotFoundException(id);
        }
        return battleService.getBattlesForCharacter(id).stream()
                .map(BattleDto::fromEntity)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable int id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}