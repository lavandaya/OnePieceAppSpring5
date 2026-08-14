package be.kdg.programming5.onepiece.data.repository;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Integer> {

    @Query("SELECT c FROM Character c LEFT JOIN FETCH c.crew LEFT JOIN FETCH c.owner ORDER BY c.id")
    List<Character> findAllByOrderByIdAsc();

    @Query("SELECT c FROM Character c LEFT JOIN FETCH c.crew LEFT JOIN FETCH c.owner WHERE c.id = :id")
    Optional<Character> findByIdWithCrew(@Param("id") int id);

    @Query("SELECT c FROM Character c LEFT JOIN FETCH c.crew LEFT JOIN FETCH c.owner WHERE c.crew = :crew ORDER BY c.id")
    List<Character> findByCrew(@Param("crew") Crew crew);

    List<Character> findByPower(double power);

    // LEFT JOIN FETCH (rather than the derived-query default) so the returned entities are
    // fully initialized before the transaction closes - required for CharacterServiceImpl's
    // @Cacheable search to be safe to reuse across requests without a LazyInitializationException.
    // ESCAPE '\' + the caller escaping %/_/\ in :name is required because, unlike Spring Data's
    // derived Containing query, a custom @Query does not auto-escape LIKE metacharacters.
    @Query("SELECT c FROM Character c LEFT JOIN FETCH c.crew LEFT JOIN FETCH c.owner "
            + "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\' ORDER BY c.id")
    List<Character> findByNameContainingIgnoreCase(@Param("name") String name);

    List<Character> findByPowerGreaterThanEqual(double minPower);

    @Query("SELECT cb.character FROM CharacterBattle cb WHERE cb.battle.id = :battleId ORDER BY cb.character.id")
    List<Character> findByBattleId(@Param("battleId") int battleId);

    @Query("SELECT DISTINCT c FROM Character c WHERE " +
            "(SELECT COUNT(cb) FROM CharacterBattle cb WHERE cb.character = c) >= :minBattles ORDER BY c.id")
    List<Character> findByMinBattles(@Param("minBattles") int minBattles);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Swordsman s SET s.swordName = :swordName WHERE s.id = :id")
    void updateSwordName(@Param("id") int id, @Param("swordName") String swordName);
}