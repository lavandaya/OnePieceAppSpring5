package be.kdg.programming5.onepiece.business.service.impl;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.Powertype;
import be.kdg.programming5.onepiece.business.domain.Swordsman;
import be.kdg.programming5.onepiece.business.service.CharacterImport;
import be.kdg.programming5.onepiece.business.service.CharacterService;
import be.kdg.programming5.onepiece.business.service.CsvImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<CharacterImport> imports = new ArrayList<>();
        int rejected = 0;

        for (String line : csvLines) {
            if (line == null || line.isBlank()) {
                continue;
            }
            try {
                String[] fields = line.split(",", -1);
                String crewName = fields.length > 5 && !fields[5].isBlank() ? fields[5].trim() : null;
                imports.add(new CharacterImport(toCharacter(fields), crewName));
            } catch (Exception ex) {
                logger.warn("Skipped invalid CSV row '{}': {}", line, ex.getMessage());
                rejected++;
            }
        }

        int imported = characterService.createCharactersBulk(imports, uploaderUsername);
        int failed = rejected + (imports.size() - imported);
        logger.info("CSV import finished: {} imported, {} failed (uploaded by {})", imported, failed, uploaderUsername);
    }

    private Character toCharacter(String[] fields) {
        String name = fields[0].trim();
        if (name.length() < 2 || name.length() > 50) {
            throw new IllegalArgumentException("name must be between 2 and 50 characters");
        }

        int age = Integer.parseInt(fields[1].trim());
        if (age < 0 || age > 200) {
            throw new IllegalArgumentException("age must be between 0 and 200");
        }

        String appearance = fields[2].trim();
        if (!appearance.matches("https?://.+")) {
            throw new IllegalArgumentException("appearance must be a valid http(s) URL");
        }

        Powertype powertype = Powertype.valueOf(fields[3].trim());

        double power = Double.parseDouble(fields[4].trim());
        if (power < 0 || power > 100) {
            throw new IllegalArgumentException("power must be between 0 and 100");
        }

        String swordName = fields.length > 6 ? fields[6].trim() : "";
        if (swordName.length() > 100) {
            throw new IllegalArgumentException("swordName may not exceed 100 characters");
        }

        return swordName.isBlank()
                ? new Character(name, age, appearance, powertype, power)
                : new Swordsman(name, age, appearance, powertype, power, swordName);
    }
}
