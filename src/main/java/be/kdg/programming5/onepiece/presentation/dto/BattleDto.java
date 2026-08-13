package be.kdg.programming5.onepiece.presentation.dto;

import java.time.LocalDateTime;

public record BattleDto(
        int id,
        String name,
        String location,
        LocalDateTime date,
        String winner
) {
}