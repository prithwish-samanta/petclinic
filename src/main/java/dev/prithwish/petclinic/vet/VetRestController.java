package dev.prithwish.petclinic.vet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vets")
@Tag(name = "Vet Management", description = "APIs for managing vets")
public class VetRestController {
    private final VetRepository vetRepository;

    @Operation(summary = "Get veterinarians", description = "Fetch all the available vets in the clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vets retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> showVets(
            @Parameter(description = "Requested page number", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Requested page size", example = "5") @RequestParam(defaultValue = "5") int size,
            @Parameter(description = "Page element sort key", example = "id") @RequestParam(defaultValue = "id") String sortBy
    ) {
        Map<String, Object> res = new HashMap<>();

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortBy));
        Page<Vet> pageRes = vetRepository.findAll(pageable);

        res.put("currentPage", page);
        res.put("totalPages", pageRes.getTotalPages());
        res.put("totalItems", pageRes.getTotalElements());
        res.put("listVets", pageRes.getContent());

        return ResponseEntity.ok(res);
    }
}
