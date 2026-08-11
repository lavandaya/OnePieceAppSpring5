package be.kdg.programming5.onepiece.business.exception;

public class CharacterNotFoundException extends RuntimeException {

    private final int characterId;

    public CharacterNotFoundException(int characterId) {
        super("Character with id=" + characterId + " was not found");
        this.characterId = characterId;
    }

    public int getCharacterId() {
        return characterId;
    }
}