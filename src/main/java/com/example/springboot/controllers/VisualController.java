package com.example.springboot.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class VisualController {
    @GetMapping("/loginNew")
    public String loginNew(@CookieValue(value = "uid") String uid) {
        return "index";
    }

}
