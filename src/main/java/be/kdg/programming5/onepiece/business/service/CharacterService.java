package be.kdg.programming5.onepiece.business.service;

import be.kdg.programming3.onepiece.business.domain.Character;
import be.kdg.programming3.onepiece.business.domain.Crew;
import be.kdg.programming3.onepiece.business.domain.Powertype;

import java.util.List;
import java.util.Optional;

public interface CharacterService {
    List<Character> getAllCharacters();
    Optional<Character> getCharacterById(int id);
    List<Character> getCharactersByCrew(Crew crew);
    List<Character> getCharactersByPower(double power);
    List<Character> getCharactersInBattle(int battleId);
    List<Crew> getAllCrews();
    Optional<Crew> getCrewByName(String name);

    List<Character> findByNameContaining(String name);
    List<Character> findByMinPower(double minPower);
    List<Character> findByMinBattles(int minBattles);

    void addCharacter(String name, int age, String appearance,
                      Powertype powertype, double power, String crewName);
    void deleteCharacter(int id);
    void updateSwordName(int id, String swordName);
}