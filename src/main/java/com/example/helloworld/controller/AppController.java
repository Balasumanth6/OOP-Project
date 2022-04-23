package com.example.helloworld.controller;

import com.example.helloworld.common.Constants;
import com.example.helloworld.model.CustomUserDetails;
import com.example.helloworld.model.User;
import com.example.helloworld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

@Controller
public class AppController {
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }
    
//    @GetMapping("/home")
//    public String viewPage() { return "/home"; }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "loginAndSignUp/signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(Constants.MEMBER_STUDENT);
        userRepository.save(user);

        return "loginAndSignUp/register_success";
    }
    
    @GetMapping("/admin")
    public String AdminView() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = ((CustomUserDetails)principal).getRole();
        if (Objects.equals(role, Constants.ROLE_ADMIN)) {
            return "/admin/home";
        }
        else {
            return "accessDenied";
        }
    }
}