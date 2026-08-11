package be.kdg.programming5.onepiece.business.service.impl;

import be.kdg.programming5.onepiece.business.domain.Battle;
import be.kdg.programming5.onepiece.business.service.BattleService;
import be.kdg.programming5.onepiece.data.repository.BattleRepository;
import be.kdg.programming5.onepiece.data.repository.CharacterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BattleServiceImpl implements BattleService {

    private static final Logger logger = LoggerFactory.getLogger(BattleServiceImpl.class);

    private final BattleRepository repository;
    private final CharacterRepository characterRepository;

    public BattleServiceImpl(BattleRepository repository, CharacterRepository characterRepository) {
        this.repository = repository;
        this.characterRepository = characterRepository;
    }

    @Override
    public List<Battle> getAllBattles() {
        return repository.findAllByOrderByIdAsc();
    }

    @Override
    public Optional<Battle> getBattleById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Battle> findBattles(String nameContains, LocalDate fromDate) {
        boolean hasName = nameContains != null && !nameContains.isBlank();
        LocalDateTime from = fromDate != null ? fromDate.atStartOfDay() : null;

        if (hasName && from != null) {
            return repository.findByNameContainingIgnoreCaseAndDateGreaterThanEqualOrderByIdAsc(nameContains, from);
        }
        if (hasName) {
            return repository.findByNameContainingIgnoreCaseOrderByIdAsc(nameContains);
        }
        if (from != null) {
            return repository.findByDateGreaterThanEqualOrderByIdAsc(from);
        }
        return repository.findAllByOrderByIdAsc();
    }

    @Override
    public List<Battle> getBattlesForCharacter(int characterId) {
        return repository.findByCharacters_Id(characterId);
    }

    @Override
    @Transactional
    public void addBattle(String name, String location, LocalDateTime date, String winner, List<Integer> characterIds) {
        Battle battle = new Battle(name, location, date, winner);
        repository.save(battle);

        if (characterIds != null) {
            characterIds.forEach(charId ->
                    characterRepository.findById(charId)
                            .ifPresent(character -> character.getBattles().add(battle)));
        }
        logger.debug("Added battle {} with {} character(s)", battle,
                characterIds == null ? 0 : characterIds.size());
    }

    @Override
    @Transactional
    public void deleteBattle(int id) {
        // Battle is the inverse side of the m2m, so join rows must be removed
        // from the owning side (Character) before the battle can be deleted.
        characterRepository.findByBattles_Id(id)
                .forEach(character -> character.getBattles().removeIf(battle -> battle.getId() == id));
        repository.deleteById(id);
        logger.debug("Deleted battle id={}", id);
    }
}