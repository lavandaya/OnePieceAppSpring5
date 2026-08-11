package be.kdg.programming5.onepiece.data.repository;

import be.kdg.programming3.onepiece.business.domain.Character;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Profile("datajpa")
public interface SpringDataCharacterRepository extends JpaRepository<Character, Integer> {
    List<Character> findByCrew_Name(String crewName);
    List<Character> findByPower(double power);
    List<Character> findByBattles_Id(int battleId);

    List<Character> findByNameContainingIgnoreCase(String name);
    List<Character> findByPowerGreaterThanEqual(double minPower);

    @Query("SELECT DISTINCT c FROM Character c WHERE SIZE(c.battles) >= :minBattles ORDER BY c.id")
    List<Character> findByMinBattles(@Param("minBattles") int minBattles);
}