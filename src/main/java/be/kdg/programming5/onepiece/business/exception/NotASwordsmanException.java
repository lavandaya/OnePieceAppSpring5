package be.kdg.programming5.onepiece.business.exception;

public class NotASwordsmanException extends RuntimeException {

    private final int characterId;

    public NotASwordsmanException(int characterId) {
        super("Character with id=" + characterId + " is not a swordsman and has no sword");
        this.characterId = characterId;
    }

    public int getCharacterId() {
        return characterId;
    }
}