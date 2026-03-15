package dev.prithwish.petclinic.owner;

import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@Tag(name = "Pet Management", description = "APIs for managing pets")
public class PetRestController {
    private final OwnerRepository ownerRepository;
    private final PetTypeRepository petTypeRepository;

    @Operation(summary = "Get pet by id", description = "Fetch pet details by ownerId and petId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet details retrieved successfully")
    })
    @GetMapping("/{petId}/owners/{ownerId}")
    public ResponseEntity<Map<String, Object>> findPet(
            @Parameter(description = "Id of the owner", example = "1", required = true) @PathVariable int ownerId,
            @Parameter(description = "Id of the pet", example = "1", required = true) @PathVariable int petId) {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId
                + ". Please ensure the ID is correct " + "and the owner exists in the database."));
        Pet pet = owner.getPetById(petId);
        Map<String, Object> res = new HashMap<>();
        res.put("pets", pet);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Get all pet types", description = "Fetch all available pet types for the clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet types retrieved successfully")
    })
    @GetMapping("/types")
    public ResponseEntity<Map<String, Object>> findAvailablePetTypes() {
        Map<String, Object> res = new HashMap<>();
        res.put("allPetTypes", petTypeRepository.findAll());
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Create a new pet", description = "Create new pet for the owner")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet created successfully")
    })
    @PostMapping("/owners/{ownerId}/new")
    public ResponseEntity<String> createNewPet(
            @Valid @RequestBody CreateOrUpdatePetRequestDTO requestDTO,
            @Parameter(description = "Id of the owner", example = "1", required = true) @PathVariable int ownerId) {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId
                + ". Please ensure the ID is correct " + "and the owner exists in the database."));
        PetType petType = petTypeRepository.findByName(requestDTO.petType().toLowerCase());
        if (petType == null) {
            throw new IllegalArgumentException("Pet with name " + requestDTO.petType() + " not found.");
        }
        Pet pet = new Pet();
        pet.setName(requestDTO.name());
        pet.setBirthDate(requestDTO.birthDate());
        pet.setType(petType);
        owner.getPets().add(pet);
        ownerRepository.save(owner);
        return new ResponseEntity<>("Pet created successfully", HttpStatus.CREATED);
    }

    @Operation(summary = "Update pet details", description = "Update pet details by ownerId and petId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet details updated successfully")
    })
    @PutMapping("{petId}/owners/{ownerId}/edit")
    public ResponseEntity<String> editExistingPetDetails(
            @Valid @RequestBody CreateOrUpdatePetRequestDTO requestDTO,
            @Parameter(description = "Id of the owner", example = "1", required = true) @PathVariable int ownerId,
            @Parameter(description = "Id of the pet", example = "1", required = true) @PathVariable int petId) {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId
                + ". Please ensure the ID is correct " + "and the owner exists in the database."));
        PetType petType = petTypeRepository.findByName(requestDTO.petType().toLowerCase());
        if (petType == null) {
            throw new IllegalArgumentException("Pet with name " + requestDTO.petType() + " not found.");
        }
        Pet pet = owner.getPetById(petId);
        if (pet == null) {
            throw new IllegalArgumentException(
                    "Pet with id " + petId + " not found for owner with id " + ownerId + ".");
        }
        pet.setName(requestDTO.name());
        pet.setBirthDate(requestDTO.birthDate());
        pet.setType(petType);
        ownerRepository.save(owner);
        return ResponseEntity.ok("Pet details updated successfully!");
    }
}
