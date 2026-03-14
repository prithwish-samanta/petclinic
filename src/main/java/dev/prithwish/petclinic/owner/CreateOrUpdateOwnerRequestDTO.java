package dev.prithwish.petclinic.owner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateOrUpdateOwnerRequestDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String address,
        @NotBlank String city,
        @NotBlank @Pattern(regexp = "\\d{10}") String telephone) {
}
