package be.kdg.programming5.onepiece.presentation.dto;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.Powertype;
import be.kdg.programming5.onepiece.business.domain.Swordsman;

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
    public static CharacterDto fromEntity(Character character) {
        String crewName = character.getCrew() != null ? character.getCrew().getName() : null;
        String swordName = character instanceof Swordsman swordsman ? swordsman.getSwordName() : null;

        return new CharacterDto(
                character.getId(),
                character.getName(),
                character.getAge(),
                character.getAppearance(),
                character.getPowertype(),
                character.getPower(),
                crewName,
                swordName
        );
    }
}