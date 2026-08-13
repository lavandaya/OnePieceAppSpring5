package be.kdg.programming5.onepiece.presentation.mapper;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.Crew;
import be.kdg.programming5.onepiece.business.domain.Swordsman;
import be.kdg.programming5.onepiece.business.service.CharacterUpdate;
import be.kdg.programming5.onepiece.presentation.dto.CharacterDto;
import be.kdg.programming5.onepiece.presentation.dto.NewCharacterDto;
import be.kdg.programming5.onepiece.presentation.dto.UpdateCharacterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CharacterMapper {

    @Mapping(target = "crewName", expression = "java(mapCrewName(character))")
    @Mapping(target = "swordName", expression = "java(mapSwordName(character))")
    CharacterDto toDto(Character character);

    List<CharacterDto> toDtoList(List<Character> characters);

    CharacterUpdate toUpdate(UpdateCharacterDto dto);

    default Character toEntity(NewCharacterDto dto) {
        if (dto.swordName() != null && !dto.swordName().isBlank()) {
            return new Swordsman(dto.name(), dto.age(), dto.appearance(),
                    dto.powertype(), dto.power(), dto.swordName());
        }
        return new Character(dto.name(), dto.age(), dto.appearance(),
                dto.powertype(), dto.power());
    }

    default String mapCrewName(Character character) {
        Crew crew = character.getCrew();
        return crew == null ? null : crew.getName();
    }

    default String mapSwordName(Character character) {
        return character instanceof Swordsman swordsman ? swordsman.getSwordName() : null;
    }
}