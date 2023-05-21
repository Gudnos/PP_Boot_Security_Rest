package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;

import java.security.Principal;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    private final UserValidator userValidator;


    @Autowired
    public AdminController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping
    public String users(Model model, Principal principal) {
        model.addAttribute("roleList", roleService.listRoles());
        model.addAttribute("formUser", new User());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        return "all-users_bootstrap";
    }

    @GetMapping("create")
    public String createUserForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roleList", roleService.listRoles());
        return "all-users_bootstrap";
    }

    @PostMapping("create")
    public String createUser(@ModelAttribute("user") User user, @RequestParam("authorities") List<String> values,
                             BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "redirect:/admin";
        }
        Set<Role> roleSet = userService.getSetOfRoles(values);
        user.setRoles(roleSet);
        userService.create(user);
        return "redirect:/admin";
    }

    @GetMapping("update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User updateUser = userService.findOne(id);
        model.addAttribute("updateUser", updateUser);
        model.addAttribute("roleList", roleService.listRoles());
        return "all-users_bootstrap";
    }

    @PutMapping("update")
    public String updateUser(Long id, User user, @RequestParam("authorities") List<String> values) {
        Set<Role> roleSet = userService.getSetOfRoles(values);
        user.setRoles(roleSet);
        userService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
