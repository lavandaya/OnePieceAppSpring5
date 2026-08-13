package be.kdg.programming5.onepiece.presentation.mapper;

import be.kdg.programming5.onepiece.business.domain.Battle;
import be.kdg.programming5.onepiece.presentation.dto.BattleDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BattleMapper {

    BattleDto toDto(Battle battle);

    List<BattleDto> toDtoList(List<Battle> battles);
}