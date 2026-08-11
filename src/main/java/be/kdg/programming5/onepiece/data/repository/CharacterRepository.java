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

    List<Character> findAllByOrderByIdAsc();

    @Query("SELECT c FROM Character c LEFT JOIN FETCH c.crew WHERE c.id = :id")
    Optional<Character> findByIdWithCrew(@Param("id") int id);

    List<Character> findByCrew(Crew crew);

    List<Character> findByPower(double power);

    List<Character> findByBattles_Id(int battleId);

    List<Character> findByNameContainingIgnoreCase(String name);

    List<Character> findByPowerGreaterThanEqual(double minPower);

    @Query("SELECT DISTINCT c FROM Character c WHERE SIZE(c.battles) >= :minBattles ORDER BY c.id")
    List<Character> findByMinBattles(@Param("minBattles") int minBattles);

    @Modifying
    @Query("UPDATE Swordsman s SET s.swordName = :swordName WHERE s.id = :id")
    void updateSwordName(@Param("id") int id, @Param("swordName") String swordName);
}