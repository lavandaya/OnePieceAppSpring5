package be.kdg.programming5.onepiece.presentation.viewmodel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BattleViewModel {

    @NotBlank(message = "{battle.name.required}")
    @Size(max = 80, message = "{battle.name.size}")
    private String name;

    @NotBlank(message = "{battle.location.required}")
    private String location;

    @NotNull(message = "{battle.date.required}")
    @PastOrPresent(message = "{battle.date.past}")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;

    @NotBlank(message = "{battle.winner.required}")
    private String winner;

    private List<Integer> characterIds = new ArrayList<>();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }

    public List<Integer> getCharacterIds() { return characterIds; }
    public void setCharacterIds(List<Integer> characterIds) { this.characterIds = characterIds; }
}