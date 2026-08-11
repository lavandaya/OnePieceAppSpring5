package be.kdg.programming5.onepiece.business.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SWORDSMAN")
public class Swordsman extends Character {

    @Column(name = "sword_name", length = 100)
    private String swordName;

    protected Swordsman() {
    }

    public Swordsman(int id, String name, int age, String appearance,
                     Powertype powertype, double power, String swordName) {
        super(id, name, age, appearance, powertype, power);
        this.swordName = swordName;
    }

    public Swordsman(String name, int age, String appearance,
                     Powertype powertype, double power, String swordName) {
        this(0, name, age, appearance, powertype, power, swordName);
    }

    public String getSwordName() { return swordName; }
    public void setSwordName(String swordName) { this.swordName = swordName; }

    @Override
    public String toString() {
        return super.toString() + " [Swordsman: " + swordName + "]";
    }
}