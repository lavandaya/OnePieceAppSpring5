package be.kdg.programming5.onepiece.testsupport;

import be.kdg.programming5.onepiece.business.domain.Battle;
import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.CharacterBattle;
import be.kdg.programming5.onepiece.business.domain.Crew;
import be.kdg.programming5.onepiece.business.domain.Powertype;
import be.kdg.programming5.onepiece.business.domain.Role;
import be.kdg.programming5.onepiece.business.domain.Swordsman;
import be.kdg.programming5.onepiece.business.domain.User;
import be.kdg.programming5.onepiece.data.repository.BattleRepository;
import be.kdg.programming5.onepiece.data.repository.CharacterBattleRepository;
import be.kdg.programming5.onepiece.data.repository.CharacterRepository;
import be.kdg.programming5.onepiece.data.repository.CrewRepository;
import be.kdg.programming5.onepiece.data.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TestDataFactory {

    private static final String APPEARANCE = "https://placehold.co/400x400/000000/ffffff?text=Test";

    private final CrewRepository crewRepository;
    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final BattleRepository battleRepository;
    private final CharacterBattleRepository characterBattleRepository;

    public TestDataFactory(CrewRepository crewRepository, UserRepository userRepository,
                           CharacterRepository characterRepository, BattleRepository battleRepository,
                           CharacterBattleRepository characterBattleRepository) {
        this.crewRepository = crewRepository;
        this.userRepository = userRepository;
        this.characterRepository = characterRepository;
        this.battleRepository = battleRepository;
        this.characterBattleRepository = characterBattleRepository;
    }

    public Crew crew(String name, String shipName) {
        return crewRepository.save(new Crew(name, true, shipName));
    }

    public User user(String username, Role role) {
        return userRepository.save(new User(username, "irrelevant-hash", username + "@onepiece.com", role));
    }

    public Character character(String name, int age, Powertype powertype, double power, Crew crew, User owner) {
        Character character = new Character(name, age, APPEARANCE, powertype, power);
        character.setCrew(crew);
        character.setOwner(owner);
        return characterRepository.save(character);
    }

    public Swordsman swordsman(String name, int age, Powertype powertype, double power,
                               String swordName, Crew crew, User owner) {
        Swordsman swordsman = new Swordsman(name, age, APPEARANCE, powertype, power, swordName);
        swordsman.setCrew(crew);
        swordsman.setOwner(owner);
        return characterRepository.save(swordsman);
    }

    public Battle battle(String name, String location, LocalDateTime date, String winner) {
        return battleRepository.save(new Battle(name, location, date, winner));
    }

    public void join(Character character, Battle battle) {
        characterBattleRepository.save(new CharacterBattle(character, battle));
    }
}