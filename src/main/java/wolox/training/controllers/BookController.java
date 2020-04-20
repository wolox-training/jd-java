package wolox.training.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Entry point to books routes
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@Controller
public class BookController {

    /**
     * Shows a greeting message
     *
     * @param name  request param to show on greeting message
     * @param model model object to add response data for view
     * @return show greeting view html
     */
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", defaultValue = "world") String name,
        Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

}
