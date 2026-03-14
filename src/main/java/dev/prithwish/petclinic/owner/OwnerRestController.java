package dev.prithwish.petclinic.owner;

import org.springframework.web.bind.annotation.*;
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
public class OwnerRestController {
    private final OwnerRepository ownerRepository;

    @GetMapping("/find/{ownerId}")
    public ResponseEntity<Owner> findOwner(@PathVariable int ownerId) {
        Owner res = ownerRepository.findById(ownerId).orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId
                + ". Please ensure the ID is correct " + "and the owner exists in the database."));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/find/lastname/{lastName}")
    public ResponseEntity<Map<String, Object>> findOwnerByLastName(
            @PathVariable String lastName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Owner> pageRes = ownerRepository.findByLastNameStartingWith(lastName, PageRequest.of(page - 1, size));

        Map<String, Object> res = new HashMap<>();
        res.put("currentPage", page);
        res.put("totalPages", pageRes.getTotalPages());
        res.put("totalItems", pageRes.getTotalElements());
        res.put("listOwners", pageRes.getContent());

        return ResponseEntity.ok(res);
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, Integer>> createNewOwner(@RequestBody CreateOrUpdateOwnerRequestDTO requestDTO) {
        Owner owner = new Owner();
        mapOwnerAttributes(owner, requestDTO);
        Owner savedOwner = ownerRepository.save(owner);
        Map<String, Integer> res = new HashMap<>();
        res.put("created owner id", savedOwner.getId());
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/{ownerId}/edit")
    public ResponseEntity<Owner> editExistingOwner(@PathVariable int ownerId, @RequestBody CreateOrUpdateOwnerRequestDTO requestDTO) {
        Owner existingOwner = ownerRepository.findById(ownerId).orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId
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
