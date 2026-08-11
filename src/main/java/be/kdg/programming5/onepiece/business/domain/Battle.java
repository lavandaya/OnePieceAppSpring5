package be.kdg.programming5.onepiece.business.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "battles")
public class Battle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_id")
    private int id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 120)
    private String location;

    @Column(name = "fought_at", nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, length = 100)
    private String winner;



    protected Battle() {
    }

    public Battle(int id, String name, String location, LocalDateTime date, String winner) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.winner = winner;
    }

    public Battle(String name, String location, LocalDateTime date, String winner) {
        this(0, name, location, date, winner);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public LocalDateTime getDate() { return date; }
    public String getWinner() { return winner; }


    @Override
    public String toString() {
        return "Battle #" + id + " {" + name + ", " + location + ", " + date + ", winner=" + winner + "}";
    }
}