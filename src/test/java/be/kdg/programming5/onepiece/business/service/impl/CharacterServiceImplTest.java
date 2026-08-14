package be.kdg.programming5.onepiece.business.service.impl;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.business.domain.Crew;
import be.kdg.programming5.onepiece.business.domain.Powertype;
import be.kdg.programming5.onepiece.business.domain.User;
import be.kdg.programming5.onepiece.business.exception.CrewNotFoundException;
import be.kdg.programming5.onepiece.business.service.CharacterImport;
import be.kdg.programming5.onepiece.data.repository.CharacterBattleRepository;
import be.kdg.programming5.onepiece.data.repository.CharacterRepository;
import be.kdg.programming5.onepiece.data.repository.CrewRepository;
import be.kdg.programming5.onepiece.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterServiceImplTest {

    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private CrewRepository crewRepository;
    @Mock
    private CharacterBattleRepository characterBattleRepository;
    @Mock
    private UserRepository userRepository;

    private CharacterServiceImpl service;

    private Crew crew;
    private User owner;

    @BeforeEach
    void setUp() {
        service = new CharacterServiceImpl(characterRepository, crewRepository, characterBattleRepository, userRepository);
        crew = new Crew("Straw Hat Pirates", true, "Going Merry");
        owner = new User("luffy", "hash", "luffy@onepiece.com", be.kdg.programming5.onepiece.business.domain.Role.USER);
    }

    @Test
    void addCharacter_withKnownCrewAndOwner_setsBothAndSaves() {
        when(crewRepository.findByName("Straw Hat Pirates")).thenReturn(Optional.of(crew));
        when(userRepository.findByUsername("luffy")).thenReturn(Optional.of(owner));

        service.addCharacter("Nico Robin", 28, "img", Powertype.DEVIL_FRUIT, 8.5, "Straw Hat Pirates", "luffy");

        ArgumentCaptor<Character> captor = ArgumentCaptor.forClass(Character.class);
        verify(characterRepository).save(captor.capture());
        Character saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("Nico Robin");
        assertThat(saved.getCrew()).isEqualTo(crew);
        assertThat(saved.getOwner()).isEqualTo(owner);
    }

    @Test
    void addCharacter_withUnknownCrew_savesWithoutSettingCrew() {
        when(crewRepository.findByName("Marines")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("luffy")).thenReturn(Optional.of(owner));

        service.addCharacter("Smoker", 36, "img", Powertype.DEVIL_FRUIT, 6.0, "Marines", "luffy");

        ArgumentCaptor<Character> captor = ArgumentCaptor.forClass(Character.class);
        verify(characterRepository).save(captor.capture());
        assertThat(captor.getValue().getCrew()).isNull();
    }

    @Test
    void addCharacter_withNullOwnerUsername_savesWithoutOwnerAndNeverQueriesUserRepository() {
        when(crewRepository.findByName("Straw Hat Pirates")).thenReturn(Optional.of(crew));

        service.addCharacter("Nico Robin", 28, "img", Powertype.DEVIL_FRUIT, 8.5, "Straw Hat Pirates", null);

        verify(userRepository, never()).findByUsername(any());
        ArgumentCaptor<Character> captor = ArgumentCaptor.forClass(Character.class);
        verify(characterRepository).save(captor.capture());
        assertThat(captor.getValue().getOwner()).isNull();
    }

    @Test
    void createCharacter_withKnownCrew_setsCrewAndOwnerAndReturnsSavedEntity() {
        Character toCreate = new Character("Nico Robin", 28, "img", Powertype.DEVIL_FRUIT, 8.5);
        Character persisted = new Character(8, "Nico Robin", 28, "img", Powertype.DEVIL_FRUIT, 8.5);

        when(crewRepository.findByName("Straw Hat Pirates")).thenReturn(Optional.of(crew));
        when(userRepository.findByUsername("luffy")).thenReturn(Optional.of(owner));
        when(characterRepository.save(toCreate)).thenReturn(persisted);

        Character result = service.createCharacter(toCreate, "Straw Hat Pirates", "luffy");

        assertThat(result).isEqualTo(persisted);
        assertThat(toCreate.getCrew()).isEqualTo(crew);
        assertThat(toCreate.getOwner()).isEqualTo(owner);
        verify(characterRepository, times(1)).save(toCreate);
    }

    @Test
    void createCharacter_withUnknownCrew_throwsCrewNotFoundExceptionAndNeverSaves() {
        Character toCreate = new Character("Smoker", 36, "img", Powertype.DEVIL_FRUIT, 6.0);
        when(crewRepository.findByName("Marines")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createCharacter(toCreate, "Marines", "luffy"))
                .isInstanceOf(CrewNotFoundException.class)
                .hasMessageContaining("Marines");

        verify(characterRepository, never()).save(any());
    }

    @Test
    void createCharacter_withBlankCrewName_skipsCrewLookupAndSaves() {
        Character toCreate = new Character("Smoker", 36, "img", Powertype.DEVIL_FRUIT, 6.0);
        when(characterRepository.save(toCreate)).thenReturn(toCreate);

        service.createCharacter(toCreate, "", null);

        verify(crewRepository, never()).findByName(any());
        verify(characterRepository).save(toCreate);
    }

    @Test
    void createCharactersBulk_resolvesOwnerOnceAndCrewOncePerDistinctName() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(owner));
        when(crewRepository.findByName("Straw Hat Pirates")).thenReturn(Optional.of(crew));
        when(characterRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<CharacterImport> imports = List.of(
                new CharacterImport(new Character("Jinbe", 45, "img", Powertype.WILL, 8.0), "Straw Hat Pirates"),
                new CharacterImport(new Character("Brook", 90, "img", Powertype.WILL, 7.5), "Straw Hat Pirates"),
                new CharacterImport(new Character("Boa Hancock", 33, "img", Powertype.DEVIL_FRUIT, 9.0), null));

        int saved = service.createCharactersBulk(imports, "admin");

        assertThat(saved).isEqualTo(3);
        verify(userRepository, times(1)).findByUsername("admin");
        verify(crewRepository, times(1)).findByName("Straw Hat Pirates");
        verify(characterRepository, times(3)).save(any());
    }

    @Test
    void createCharactersBulk_unknownCrew_skipsThatRowButSavesTheRest() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(owner));
        when(crewRepository.findByName("Marines")).thenReturn(Optional.empty());
        when(characterRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<CharacterImport> imports = List.of(
                new CharacterImport(new Character("Smoker", 36, "img", Powertype.DEVIL_FRUIT, 6.0), "Marines"),
                new CharacterImport(new Character("Boa Hancock", 33, "img", Powertype.DEVIL_FRUIT, 9.0), null));

        int saved = service.createCharactersBulk(imports, "admin");

        assertThat(saved).isEqualTo(1);
        verify(characterRepository, times(1)).save(any());
    }

    @Test
    void createCharactersBulk_nullOwnerUsername_neverQueriesUserRepository() {
        when(characterRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<CharacterImport> imports = List.of(
                new CharacterImport(new Character("Boa Hancock", 33, "img", Powertype.DEVIL_FRUIT, 9.0), null));

        service.createCharactersBulk(imports, null);

        verify(userRepository, never()).findByUsername(any());
    }
}
