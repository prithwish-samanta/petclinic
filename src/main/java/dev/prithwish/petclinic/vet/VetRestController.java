package dev.prithwish.petclinic.vet;

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
public class VetRestController {
    private final VetRepository vetRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> showVets(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy
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
