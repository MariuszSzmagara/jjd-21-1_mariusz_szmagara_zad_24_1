package pl.javastart.homebudget.transaction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.javastart.homebudget.transaction.model.Transaction;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHome() {
        return "home";
    }
}
