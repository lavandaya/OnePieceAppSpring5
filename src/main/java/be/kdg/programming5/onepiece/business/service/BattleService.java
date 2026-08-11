package be.kdg.programming5.onepiece.business.service;

import be.kdg.programming3.onepiece.business.domain.Battle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BattleService {
    List<Battle> getAllBattles();
    Optional<Battle> getBattleById(int id);
    List<Battle> findBattles(String nameContains, LocalDate fromDate);
    List<Battle> getBattlesForCharacter(int characterId);

    void addBattle(String name, String location, LocalDateTime date, String winner, List<Integer> characterIds);
    void deleteBattle(int id);
}