package be.kdg.programming5.onepiece.data.repository;

import be.kdg.programming5.onepiece.business.domain.CharacterBattle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CharacterBattleRepository extends JpaRepository<CharacterBattle, Integer> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM CharacterBattle cb WHERE cb.character.id = :characterId")
    void deleteByCharacterId(@Param("characterId") int characterId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM CharacterBattle cb WHERE cb.battle.id = :battleId")
    void deleteByBattleId(@Param("battleId") int battleId);
}