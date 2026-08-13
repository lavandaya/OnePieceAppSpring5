package be.kdg.programming5.onepiece.business.exception;

public class UsernameTakenException extends RuntimeException {

    private final String username;

    public UsernameTakenException(String username) {
        super("Username '" + username + "' is already taken");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}