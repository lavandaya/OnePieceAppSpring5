package be.kdg.programming5.onepiece.presentation.controller.api;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.exception.CharacterNotFoundException;
import be.kdg.programming5.onepiece.business.service.BattleService;
import be.kdg.programming5.onepiece.business.service.CharacterService;
import be.kdg.programming5.onepiece.presentation.dto.BattleDto;
import be.kdg.programming5.onepiece.presentation.dto.CharacterDto;
import be.kdg.programming5.onepiece.presentation.dto.NewCharacterDto;
import be.kdg.programming5.onepiece.presentation.dto.UpdateCharacterDto;
import be.kdg.programming5.onepiece.presentation.mapper.BattleMapper;
import be.kdg.programming5.onepiece.presentation.mapper.CharacterMapper;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/characters", produces = MediaType.APPLICATION_JSON_VALUE)
public class CharacterRestController {

    private final CharacterService characterService;
    private final BattleService battleService;
    private final CharacterMapper characterMapper;
    private final BattleMapper battleMapper;

    public CharacterRestController(CharacterService characterService, BattleService battleService,
                                   CharacterMapper characterMapper, BattleMapper battleMapper) {
        this.characterService = characterService;
        this.battleService = battleService;
        this.characterMapper = characterMapper;
        this.battleMapper = battleMapper;
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
        return characterMapper.toDtoList(characters);
    }

    @GetMapping("/{id}")
    public CharacterDto getCharacter(@PathVariable int id) {
        Character character = characterService.getCharacterById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));
        return characterMapper.toDto(character);
    }

    @GetMapping("/{id}/battles")
    public List<BattleDto> getCharacterBattles(@PathVariable int id) {
        if (characterService.getCharacterById(id).isEmpty()) {
            throw new CharacterNotFoundException(id);
        }
        return battleMapper.toDtoList(battleService.getBattlesForCharacter(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CharacterDto> createCharacter(@Valid @RequestBody NewCharacterDto dto,
                                                        UriComponentsBuilder uriBuilder,
                                                        Principal principal) {
        Character created = characterService.createCharacter(characterMapper.toEntity(dto), dto.crewName(),
                principal != null ? principal.getName() : null);

        URI location = uriBuilder.path("/api/characters/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(characterMapper.toDto(created));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CharacterDto> updateCharacter(@PathVariable int id,
                                                        @Valid @RequestBody UpdateCharacterDto dto) {
        Character updated = characterService.updateCharacter(id, characterMapper.toUpdate(dto));
        return ResponseEntity.ok(characterMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable int id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}