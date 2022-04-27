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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Controller
public class AppController {
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping(value =  {"", "/firstPage"})
    public String viewHomePage() {
        return "index";
    }
    
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
        user.setCreatedDate(new Date());
        userRepository.save(user);
        return "loginAndSignUp/register_success";
    }
    
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam(value = "oldPassword", required = true) String oldPassword, @RequestParam(value = "newPassword", required = true) String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String oldEncodedPassword = passwordEncoder.encode(oldPassword);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((CustomUserDetails)principal).getEmail();
        if (passwordEncoder.matches(oldPassword, oldEncodedPassword)) {
            User user = userRepository.findByEmail(email);
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return "/loginAndSignUp/passwordChangedSuccessfully";
        }
        else {
            return "/accessDenied";
        }
    }
}
