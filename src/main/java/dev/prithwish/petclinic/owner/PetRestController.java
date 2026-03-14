package dev.prithwish.petclinic.owner;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetRestController {
    private final OwnerRepository ownerRepository;
    private final PetTypeRepository petTypeRepository;

    @GetMapping("/{petId}/owners/{ownerId}")
    public ResponseEntity<Map<String, Object>> findPet(@PathVariable int ownerId, @PathVariable int petId) {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId
                + ". Please ensure the ID is correct " + "and the owner exists in the database."));
        Pet pet = owner.getPetById(petId);
        Map<String, Object> res = new HashMap<>();
        res.put("pets", pet);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/types")
    public ResponseEntity<Map<String, Object>> findAvailablePetTypes() {
        Map<String, Object> res = new HashMap<>();
        res.put("allPetTypes", petTypeRepository.findAll());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/owners/{ownerId}/new")
    public ResponseEntity<String> createNewPet(@Valid @RequestBody CreateOrUpdatePetRequestDTO requestDTO, @PathVariable int ownerId) {
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

    @PutMapping("{petId}/owners/{ownerId}/edit")
    public ResponseEntity<String> editExistingPetDetails(@Valid @RequestBody CreateOrUpdatePetRequestDTO requestDTO, @PathVariable int ownerId, @PathVariable int petId) {
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
