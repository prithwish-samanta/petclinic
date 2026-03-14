package dev.prithwish.petclinic.owner;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record CreateOrUpdateVisitRequestDTO(
        @NotNull @FutureOrPresent @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @NotBlank String description
) {
}
