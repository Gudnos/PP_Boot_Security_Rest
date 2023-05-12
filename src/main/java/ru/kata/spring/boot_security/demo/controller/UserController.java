package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.services.MyUserDetailsService;

import java.security.Principal;


@Controller
public class UserController {
    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public UserController(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @GetMapping(value = "/user")
    public String userAccount(Model model, Principal principal) {
        model.addAttribute("user", myUserDetailsService.findByUsername(principal.getName()));
        return "user";
    }
}


