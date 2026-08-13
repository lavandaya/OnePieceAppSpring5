package be.kdg.programming5.onepiece.data.repository;

import be.kdg.programming5.onepiece.business.domain.Battle;
import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.CharacterBattle;
import be.kdg.programming5.onepiece.business.domain.Crew;
import be.kdg.programming5.onepiece.business.domain.Powertype;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
class CharacterRepositoryTest {

    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private CrewRepository crewRepository;
    @Autowired
    private BattleRepository battleRepository;
    @Autowired
    private CharacterBattleRepository characterBattleRepository;
    @Autowired
    private EntityManager entityManager;

    private Character character;
    private Battle battle;

    @BeforeEach
    void setUp() {
        Crew crew = crewRepository.save(new Crew("Straw Hat Pirates", true, "Going Merry"));

        character = new Character("Luffy", 18, "img", Powertype.DEVIL_FRUIT, 10);
        character.setCrew(crew);
        character = characterRepository.save(character);

        battle = battleRepository.save(new Battle("Arlong Park showdown", "Arlong Park",
                LocalDateTime.of(2005, 7, 23, 12, 20), "Luffy"));

        characterBattleRepository.save(new CharacterBattle(character, battle));
    }

    @Test
    void deletingCharacter_removesCharacterBattles_butKeepsBattle() {
        characterBattleRepository.deleteByCharacterId(character.getId());
        characterRepository.deleteById(character.getId());
        entityManager.flush();
        entityManager.clear();

        assertThat(characterBattleRepository.findAll()).isEmpty();
        assertThat(battleRepository.findById(battle.getId())).isPresent();
    }

    @Test
    void savingDuplicateCharacterBattlePair_violatesUniqueConstraint() {
        CharacterBattle duplicate = new CharacterBattle(character, battle);

        assertThatThrownBy(() -> characterBattleRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findById_doesNotEagerlyLoadCrew() {
        entityManager.clear();

        Character found = characterRepository.findById(character.getId()).orElseThrow();

        assertThat(Hibernate.isInitialized(found.getCrew())).isFalse();
    }

    @Test
    void findByIdWithCrew_eagerlyLoadsCrew() {
        entityManager.clear();

        Character found = characterRepository.findByIdWithCrew(character.getId()).orElseThrow();

        assertThat(Hibernate.isInitialized(found.getCrew())).isTrue();
    }
}