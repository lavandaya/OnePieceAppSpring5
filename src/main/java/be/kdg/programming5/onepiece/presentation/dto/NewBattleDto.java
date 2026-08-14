package be.kdg.programming5.onepiece.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record NewBattleDto(

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 120, message = "Name must be between 2 and 120 characters")
        String name,

        @NotBlank(message = "Location is required")
        @Size(min = 2, max = 120, message = "Location must be between 2 and 120 characters")
        String location,

        @NotNull(message = "Date is required")
        LocalDateTime date,

        @NotBlank(message = "Winner is required")
        @Size(min = 2, max = 100, message = "Winner must be between 2 and 100 characters")
        String winner
) {
}
