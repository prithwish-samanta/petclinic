package dev.prithwish.petclinic.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class CrashWebController {
    @GetMapping("/oups")
    public String triggerException() {
        throw new RuntimeException(
                "Expected: controller used to showcase what " + "happens when an exception is thrown");
    }
}
