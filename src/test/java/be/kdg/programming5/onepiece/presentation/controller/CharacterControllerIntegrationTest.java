package be.kdg.programming5.onepiece.presentation.controller;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CharacterControllerIntegrationTest {

    private static final int UNKNOWN_ID = 9999;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestDataFactory testData;
    @Autowired
    private CacheManager cacheManager;

    private Character luffy;
    private Character zoro;
    private Character trafalgar;

    @BeforeEach
    void setUp() {
        cacheManager.getCache(CacheConfig.CHARACTER_SEARCH_CACHE).clear();
        Crew strawHats = testData.crew("Straw Hat Pirates", "Going Merry");
        Crew heartPirates = testData.crew("Heart Pirates", "Polar Tang");

        User luffyUser = testData.user("luffy", Role.USER);
        User zoroUser = testData.user("zoro", Role.USER);

        luffy = testData.character("Luffy", 18, Powertype.DEVIL_FRUIT, 10, strawHats, luffyUser);
        zoro = testData.swordsman("Zoro", 20, Powertype.WILL, 9, "Wado Ichimonji", strawHats, zoroUser);
        testData.character("Nami", 19, Powertype.NO_POWER, 1, strawHats, luffyUser);
        trafalgar = testData.character("Trafalgar", 21, Powertype.DEVIL_FRUIT, 10, heartPirates, luffyUser);

        Battle arlongPark = testData.battle("Arlong Park showdown", "Arlong Park",
                LocalDateTime.of(2005, 7, 23, 12, 20), "Luffy");
        testData.join(luffy, arlongPark);
    }

    @Test
    void showCharacters_withoutFilter_returnsAllCharacters() throws Exception {
        mockMvc.perform(get("/characters"))
                .andExpect(status().isOk())
                .andExpect(view().name("characters"))
                .andExpect(model().attribute("characters", hasSize(4)))
                .andExpect(model().attribute("crews", hasSize(2)))
                .andExpect(model().attribute("selectedCrew", nullValue()))
                .andExpect(model().attributeExists("powertypes"));
    }

    @Test
    void showCharacters_filteredByCrew_returnsOnlyMembersOfThatCrew() throws Exception {
        mockMvc.perform(get("/characters").param("crewName", "Heart Pirates"))
                .andExpect(status().isOk())
                .andExpect(view().name("characters"))
                .andExpect(model().attribute("characters", hasSize(1)))
                .andExpect(model().attribute("selectedCrew", "Heart Pirates"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Trafalgar")));
    }

    @Test
    void showCharacters_filteredByUnknownCrew_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/characters").param("crewName", "Red Hair Pirates"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("characters", hasSize(0)));
    }

    @Test
    void searchCharacters_withoutParameters_showsFormWithoutResults() throws Exception {
        mockMvc.perform(get("/characters/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("characterSearch"))
                .andExpect(model().attribute("results", nullValue()));
    }

    @Test
    void searchCharacters_byName_returnsMatchingCharacters() throws Exception {
        mockMvc.perform(get("/characters/search").param("name", "zor"))
                .andExpect(status().isOk())
                .andExpect(view().name("characterSearch"))
                .andExpect(model().attribute("results", hasSize(1)))
                .andExpect(model().attribute("searchName", "zor"));
    }

    @Test
    void searchCharacters_byNameWithoutMatches_returnsEmptyResultList() throws Exception {
        mockMvc.perform(get("/characters/search").param("name", "Shanks"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("results", hasSize(0)));
    }

    @Test
    void searchCharacters_byMinPower_returnsCharactersAboveThreshold() throws Exception {
        mockMvc.perform(get("/characters/search").param("minPower", "9"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("results", hasSize(3)))
                .andExpect(model().attribute("searchMinPower", 9.0));
    }

    @Test
    void searchCharacters_byMinBattles_returnsOnlyCharactersWithEnoughBattles() throws Exception {
        mockMvc.perform(get("/characters/search").param("minBattles", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("results", hasSize(1)))
                .andExpect(model().attribute("searchMinBattles", 1));

        mockMvc.perform(get("/characters/search").param("minBattles", "2"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("results", hasSize(0)));
    }

    @Test
    void searchCharacters_nameTakesPrecedenceOverMinPower() throws Exception {
        mockMvc.perform(get("/characters/search")
                        .param("name", "Nami")
                        .param("minPower", "9"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("results", hasSize(1)));
    }

    @Test
    void showCharacterDetail_existingId_returnsDetailViewWithBattles() throws Exception {
        mockMvc.perform(get("/characters/{id}", luffy.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("characterDetail"))
                .andExpect(model().attribute("character", luffy))
                .andExpect(model().attribute("battles", hasSize(1)))
                .andExpect(model().attributeDoesNotExist("swordName"));
    }

    @Test
    void showCharacterDetail_swordsmanId_addsSwordNameToModel() throws Exception {
        mockMvc.perform(get("/characters/{id}", zoro.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("characterDetail"))
                .andExpect(model().attribute("swordName", "Wado Ichimonji"))
                .andExpect(model().attribute("battles", hasSize(0)));
    }

    @Test
    void showCharacterDetail_unknownId_redirectsToOverview() throws Exception {
        mockMvc.perform(get("/characters/{id}", UNKNOWN_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/characters"));
    }

    @Test
    void showCharacterDetail_anonymousUser_hidesPowerValue() throws Exception {
        mockMvc.perform(get("/characters/{id}", trafalgar.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("???")));
    }

    @Test
    void showAddCharacterForm_anonymousUser_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/characters/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "luffy", roles = "USER")
    void showAddCharacterForm_authenticatedUser_returnsForm() throws Exception {
        mockMvc.perform(get("/characters/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("addCharacter"))
                .andExpect(model().attributeExists("characterViewModel", "crews", "powertypes"));
    }
}