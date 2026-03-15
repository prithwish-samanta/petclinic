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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
@Tag(name = "Owner Management", description = "APIs for managing vets")
public class OwnerRestController {
    private final OwnerRepository ownerRepository;

    @Operation(summary = "Get owner by id", description = "Fetch owner details by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Owner details retrieved successfully", content = @Content(schema = @Schema(implementation = Owner.class))),
            @ApiResponse(responseCode = "404", description = "Owner details not found", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/find/{ownerId}")
    public ResponseEntity<Owner> findOwner(@Parameter(description = "Id of the owner", example = "1") @PathVariable int ownerId) {
        Owner res = ownerRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + ownerId
                + ". Please ensure the ID is correct " + "and the owner exists in the database."));
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Search owners by lastname", description = "Search and fetch owners' details by lastname")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Owner search result retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/find/lastname/{lastName}")
    public ResponseEntity<Map<String, Object>> findOwnerByLastName(
            @Parameter(description = "Last name of the owner", example = "Coleman", required = true) @PathVariable String lastName,
            @Parameter(description = "Requested page number", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Requested page size", example = "5") @RequestParam(defaultValue = "5") int size) {
        Page<Owner> pageRes = ownerRepository.findByLastNameStartingWith(lastName, PageRequest.of(page - 1, size));

        Map<String, Object> res = new HashMap<>();
        res.put("currentPage", page);
        res.put("totalPages", pageRes.getTotalPages());
        res.put("totalItems", pageRes.getTotalElements());
        res.put("listOwners", pageRes.getContent());

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Create new owner", description = "Create a new owner")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Owner created successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/new")
    public ResponseEntity<Map<String, Integer>> createNewOwner(@Valid @RequestBody CreateOrUpdateOwnerRequestDTO requestDTO) {
        Owner owner = new Owner();
        mapOwnerAttributes(owner, requestDTO);
        Owner savedOwner = ownerRepository.save(owner);
        Map<String, Integer> res = new HashMap<>();
        res.put("created owner id", savedOwner.getId());
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @Operation(summary = "Update owner details", description = "Update owner details by ownerId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Owner details updated successfully"),
            @ApiResponse(responseCode = "404", description = "Owner details not found", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping("/{ownerId}/edit")
    public ResponseEntity<Owner> editExistingOwner(
            @Parameter(description = "Id of the owner", example = "1", required = true) @PathVariable int ownerId,
            @RequestBody CreateOrUpdateOwnerRequestDTO requestDTO) {
        Owner existingOwner = ownerRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + ownerId
                + ". Please ensure the ID is correct " + "and the owner exists in the database."));
        mapOwnerAttributes(existingOwner, requestDTO);
        Owner res = ownerRepository.save(existingOwner);
        return ResponseEntity.ok(res);
    }

    private void mapOwnerAttributes(Owner owner, CreateOrUpdateOwnerRequestDTO dto) {
        owner.setFirstName(dto.firstName());
        owner.setLastName(dto.lastName());
        owner.setAddress(dto.address());
        owner.setCity(dto.city());
        owner.setTelephone(dto.telephone());
    }
}
