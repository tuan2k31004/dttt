package org.example.dttt2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class AppController {

    @GetMapping
    public String app(Model model) {
        model.addAttribute("matchingNetwork", new MatchingNetwork());
        return "app";
    }

    @PostMapping("calculate")
    public String calculate(Model model, @ModelAttribute MatchingNetwork matchingNetwork) {
        model.addAttribute("result", matchingNetwork.calculate());
        return "app";
    }
}
