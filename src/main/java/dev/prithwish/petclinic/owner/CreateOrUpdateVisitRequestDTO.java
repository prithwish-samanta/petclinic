package dev.prithwish.petclinic.owner;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record CreateOrUpdateVisitRequestDTO(
        @Schema(description = "Date of the clinic visit", example = "2026-03-15") @NotNull @FutureOrPresent @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @Schema(description = "Description of the clinic visit", example = "rabies shot") @NotBlank String description
) {
}
