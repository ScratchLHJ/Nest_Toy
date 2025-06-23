package com.nest.adminsystem.domain.mainpage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestLayoutController {

    @GetMapping("/test-layout")
    public String testLayout(Model model) {
        model.addAttribute("title", "레이아웃 테스트");
        model.addAttribute("content", "fragments/testcontent :: content");
        return "layout";
    }
}
