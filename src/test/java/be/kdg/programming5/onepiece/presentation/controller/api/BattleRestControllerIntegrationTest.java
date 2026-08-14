package be.kdg.programming5.onepiece.presentation.controller.api;

import be.kdg.programming5.onepiece.testsupport.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BattleRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestDataFactory testData;

    @BeforeEach
    void setUp() {
        testData.battle("Arlong Park showdown", "Arlong Park",
                LocalDateTime.of(2005, 7, 23, 12, 20), "Luffy");
        testData.battle("Marineford war", "Marineford",
                LocalDateTime.of(2006, 3, 15, 16, 45), "Whitebeard");
    }

    @Test
    void searchBattles_byName_returnsMatchingBattles() throws Exception {
        mockMvc.perform(get("/api/battles").param("name", "Arlong").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Arlong Park showdown"));
    }

    @Test
    void searchBattles_noMatches_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/battles").param("name", "Onigashima").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void searchBattles_withoutParameters_returnsAllBattles() throws Exception {
        mockMvc.perform(get("/api/battles").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void createBattle_anonymousCallerWithoutCsrf_returnsCreated() throws Exception {
        mockMvc.perform(post("/api/battles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Water 7 Clash",
                                  "location": "Water 7",
                                  "date": "2026-01-01T10:00:00",
                                  "winner": "Franky"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/battles/")))
                .andExpect(jsonPath("$.name").value("Water 7 Clash"))
                .andExpect(jsonPath("$.winner").value("Franky"));
    }

    @Test
    void createBattle_blankFields_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/battles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "",
                                  "location": "",
                                  "date": "2026-01-01T10:00:00",
                                  "winner": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.location").exists())
                .andExpect(jsonPath("$.winner").exists());
    }
}
