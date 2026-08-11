package be.kdg.programming5.onepiece.presentation.converter;

import be.kdg.programming5.onepiece.business.domain.Crew;
import be.kdg.programming5.onepiece.business.service.CharacterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class StringToCrewConverter implements Converter<String, Crew> {

    private static final Logger logger = LoggerFactory.getLogger(StringToCrewConverter.class);

    private final CharacterService characterService;

    public StringToCrewConverter(CharacterService characterService) {
        this.characterService = characterService;
    }

    @Override
    public Crew convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        logger.debug("Converting crew name '{}' to Crew", source);
        return characterService.getAllCrews().stream()
                .filter(crew -> crew.getName().equals(source))
                .findFirst()
                .orElse(null);
    }
}