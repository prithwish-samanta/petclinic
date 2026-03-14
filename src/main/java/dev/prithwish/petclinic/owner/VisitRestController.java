package dev.prithwish.petclinic.owner;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/visit")
@RequiredArgsConstructor
public class VisitRestController {
    private final OwnerRepository ownerRepository;

    @GetMapping("/owners/{ownerId}/pets/{petId}")
    public ResponseEntity<Map<String, Object>> findPetWithVisit(@PathVariable int ownerId, @PathVariable int petId) {
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

    @PostMapping("/owners/{ownerId}/pets/{petId}/new")
    public ResponseEntity<String> crateNewPetVisit(@Valid @RequestBody CreateOrUpdateVisitRequestDTO requestDTO, @PathVariable int ownerId, @PathVariable int petId) {
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
