package be.kdg.programming5.onepiece.presentation.dto;

import be.kdg.programming5.onepiece.business.domain.Powertype;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateCharacterDto(

        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @Min(value = 0, message = "Age cannot be negative")
        @Max(value = 200, message = "Age is unrealistically high")
        Integer age,

        @Pattern(regexp = "https?://.+", message = "Appearance must be a valid http(s) URL")
        String appearance,

        Powertype powertype,

        @DecimalMin(value = "0.0", message = "Power cannot be negative")
        @DecimalMax(value = "100.0", message = "Power may not exceed 100 DON")
        Double power,

        String crewName,

        @Size(max = 100, message = "Sword name may not exceed 100 characters")
        String swordName
) {
}