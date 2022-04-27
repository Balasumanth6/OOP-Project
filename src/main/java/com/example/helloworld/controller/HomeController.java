package com.example.helloworld.controller;

import com.example.helloworld.common.Constants;
import com.example.helloworld.model.Book;
import com.example.helloworld.model.CustomUserDetails;
import com.example.helloworld.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Objects;

@Controller
public class HomeController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping(value = {"/home"})
    public String homePage() {
        return "/home";
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

    @GetMapping("/addBook")
    public String showBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "/books/form";
    }
    
    @GetMapping("/walletStatusPage")
    public String walletStatusPage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentAmount = ((CustomUserDetails)principal).getWalletStatus();
        model.addAttribute("currentAmount", currentAmount);
        return "/walletStatusPage";
    }

    @GetMapping("/listBooks")
    public String showBooksPage(Model model) {
        List<Book> AllBooks = bookService.getAll();
        model.addAttribute("AllBooks", AllBooks);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = ((CustomUserDetails)principal).getId();
        model.addAttribute("currentUserId", currentUserId);
        return "/books/listBooks";
    }

    @GetMapping("/changePasswordPage")
    public String changePasswordPage() {
        return "/loginAndSignUp/changePasswordPage";
    }
    
    @GetMapping("/deleteAccountPage")
    public String deleteAccount() {
        return "/DeleteAccount/deleteAccountPage";
    }
    
    @GetMapping("/checkoutPage") 
    public String checkedOutPage(Model model) {
        List<Book> AllBooks = bookService.getAll();
        model.addAttribute("AllBooks", AllBooks);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = ((CustomUserDetails)principal).getId();
        model.addAttribute("currentUserId", currentUserId);
        return "/books/checkoutPage";
    }
    
    @GetMapping("/myBooksPage")
    public String myBooksPage(Model model) {
        List<Book> AllBooks = bookService.getAll();
        model.addAttribute("AllBooks", AllBooks);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = ((CustomUserDetails)principal).getId();
        model.addAttribute("currentUserId", currentUserId);
        return "/books/myBooksPage";
    }
}
