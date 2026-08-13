package be.kdg.programming5.onepiece.presentation.dto;

import be.kdg.programming5.onepiece.business.domain.Powertype;

public record CharacterDto(
        int id,
        String name,
        int age,
        String appearance,
        Powertype powertype,
        double power,
        String crewName,
        String swordName
) {
}