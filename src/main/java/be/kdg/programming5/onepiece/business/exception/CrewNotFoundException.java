package be.kdg.programming5.onepiece.business.exception;

public class CrewNotFoundException extends RuntimeException {

    private final String crewName;

    public CrewNotFoundException(String crewName) {
        super("Crew '" + crewName + "' was not found");
        this.crewName = crewName;
    }

    public String getCrewName() {
        return crewName;
    }
}