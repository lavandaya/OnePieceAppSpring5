package be.kdg.programming5.onepiece.business.service.impl;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.Crew;
import be.kdg.programming5.onepiece.business.domain.Powertype;
import be.kdg.programming5.onepiece.business.exception.CharacterNotFoundException;
import be.kdg.programming5.onepiece.business.service.CharacterService;
import be.kdg.programming5.onepiece.data.repository.CharacterRepository;
import be.kdg.programming5.onepiece.data.repository.CrewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CharacterServiceImpl implements CharacterService {

    private static final Logger logger = LoggerFactory.getLogger(CharacterServiceImpl.class);

    private final CharacterRepository repository;
    private final CrewRepository crewRepository;

    public CharacterServiceImpl(CharacterRepository repository, CrewRepository crewRepository) {
        this.repository = repository;
        this.crewRepository = crewRepository;
    }

    @Override
    public List<Character> getAllCharacters() {
        return repository.findAllByOrderByIdAsc();
    }

    @Override
    public Optional<Character> getCharacterById(int id) {
        return repository.findByIdWithCrew(id);
    }

    @Override
    public List<Character> getCharactersByCrew(Crew crew) {
        return repository.findByCrew(crew);
    }

    @Override
    public List<Character> getCharactersByPower(double power) {
        return repository.findByPower(power);
    }

    @Override
    public List<Character> getCharactersInBattle(int battleId) {
        return repository.findByBattles_Id(battleId);
    }

    @Override
    public List<Crew> getAllCrews() {
        return crewRepository.findAll();
    }

    @Override
    public Optional<Crew> getCrewByName(String name) {
        return crewRepository.findByName(name);
    }

    @Override
    public List<Character> findByNameContaining(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Character> findByMinPower(double minPower) {
        return repository.findByPowerGreaterThanEqual(minPower);
    }

    @Override
    public List<Character> findByMinBattles(int minBattles) {
        return repository.findByMinBattles(minBattles);
    }

    @Override
    @Transactional
    public void addCharacter(String name, int age, String appearance,
                             Powertype powertype, double power, String crewName) {
        Character character = new Character(name, age, appearance, powertype, power);
        crewRepository.findByName(crewName).ifPresent(character::setCrew);
        repository.save(character);
        logger.debug("Added character {} (crew='{}')", character, crewName);
    }

    @Override
    @Transactional
    public void deleteCharacter(int id) {
        if (!repository.existsById(id)) {
            throw new CharacterNotFoundException(id);
        }
        repository.deleteById(id);
        logger.debug("Deleted character id={}", id);
    }

    @Override
    @Transactional
    public void updateSwordName(int id, String swordName) {
        if (!repository.existsById(id)) {
            throw new CharacterNotFoundException(id);
        }
        repository.updateSwordName(id, swordName);
        logger.debug("Updated sword name for character id={}", id);
    }
}