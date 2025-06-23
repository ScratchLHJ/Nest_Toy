package com.nest.adminsystem.domain.mainpage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    @GetMapping("/")
    public String showDashboard(Model model) {
        model.addAttribute("title", "대시보드");
        model.addAttribute("contentFragment", "main/index");
        return "layout";  // templates/layout.html
    }
}
