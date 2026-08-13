package be.kdg.programming5.onepiece.business.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "characters")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "character_type", length = 20)
@DiscriminatorValue("CHARACTER")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id")
    private int id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false, length = 255)
    private String appearance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Powertype powertype;

    @Column(nullable = false)
    private double power;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_name")
    private Crew crew;



    protected Character() {
    }

    public Character(int id, String name, int age, String appearance, Powertype powertype, double power) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.appearance = appearance;
        this.powertype = powertype;
        this.power = power;
    }

    public Character(String name, int age, String appearance, Powertype powertype, double power) {
        this(0, name, age, appearance, powertype, power);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getAppearance() { return appearance; }
    public Powertype getPowertype() { return powertype; }
    public double getPower() { return power; }

    public Crew getCrew() { return crew; }
    public void setCrew(Crew crew) { this.crew = crew; }


    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setAppearance(String appearance) { this.appearance = appearance; }
    public void setPowertype(Powertype powertype) { this.powertype = powertype; }
    public void setPower(double power) { this.power = power; }

    @Override
    public String toString() {
        return "Character #" + id + ", " + name + ", powertype - " + powertype + ", power - " + power + " DON";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return id == character.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}