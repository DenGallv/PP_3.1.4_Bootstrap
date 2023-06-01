package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String getAllUsers(Model model) {
        model.addAttribute("userList", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/info")
    public String addUser(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "user-info";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("newUser") User user,
                           @RequestParam(value = "userRole") String[] roles) {
        user.setRoles(userService.getRolesByNames(roles));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveOrUpdateUser(user);
        return "redirect:/admin/";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        model.addAttribute("userFromDB", userService.getUser(id));
        model.addAttribute("allRoles", userService.getAllRoles());
        return "user-edit";
    }

    @PatchMapping("/updateUser")
    public String updateUser(@ModelAttribute("userFromDB") User user,
                             @RequestParam(value = "userRole") String[] roles) {
        user.setRoles(userService.getRolesByNames(roles));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveOrUpdateUser(user);
        return "redirect:/admin/";
    }

    @DeleteMapping("/deleteUser")
    public String delete(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/";
    }
}
