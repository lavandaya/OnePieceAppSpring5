package be.kdg.programming5.onepiece.presentation.controller.api;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.Powertype;
import be.kdg.programming5.onepiece.business.exception.CrewNotFoundException;
import be.kdg.programming5.onepiece.business.service.BattleService;
import be.kdg.programming5.onepiece.business.service.CharacterService;
import be.kdg.programming5.onepiece.presentation.dto.CharacterDto;
import be.kdg.programming5.onepiece.presentation.dto.NewCharacterDto;
import be.kdg.programming5.onepiece.presentation.mapper.BattleMapper;
import be.kdg.programming5.onepiece.presentation.mapper.CharacterMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CharacterRestController.class)
class CharacterRestControllerMockTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CharacterService characterService;
    @MockitoBean
    private BattleService battleService;
    @MockitoBean
    private CharacterMapper characterMapper;
    @MockitoBean
    private BattleMapper battleMapper;

    private static final NewCharacterDto VALID_DTO = new NewCharacterDto(
            "Nico Robin", 28, "https://placehold.co/400x400", Powertype.DEVIL_FRUIT, 8.5,
            "Straw Hat Pirates", null);

    @Test
    void createCharacter_authenticatedUser_returnsCreatedWithLocationAndBecomesOwner() throws Exception {
        Character entity = new Character("Nico Robin", 28, "https://placehold.co/400x400", Powertype.DEVIL_FRUIT, 8.5);
        Character saved = new Character(8, "Nico Robin", 28, "https://placehold.co/400x400", Powertype.DEVIL_FRUIT, 8.5);
        CharacterDto dto = new CharacterDto(8, "Nico Robin", 28, "https://placehold.co/400x400",
                Powertype.DEVIL_FRUIT, 8.5, "Straw Hat Pirates", null);

        when(characterMapper.toEntity(VALID_DTO)).thenReturn(entity);
        when(characterService.createCharacter(entity, "Straw Hat Pirates", "luffy")).thenReturn(saved);
        when(characterMapper.toDto(saved)).thenReturn(dto);

        mockMvc.perform(post("/api/characters")
                        .with(user("luffy").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(VALID_DTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/characters/8")))
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.name").value("Nico Robin"));

        verify(characterService).createCharacter(entity, "Straw Hat Pirates", "luffy");
    }

    @Test
    void createCharacter_anonymousUser_isRejectedBeforeReachingService() throws Exception {
        mockMvc.perform(post("/api/characters")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(VALID_DTO)))
                .andExpect(status().isUnauthorized());

        verify(characterService, never()).createCharacter(any(), any(), any());
    }

    @Test
    void createCharacter_blankName_returnsBadRequestAndNeverCallsService() throws Exception {
        NewCharacterDto invalid = new NewCharacterDto(
                "", 28, "https://placehold.co/400x400", Powertype.DEVIL_FRUIT, 8.5, null, null);

        mockMvc.perform(post("/api/characters")
                        .with(user("luffy").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());

        verify(characterService, never()).createCharacter(any(), any(), any());
    }

    @Test
    void createCharacter_unknownCrew_returnsBadRequestWithMessage() throws Exception {
        Character entity = new Character("Nico Robin", 28, "https://placehold.co/400x400", Powertype.DEVIL_FRUIT, 8.5);

        when(characterMapper.toEntity(VALID_DTO)).thenReturn(entity);
        when(characterService.createCharacter(entity, "Straw Hat Pirates", "luffy"))
                .thenThrow(new CrewNotFoundException("Straw Hat Pirates"));

        mockMvc.perform(post("/api/characters")
                        .with(user("luffy").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(VALID_DTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Crew 'Straw Hat Pirates' was not found"));
    }

    @Test
    void createCharacter_missingCsrfToken_isForbidden() throws Exception {
        mockMvc.perform(post("/api/characters")
                        .with(user("luffy").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(VALID_DTO)))
                .andExpect(status().isForbidden());

        verify(characterService, never()).createCharacter(any(), any(), any());
    }
}
