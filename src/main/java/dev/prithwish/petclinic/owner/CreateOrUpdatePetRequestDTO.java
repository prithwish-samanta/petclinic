package dev.prithwish.petclinic.owner;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record CreateOrUpdatePetRequestDTO(
        @Schema(description = "Name of the pet", example = "Leo") @NotBlank String name,
        @Schema(description = "Birth date of the pet", example = "2010-09-07") @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
        @Schema(description = "Type of the pet", example = "cat") @NotNull String petType
) {
}
