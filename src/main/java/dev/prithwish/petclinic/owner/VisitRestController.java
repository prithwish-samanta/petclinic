package dev.prithwish.petclinic.owner;

import org.springframework.web.bind.annotation.*;
import dev.prithwish.petclinic.exception.ResourceNotFoundException;
import dev.prithwish.petclinic.system.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/visit")
@RequiredArgsConstructor
@Tag(name = "Visit Management", description = "APIs for managing visits")
public class VisitRestController {
    private final OwnerRepository ownerRepository;

    @Operation(summary = "Get visit details for a pet", description = "Fetch clinic visit details for a pet by ownerId and petId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet clinic visit details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Owner or pet details not found", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/owners/{ownerId}/pets/{petId}")
    public ResponseEntity<Map<String, Object>> findPetWithVisit(
            @Parameter(description = "Id of the owner", example = "1", required = true) @PathVariable int ownerId,
            @Parameter(description = "Id of the pet", example = "1", required = true) @PathVariable int petId) {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + ownerId
                + ". Please ensure the ID is correct " + "and the owner exists in the database."));

        Pet pet = owner.getPetById(petId);
        if (pet == null) {
            throw new ResourceNotFoundException(
                    "Pet with id " + petId + " not found for owner with id " + ownerId + ".");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("visits", pet.getVisits());
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Create a new visit for a pet", description = "Create a new clinic visit for a pet by ownerId and petId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet clinic visit created successfully"),
            @ApiResponse(responseCode = "404", description = "Owner details not found", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/owners/{ownerId}/pets/{petId}/new")
    public ResponseEntity<String> crateNewPetVisit(
            @Valid @RequestBody CreateOrUpdateVisitRequestDTO requestDTO,
            @Parameter(description = "Id of the owner", example = "1", required = true) @PathVariable int ownerId,
            @Parameter(description = "Id of the pet", example = "1", required = true) @PathVariable int petId) {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + ownerId
                + ". Please ensure the ID is correct " + "and the owner exists in the database."));
        Pet pet = owner.getPetById(petId);
        if (pet == null) {
            throw new ResourceNotFoundException(
                    "Pet with id " + petId + " not found for owner with id " + ownerId + ".");
        }
        Visit visit = new Visit();
        visit.setDate(requestDTO.date());
        visit.setDescription(requestDTO.description());
        pet.getVisits().add(visit);
        ownerRepository.save(owner);
        return new ResponseEntity<>("Pet visit created successfully", HttpStatus.CREATED);
    }
}
