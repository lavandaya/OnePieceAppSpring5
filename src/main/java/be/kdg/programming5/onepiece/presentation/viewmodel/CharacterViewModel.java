package be.kdg.programming5.onepiece.presentation.viewmodel;

import be.kdg.programming3.onepiece.business.domain.Crew;
import be.kdg.programming3.onepiece.business.domain.Powertype;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CharacterViewModel {

    @NotBlank(message = "{character.name.required}")
    @Size(min = 2, max = 50, message = "{character.name.size}")
    private String name;

    @NotNull(message = "{character.age.required}")
    @Min(value = 0, message = "{character.age.min}")
    @Max(value = 200, message = "{character.age.max}")
    private Integer age;

    @NotBlank(message = "{character.appearance.required}")
    @Pattern(regexp = "https?://.+", message = "{character.appearance.url}")
    private String appearance;

    @NotNull(message = "{character.powertype.required}")
    private Powertype powertype;

    @NotNull(message = "{character.power.required}")
    @DecimalMin(value = "0.0", message = "{character.power.min}")
    @DecimalMax(value = "100.0", message = "{character.power.max}")
    private Double power;

    @NotNull(message = "{character.crew.required}")
    private Crew crew;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getAppearance() { return appearance; }
    public void setAppearance(String appearance) { this.appearance = appearance; }

    public Powertype getPowertype() { return powertype; }
    public void setPowertype(Powertype powertype) { this.powertype = powertype; }

    public Double getPower() { return power; }
    public void setPower(Double power) { this.power = power; }

    public Crew getCrew() { return crew; }
    public void setCrew(Crew crew) { this.crew = crew; }
}