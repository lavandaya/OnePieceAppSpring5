package be.kdg.programming5.onepiece.presentation.controller.api;

import be.kdg.programming5.onepiece.business.domain.Battle;
import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.Crew;
import be.kdg.programming5.onepiece.business.domain.Powertype;
import be.kdg.programming5.onepiece.business.domain.Role;
import be.kdg.programming5.onepiece.business.domain.User;
import be.kdg.programming5.onepiece.config.CacheConfig;
import be.kdg.programming5.onepiece.testsupport.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CharacterRestControllerIntegrationTest {

    private static final int UNKNOWN_ID = 9999;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestDataFactory testData;
    @Autowired
    private CacheManager cacheManager;

    private Character luffy;
    private Character zoro;

    @BeforeEach
    void setUp() {
        cacheManager.getCache(CacheConfig.CHARACTER_SEARCH_CACHE).clear();
        Crew strawHats = testData.crew("Straw Hat Pirates", "Going Merry");
        User luffyUser = testData.user("luffy", Role.USER);
        User zoroUser = testData.user("zoro", Role.USER);

        luffy = testData.character("Luffy", 18, Powertype.DEVIL_FRUIT, 10, strawHats, luffyUser);
        zoro = testData.swordsman("Zoro", 20, Powertype.WILL, 9, "Wado Ichimonji", strawHats, zoroUser);
        testData.character("Nami", 19, Powertype.NO_POWER, 1, strawHats, luffyUser);

        Battle arlongPark = testData.battle("Arlong Park showdown", "Arlong Park",
                LocalDateTime.of(2005, 7, 23, 12, 20), "Luffy");
        testData.join(luffy, arlongPark);
    }

    @Test
    void searchCharacters_withoutParameters_returnsAllCharactersOrderedById() throws Exception {
        mockMvc.perform(get("/api/characters").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Luffy"))
                .andExpect(jsonPath("$[0].crewName").value("Straw Hat Pirates"))
                .andExpect(jsonPath("$[1].swordName").value("Wado Ichimonji"));
    }

    @Test
    void searchCharacters_byName_returnsOnlyMatchingCharacters() throws Exception {
        mockMvc.perform(get("/api/characters").param("name", "zor").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(zoro.getId()))
                .andExpect(jsonPath("$[0].name").value("Zoro"));
    }

    @Test
    void searchCharacters_byNameWithoutMatches_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/characters").param("name", "Shanks").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void searchCharacters_byMinPower_returnsCharactersAboveThreshold() throws Exception {
        mockMvc.perform(get("/api/characters").param("minPower", "9").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Luffy", "Zoro")));
    }

    @Test
    void searchCharacters_blankName_fallsBackToAllCharacters() throws Exception {
        mockMvc.perform(get("/api/characters").param("name", "   ").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void getCharacter_existingId_returnsCharacter() throws Exception {
        mockMvc.perform(get("/api/characters/{id}", luffy.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(luffy.getId()))
                .andExpect(jsonPath("$.name").value("Luffy"))
                .andExpect(jsonPath("$.powertype").value("DEVIL_FRUIT"))
                .andExpect(jsonPath("$.power").value(10.0))
                .andExpect(jsonPath("$.swordName").doesNotExist());
    }

    @Test
    void getCharacter_swordsmanId_includesSwordName() throws Exception {
        mockMvc.perform(get("/api/characters/{id}", zoro.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swordName").value("Wado Ichimonji"));
    }

    @Test
    void getCharacter_unknownId_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/characters/{id}", UNKNOWN_ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void getCharacter_unsupportedAcceptHeader_returnsNotAcceptable() throws Exception {
        mockMvc.perform(get("/api/characters/{id}", luffy.getId()).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void getCharacterBattles_characterWithBattles_returnsBattles() throws Exception {
        mockMvc.perform(get("/api/characters/{id}/battles", luffy.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Arlong Park showdown"))
                .andExpect(jsonPath("$[0].winner").value("Luffy"));
    }

    @Test
    void getCharacterBattles_characterWithoutBattles_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/characters/{id}/battles", zoro.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getCharacterBattles_unknownCharacter_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/characters/{id}/battles", UNKNOWN_ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }
}