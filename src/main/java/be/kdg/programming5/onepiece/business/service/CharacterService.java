package be.kdg.programming5.onepiece.business.service;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.Crew;
import be.kdg.programming5.onepiece.business.domain.Powertype;

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
                      Powertype powertype, double power, String crewName, String ownerUsername);
    void deleteCharacter(int id);
    void updateSwordName(int id, String swordName);

    Character createCharacter(Character character, String crewName, String ownerUsername);
    Character updateCharacter(int id, CharacterUpdate update);

    // Resolves the owner and each distinct crew name once for the whole batch (rather than once
    // per item) and evicts the search cache once at the end. Rows whose crew name doesn't exist
    // are skipped rather than failing the whole batch. Returns the number of characters saved.
    int createCharactersBulk(List<CharacterImport> imports, String ownerUsername);
}