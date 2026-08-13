package be.kdg.programming5.onepiece.business.service;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.Crew;
import be.kdg.programming5.onepiece.business.domain.Powertype;
import be.kdg.programming5.onepiece.business.domain.Swordsman;
import be.kdg.programming5.onepiece.business.exception.CharacterNotFoundException;
import be.kdg.programming5.onepiece.business.exception.CrewNotFoundException;
import be.kdg.programming5.onepiece.business.exception.NotASwordsmanException;
import be.kdg.programming5.onepiece.data.repository.CharacterRepository;
import be.kdg.programming5.onepiece.data.repository.CrewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CharacterServiceIntegrationTest {

    @Autowired
    private CharacterService characterService;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private CrewRepository crewRepository;

    private Character character;
    private Swordsman swordsman;

    @BeforeEach
    void setUp() {
        Crew crew = crewRepository.save(new Crew("Straw Hat Pirates", true, "Going Merry"));

        character = new Character("Luffy", 18, "img", Powertype.DEVIL_FRUIT, 10);
        character.setCrew(crew);
        character = characterRepository.save(character);

        swordsman = characterRepository.save(
                new Swordsman("Zoro", 20, "img", Powertype.WILL, 9, "Wado Ichimonji"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCharacter_appliesOnlyProvidedFields() {
        CharacterUpdate update = new CharacterUpdate("Monkey D. Luffy", null, null, null, 12.0, null, null);

        Character updated = characterService.updateCharacter(character.getId(), update);

        assertThat(updated.getName()).isEqualTo("Monkey D. Luffy");
        assertThat(updated.getPower()).isEqualTo(12.0);
        assertThat(updated.getAge()).isEqualTo(18);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCharacter_unknownId_throwsCharacterNotFoundException() {
        CharacterUpdate update = new CharacterUpdate("X", null, null, null, null, null, null);

        assertThatThrownBy(() -> characterService.updateCharacter(-1, update))
                .isInstanceOf(CharacterNotFoundException.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCharacter_unknownCrew_throwsCrewNotFoundException() {
        CharacterUpdate update = new CharacterUpdate(null, null, null, null, null, "Unknown Crew", null);

        assertThatThrownBy(() -> characterService.updateCharacter(character.getId(), update))
                .isInstanceOf(CrewNotFoundException.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCharacter_swordNameOnNonSwordsman_throwsNotASwordsmanException() {
        CharacterUpdate update = new CharacterUpdate(null, null, null, null, null, null, "Enma");

        assertThatThrownBy(() -> characterService.updateCharacter(character.getId(), update))
                .isInstanceOf(NotASwordsmanException.class);
    }
}