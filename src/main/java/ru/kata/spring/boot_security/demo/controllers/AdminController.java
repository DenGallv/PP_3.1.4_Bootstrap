package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getAllUsers(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("userInfo", user);
        model.addAttribute("newUser", new User());
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "admin-page";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("newUser") User user,
                           @RequestParam(value = "userRole") String[] roles) {
        userService.saveUser(user, roles);
        return "redirect:/admin/";
    }

    @PatchMapping("/{id}/updateUser")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "userRole") String[] roles) {
        userService.updateUser(user, roles);
        return "redirect:/admin/";
    }

    @DeleteMapping("/{id}/deleteUser")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/";
    }
}
