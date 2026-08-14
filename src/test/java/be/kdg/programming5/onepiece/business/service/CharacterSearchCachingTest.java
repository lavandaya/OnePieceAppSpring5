package be.kdg.programming5.onepiece.business.service;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.Powertype;
import be.kdg.programming5.onepiece.config.CacheConfig;
import be.kdg.programming5.onepiece.data.repository.CharacterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CharacterSearchCachingTest {

    @Autowired
    private CharacterService characterService;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cacheManager.getCache(CacheConfig.CHARACTER_SEARCH_CACHE).clear();
        characterRepository.save(new Character("Luffy", 18, "img", Powertype.DEVIL_FRUIT, 10));
    }

    @Test
    void findByNameContaining_secondCallWithSameTerm_isServedFromCache() {
        List<Character> first = characterService.findByNameContaining("Luffy");
        assertThat(first).hasSize(1);

        // Bypasses the service (and its @CacheEvict) on purpose: a new matching row is now in the
        // database, but the second call below should still see the stale, cached result - proof
        // that it never re-queried the database for the same search term.
        characterRepository.save(new Character("Luffy Jr", 10, "img", Powertype.WILL, 2));

        List<Character> second = characterService.findByNameContaining("Luffy");
        assertThat(second).hasSize(1);
    }

    @Test
    void addCharacter_evictsSearchCache_soNewMatchIsVisibleOnNextSearch() {
        List<Character> before = characterService.findByNameContaining("Luffy");
        assertThat(before).hasSize(1);

        characterService.addCharacter("Luffy Jr", 10, "img", Powertype.WILL, 2, null, null);

        List<Character> after = characterService.findByNameContaining("Luffy");
        assertThat(after).hasSize(2);
    }

    @Test
    @WithMockUser(username = "tester", roles = "ADMIN")
    void deleteCharacter_evictsSearchCache_soDeletedCharacterDisappearsFromNextSearch() {
        Character luffyJr = characterRepository.save(new Character("Luffy Jr", 10, "img", Powertype.WILL, 2));
        cacheManager.getCache(CacheConfig.CHARACTER_SEARCH_CACHE).clear();

        List<Character> before = characterService.findByNameContaining("Luffy");
        assertThat(before).hasSize(2);

        characterService.deleteCharacter(luffyJr.getId());

        List<Character> after = characterService.findByNameContaining("Luffy");
        assertThat(after).hasSize(1);
    }
}
