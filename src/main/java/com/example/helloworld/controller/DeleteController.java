package com.example.helloworld.controller;

import com.example.helloworld.model.CustomUserDetails;
import com.example.helloworld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DeleteController {
    
    @Autowired
    UserRepository userRepository;
    
    @GetMapping("/deleteAccount")
    public String deleteAccount() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((CustomUserDetails)principal).getId();
        userRepository.deleteById(userId);
        
        return "/DeleteAccount/AccountDeleteSuccessfulPage";
    }
    
    @GetMapping("/deleteAccountCancelRequest")
    public String cancelDeleteAccountRequest() {
        return "redirect:/home";
    }
}
