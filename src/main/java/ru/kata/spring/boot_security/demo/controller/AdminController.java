package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.util.UserValidator;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.MyUserDetailsService;
import ru.kata.spring.boot_security.demo.services.RoleService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("admin")
public class AdminController {

    private final MyUserDetailsService myUserDetailsService;
    private final RoleService roleService;

    private final UserValidator userValidator;

    @Autowired
    public AdminController(MyUserDetailsService myUserDetailsService, RoleService roleService, UserValidator userValidator) {
        this.myUserDetailsService = myUserDetailsService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping
    public String users(Model model) {
        model.addAttribute("users", myUserDetailsService.findAll());
        return "all-users";
    }

    @GetMapping("create")
    public String createUserForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roleList", roleService.listRoles());
        return "create";
    }

    @PostMapping("create")
    public String createUser(@ModelAttribute("user") User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "redirect:/admin";
        }
        List<String> listS = user.getRoles().stream().map(Role::getRole).collect(Collectors.toList());
        Set<Role> listR = roleService.listByRole(listS);
        user.setRoles(listR);
        myUserDetailsService.create(user);
        return "redirect:/admin";
    }

    @GetMapping("update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = myUserDetailsService.findOne(id);
        model.addAttribute("user", user);
        model.addAttribute("roleList", roleService.listRoles());
        return "update";
    }

    @PutMapping("update")
    public String updateUser(Long id, User user) {
        List<String> listS = user.getRoles().stream().map(Role::getRole).collect(Collectors.toList());
        Set<Role> listR = roleService.listByRole(listS);
        user.setRoles(listR);
        myUserDetailsService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        myUserDetailsService.delete(id);
        return "redirect:/admin";
    }
}
