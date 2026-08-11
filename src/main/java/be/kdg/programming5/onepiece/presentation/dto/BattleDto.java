package be.kdg.programming5.onepiece.presentation.dto;

import be.kdg.programming5.onepiece.business.domain.Battle;

import java.time.LocalDateTime;

public record BattleDto(
        int id,
        String name,
        String location,
        LocalDateTime date,
        String winner
) {
    public static BattleDto fromEntity(Battle battle) {
        return new BattleDto(
                battle.getId(),
                battle.getName(),
                battle.getLocation(),
                battle.getDate(),
                battle.getWinner()
        );
    }
}