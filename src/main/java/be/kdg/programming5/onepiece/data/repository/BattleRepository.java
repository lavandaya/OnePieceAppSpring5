package be.kdg.programming5.onepiece.data.repository;

import be.kdg.programming5.onepiece.business.domain.Battle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BattleRepository extends JpaRepository<Battle, Integer> {

    List<Battle> findAllByOrderByIdAsc();

    List<Battle> findByCharacters_Id(int characterId);

    List<Battle> findByNameContainingIgnoreCaseOrderByIdAsc(String name);

    List<Battle> findByDateGreaterThanEqualOrderByIdAsc(LocalDateTime fromDate);

    List<Battle> findByNameContainingIgnoreCaseAndDateGreaterThanEqualOrderByIdAsc(
            String name, LocalDateTime fromDate);
}