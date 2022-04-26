package com.example.helloworld.controller;

import com.example.helloworld.model.Book;
import com.example.helloworld.model.CustomUserDetails;
import com.example.helloworld.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping(value = {"/home"})
    public String homePage() {
        return "/home";
    }

    @GetMapping("/addBook")
    public String showBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "/books/form";
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
