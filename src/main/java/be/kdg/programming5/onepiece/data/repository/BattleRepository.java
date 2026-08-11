package be.kdg.programming5.onepiece.data.repository;

import be.kdg.programming3.onepiece.business.domain.Battle;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Profile("datajpa")
public interface SpringDataBattleRepository extends JpaRepository<Battle, Integer> {
    List<Battle> findByCharacters_Id(int characterId);

    List<Battle> findAllByOrderByIdAsc();

    List<Battle> findByNameContainingIgnoreCaseOrderByIdAsc(String name);

    List<Battle> findByDateGreaterThanEqualOrderByIdAsc(LocalDateTime fromDate);

    List<Battle> findByNameContainingIgnoreCaseAndDateGreaterThanEqualOrderByIdAsc(
            String name, LocalDateTime fromDate);
}