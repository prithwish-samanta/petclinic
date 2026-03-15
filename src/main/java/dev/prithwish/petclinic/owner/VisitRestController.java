package dev.prithwish.petclinic.owner;

import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
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
            @ApiResponse(responseCode = "200", description = "Pet clinic visit details retrieved successfully")
    })
    @GetMapping("/owners/{ownerId}/pets/{petId}")
    public ResponseEntity<Map<String, Object>> findPetWithVisit(
            @Parameter(description = "Id of the owner", example = "1", required = true) @PathVariable int ownerId,
            @Parameter(description = "Id of the pet", example = "1", required = true) @PathVariable int petId) {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId
                + ". Please ensure the ID is correct " + "and the owner exists in the database."));

        Pet pet = owner.getPetById(petId);
        if (pet == null) {
            throw new IllegalArgumentException(
                    "Pet with id " + petId + " not found for owner with id " + ownerId + ".");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("visits", pet.getVisits());
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Create a new visit for a pet", description = "Create a new clinic visit for a pet by ownerId and petId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet clinic visit created successfully")
    })
    @PostMapping("/owners/{ownerId}/pets/{petId}/new")
    public ResponseEntity<String> crateNewPetVisit(
            @Valid @RequestBody CreateOrUpdateVisitRequestDTO requestDTO,
            @Parameter(description = "Id of the owner", example = "1", required = true) @PathVariable int ownerId,
            @Parameter(description = "Id of the pet", example = "1", required = true) @PathVariable int petId) {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId
                + ". Please ensure the ID is correct " + "and the owner exists in the database."));
        Pet pet = owner.getPetById(petId);
        if (pet == null) {
            throw new IllegalArgumentException(
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
