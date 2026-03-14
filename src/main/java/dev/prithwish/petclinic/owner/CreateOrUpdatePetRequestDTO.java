package dev.prithwish.petclinic.owner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record CreateOrUpdatePetRequestDTO(
        @NotBlank String name,
        @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
        @NotNull String petType
) {
}
