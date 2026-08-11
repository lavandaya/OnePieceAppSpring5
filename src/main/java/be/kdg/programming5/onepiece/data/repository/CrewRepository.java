package be.kdg.programming5.onepiece.data.repository;

import be.kdg.programming5.onepiece.business.domain.Crew;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew, String> {
    Optional<Crew> findByName(String name);
}