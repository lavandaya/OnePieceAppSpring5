package be.kdg.programming5.onepiece.data.repository;

import be.kdg.programming5.onepiece.business.domain.Battle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BattleRepository extends JpaRepository<Battle, Integer> {

    List<Battle> findAllByOrderByIdAsc();

    List<Battle> findByNameContainingIgnoreCaseOrderByIdAsc(String name);

    List<Battle> findByDateGreaterThanEqualOrderByIdAsc(LocalDateTime fromDate);

    List<Battle> findByNameContainingIgnoreCaseAndDateGreaterThanEqualOrderByIdAsc(
            String name, LocalDateTime fromDate);

    @Query("SELECT cb.battle FROM CharacterBattle cb WHERE cb.character.id = :characterId ORDER BY cb.battle.id")
    List<Battle> findByCharacterId(@Param("characterId") int characterId);
}