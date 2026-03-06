package dev.prithwish.petclinic.vet;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ui")
@RequiredArgsConstructor
public class VetWebController {
    private final VetRepository vetRepository;

    @GetMapping("/vets.html")
    public String showVets(@RequestParam(defaultValue = "1") int page, Model model) {
        Page<Vet> pageRes = vetRepository.findAll(PageRequest.of(page - 1, 5));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageRes.getTotalPages());
        model.addAttribute("totalItems", pageRes.getTotalElements());
        model.addAttribute("listVets", pageRes.getContent());
        return "vets/vetList";
    }
}
