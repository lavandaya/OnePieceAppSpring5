package be.kdg.programming5.onepiece.config;

import be.kdg.programming5.onepiece.business.domain.Character;
import be.kdg.programming5.onepiece.data.repository.CharacterRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("characterSecurity")
public class CharacterSecurity {

    private final CharacterRepository characterRepository;

    public CharacterSecurity(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    public boolean isOwner(int characterId, Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return characterRepository.findByIdWithCrew(characterId)
                .map(Character::getOwner)
                .map(owner -> owner.getUsername().equals(authentication.getName()))
                .orElse(false);
    }
}