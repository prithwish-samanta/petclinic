package dev.prithwish.petclinic.system;

import java.time.Instant;

public record ErrorResponseDTO(Instant timestamp, int status, String message) {
}
