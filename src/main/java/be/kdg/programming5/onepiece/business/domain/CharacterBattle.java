package be.kdg.programming5.onepiece.business.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "character_battles",
        uniqueConstraints = @UniqueConstraint(columnNames = {"character_id", "battle_id"}))
public class CharacterBattle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_id", nullable = false)
    private Battle battle;

    protected CharacterBattle() {
    }

    public CharacterBattle(Character character, Battle battle) {
        this.character = character;
        this.battle = battle;
    }

    public int getId() { return id; }
    public Character getCharacter() { return character; }
    public Battle getBattle() { return battle; }
}