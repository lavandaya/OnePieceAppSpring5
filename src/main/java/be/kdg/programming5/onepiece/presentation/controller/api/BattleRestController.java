package be.kdg.programming5.onepiece.presentation.controller.api;

import be.kdg.programming5.onepiece.business.domain.Battle;
import be.kdg.programming5.onepiece.business.service.BattleService;
import be.kdg.programming5.onepiece.presentation.dto.BattleDto;
import be.kdg.programming5.onepiece.presentation.dto.NewBattleDto;
import be.kdg.programming5.onepiece.presentation.mapper.BattleMapper;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/battles", produces = MediaType.APPLICATION_JSON_VALUE)
public class BattleRestController {

    private final BattleService battleService;
    private final BattleMapper battleMapper;

    public BattleRestController(BattleService battleService, BattleMapper battleMapper) {
        this.battleService = battleService;
        this.battleMapper = battleMapper;
    }

    @GetMapping
    public List<BattleDto> searchBattles(@RequestParam(required = false) String name,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate) {
        return battleMapper.toDtoList(battleService.findBattles(name, fromDate));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BattleDto> createBattle(@Valid @RequestBody NewBattleDto dto,
                                                  UriComponentsBuilder uriBuilder) {
        Battle created = battleService.createBattle(dto.name(), dto.location(), dto.date(), dto.winner());

        URI location = uriBuilder.path("/api/battles/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(battleMapper.toDto(created));
    }
}
