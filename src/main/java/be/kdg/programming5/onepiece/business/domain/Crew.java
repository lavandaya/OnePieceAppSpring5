package be.kdg.programming5.onepiece.business.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "crews")
public class Crew {
    @Id
    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "has_bounty", nullable = false)
    private boolean hasBounty;

    @Column(name = "ship_name", nullable = false, length = 100)
    private String shipName;

    @OneToMany(mappedBy = "crew")
    private List<Character> members = new ArrayList<>();

    protected Crew() {
    }

    public Crew(String name, boolean hasBounty, String shipName) {
        this.name = name;
        this.hasBounty = hasBounty;
        this.shipName = shipName;
    }

    public String getName() { return name; }
    public boolean isHasBounty() { return hasBounty; }
    public String getShipName() { return shipName; }

    public List<Character> getMembers() { return members; }

    public void addCharacter(Character character) {
        if (character != null && !members.contains(character)) {
            members.add(character);
            character.setCrew(this);
        }
    }

    @Override
    public String toString() {
        return "Crew{" + name + ", ship='" + shipName + "'}";
    }
}