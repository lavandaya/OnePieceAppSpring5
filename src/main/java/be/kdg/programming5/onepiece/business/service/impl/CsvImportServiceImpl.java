package be.kdg.programming5.onepiece.business.service.impl;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.Powertype;
import be.kdg.programming5.onepiece.business.domain.Swordsman;
import be.kdg.programming5.onepiece.business.service.CharacterService;
import be.kdg.programming5.onepiece.business.service.CsvImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsvImportServiceImpl implements CsvImportService {

    private static final Logger logger = LoggerFactory.getLogger(CsvImportServiceImpl.class);

    private final CharacterService characterService;

    public CsvImportServiceImpl(CharacterService characterService) {
        this.characterService = characterService;
    }

    @Override
    @Async("csvImportExecutor")
    public void importCharacters(List<String> csvLines, String uploaderUsername) {
        int imported = 0;
        int failed = 0;

        for (String line : csvLines) {
            if (line == null || line.isBlank()) {
                continue;
            }
            try {
                String[] fields = line.split(",", -1);
                String crewName = fields.length > 5 && !fields[5].isBlank() ? fields[5].trim() : null;
                characterService.createCharacter(toCharacter(fields), crewName, uploaderUsername);
                imported++;
            } catch (Exception ex) {
                logger.warn("Skipped invalid CSV row '{}': {}", line, ex.getMessage());
                failed++;
            }
        }

        logger.info("CSV import finished: {} imported, {} failed (uploaded by {})", imported, failed, uploaderUsername);
    }

    private Character toCharacter(String[] fields) {
        String name = fields[0].trim();
        int age = Integer.parseInt(fields[1].trim());
        String appearance = fields[2].trim();
        Powertype powertype = Powertype.valueOf(fields[3].trim());
        double power = Double.parseDouble(fields[4].trim());
        String swordName = fields.length > 6 ? fields[6].trim() : "";

        return swordName.isBlank()
                ? new Character(name, age, appearance, powertype, power)
                : new Swordsman(name, age, appearance, powertype, power, swordName);
    }
}
