package com.example.helloworld.controller;

import com.example.helloworld.model.User;
import com.example.helloworld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AdminController {
    
    @Autowired
    UserRepository userRepository;
    
    @GetMapping("/users")
    public String ListUsers(Model model) {
        List<User> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "/admin/users";
    }
    
    @GetMapping("/generateReportPage")
    public String generateReportPage(Model model) {
        List<User> listusers = userRepository.findAll();
        model.addAttribute("listUsers", listusers);
        return "/admin/generateReport";
    }
    
    @GetMapping("/generateReport/{id}")
    public String generateReport(@PathVariable(name = "id") Long id) {
        return "/admin/home";
    }
    
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id) {
        userRepository.deleteById(id);
        return "/admin/home";
    }
}
