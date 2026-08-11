package be.kdg.programming5.onepiece.data.repository;

import be.kdg.programming3.onepiece.business.domain.Crew;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("datajpa")
public interface SpringDataCrewRepository extends JpaRepository<Crew, String> {
}