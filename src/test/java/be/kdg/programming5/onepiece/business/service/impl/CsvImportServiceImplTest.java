package be.kdg.programming5.onepiece.business.service.impl;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.Swordsman;
import be.kdg.programming5.onepiece.business.service.CharacterImport;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
    void importCharacters_validRows_buildsOneImportPerRowAndCallsBulkCreateOnce() {
        List<String> lines = List.of(
                "Jinbe,45,https://img,WILL,8.0,Straw Hat Pirates,",
                "Brook,90,https://img,WILL,7.5,Straw Hat Pirates,Soul Solid");

        service.importCharacters(lines, "admin");

        ArgumentCaptor<List<CharacterImport>> captor = ArgumentCaptor.forClass(List.class);
        verify(characterService).createCharactersBulk(captor.capture(), eq("admin"));

        List<CharacterImport> imports = captor.getValue();
        assertThat(imports).hasSize(2);
        assertThat(imports.get(0).character().getName()).isEqualTo("Jinbe");
        assertThat(imports.get(0).crewName()).isEqualTo("Straw Hat Pirates");
        assertThat(imports.get(0).character()).isNotInstanceOf(Swordsman.class);
        assertThat(imports.get(1).character()).isInstanceOf(Swordsman.class);
        assertThat(((Swordsman) imports.get(1).character()).getSwordName()).isEqualTo("Soul Solid");
    }

    @Test
    void importCharacters_rowWithoutCrew_passesNullCrewName() {
        service.importCharacters(List.of("Hancock,33,https://img,DEVIL_FRUIT,9.0,,"), "admin");

        ArgumentCaptor<List<CharacterImport>> captor = ArgumentCaptor.forClass(List.class);
        verify(characterService).createCharactersBulk(captor.capture(), eq("admin"));
        assertThat(captor.getValue().get(0).crewName()).isNull();
    }

    @Test
    void importCharacters_malformedRow_isExcludedFromTheBatchWithoutAbortingTheImport() {
        List<String> lines = List.of(
                "BadRow,notAnAge,https://img,DEVIL_FRUIT,9.0,,",
                "Jinbe,45,https://img,WILL,8.0,,");

        service.importCharacters(lines, "admin");

        ArgumentCaptor<List<CharacterImport>> captor = ArgumentCaptor.forClass(List.class);
        verify(characterService).createCharactersBulk(captor.capture(), eq("admin"));
        assertThat(captor.getValue()).hasSize(1);
        assertThat(captor.getValue().get(0).character().getName()).isEqualTo("Jinbe");
    }

    @Test
    void importCharacters_blankLines_areSkipped() {
        service.importCharacters(List.of("", "   "), "admin");

        ArgumentCaptor<List<CharacterImport>> captor = ArgumentCaptor.forClass(List.class);
        verify(characterService).createCharactersBulk(captor.capture(), eq("admin"));
        assertThat(captor.getValue()).isEmpty();
    }

    @Test
    void importCharacters_ageOutOfRange_rowIsRejected() {
        service.importCharacters(List.of("Weirdo,201,https://img,DEVIL_FRUIT,9.0,,"), "admin");

        ArgumentCaptor<List<CharacterImport>> captor = ArgumentCaptor.forClass(List.class);
        verify(characterService).createCharactersBulk(captor.capture(), eq("admin"));
        assertThat(captor.getValue()).isEmpty();
    }

    @Test
    void importCharacters_powerOutOfRange_rowIsRejected() {
        service.importCharacters(List.of("Weirdo,30,https://img,DEVIL_FRUIT,101,,"), "admin");

        ArgumentCaptor<List<CharacterImport>> captor = ArgumentCaptor.forClass(List.class);
        verify(characterService).createCharactersBulk(captor.capture(), eq("admin"));
        assertThat(captor.getValue()).isEmpty();
    }

    @Test
    void importCharacters_appearanceNotAUrl_rowIsRejected() {
        service.importCharacters(List.of("Weirdo,30,not-a-url,DEVIL_FRUIT,9.0,,"), "admin");

        ArgumentCaptor<List<CharacterImport>> captor = ArgumentCaptor.forClass(List.class);
        verify(characterService).createCharactersBulk(captor.capture(), eq("admin"));
        assertThat(captor.getValue()).isEmpty();
    }

    @Test
    void importCharacters_nameTooShort_rowIsRejected() {
        service.importCharacters(List.of("X,30,https://img,DEVIL_FRUIT,9.0,,"), "admin");

        ArgumentCaptor<List<CharacterImport>> captor = ArgumentCaptor.forClass(List.class);
        verify(characterService).createCharactersBulk(captor.capture(), eq("admin"));
        assertThat(captor.getValue()).isEmpty();
    }

    @Test
    void importCharacters_bulkCreateReportsFewerSaved_stillLogsWithoutThrowing() {
        when(characterService.createCharactersBulk(any(), eq("admin"))).thenReturn(0);

        service.importCharacters(List.of("Jinbe,45,https://img,WILL,8.0,,"), "admin");

        verify(characterService).createCharactersBulk(any(), eq("admin"));
    }
}
