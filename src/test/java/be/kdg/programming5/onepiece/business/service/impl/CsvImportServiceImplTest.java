package be.kdg.programming5.onepiece.business.service.impl;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.Swordsman;
import be.kdg.programming5.onepiece.business.service.CharacterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class CsvImportServiceImplTest {

    @Mock
    private CharacterService characterService;

    private CsvImportServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CsvImportServiceImpl(characterService);
    }

    @Test
    void importCharacters_validRows_createsOneCharacterPerRow() {
        List<String> lines = List.of(
                "Jinbe,45,https://img,WILL,8.0,Straw Hat Pirates,",
                "Brook,90,https://img,WILL,7.5,Straw Hat Pirates,Soul Solid");

        service.importCharacters(lines, "admin");

        ArgumentCaptor<Character> captor = ArgumentCaptor.forClass(Character.class);
        verify(characterService, times(2)).createCharacter(captor.capture(), eq("Straw Hat Pirates"), eq("admin"));

        List<Character> created = captor.getAllValues();
        assertThat(created.get(0).getName()).isEqualTo("Jinbe");
        assertThat(created.get(0)).isNotInstanceOf(Swordsman.class);
        assertThat(created.get(1)).isInstanceOf(Swordsman.class);
        assertThat(((Swordsman) created.get(1)).getSwordName()).isEqualTo("Soul Solid");
    }

    @Test
    void importCharacters_rowWithoutCrew_passesNullCrewName() {
        service.importCharacters(List.of("Hancock,33,https://img,DEVIL_FRUIT,9.0,,"), "admin");

        verify(characterService).createCharacter(any(), isNull(), eq("admin"));
    }

    @Test
    void importCharacters_invalidRow_isSkippedWithoutAbortingTheImport() {
        List<String> lines = List.of(
                "BadRow,notAnAge,https://img,DEVIL_FRUIT,9.0,,",
                "Jinbe,45,https://img,WILL,8.0,,");

        service.importCharacters(lines, "admin");

        verify(characterService, times(1)).createCharacter(any(), any(), eq("admin"));
    }

    @Test
    void importCharacters_blankLines_areSkipped() {
        service.importCharacters(List.of("", "   "), "admin");

        verifyNoInteractions(characterService);
    }
}
