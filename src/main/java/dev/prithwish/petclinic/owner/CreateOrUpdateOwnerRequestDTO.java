package dev.prithwish.petclinic.owner;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateOrUpdateOwnerRequestDTO(
        @Schema(description = "First name of the owner", example = "George") @NotBlank String firstName,
        @Schema(description = "Last name of the owner", example = "Franklin") @NotBlank String lastName,
        @Schema(description = "Address details of the owner", example = "110 W. Liberty St.") @NotBlank String address,
        @Schema(description = "City of the owner", example = "Madison") @NotBlank String city,
        @Schema(description = "Telephone number of the owner", example = "6085551023") @NotBlank @Pattern(regexp = "\\d{10}") String telephone) {
}
