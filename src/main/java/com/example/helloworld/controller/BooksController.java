package com.example.helloworld.controller;

import com.example.helloworld.common.Constants;
import com.example.helloworld.model.Book;
import com.example.helloworld.model.CustomUserDetails;
import com.example.helloworld.repository.BookRepository;
import com.example.helloworld.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(value = "/books")
public class BooksController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BookRepository bookRepository;
    
    @GetMapping("/addBook")
    public String showBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "/books/form";
    }
    @PostMapping("/book_register")
    public String bookRegister(Book book) {
        book.setStatus(Constants.BOOK_STATUS_AVAILABLE);
        bookRepository.save(book);
        return "/books/addSuccessful";
    }
    
    @GetMapping("/listBooks")
    public String showBooksPage(Model model) {
        List<Book> AllBooks = bookService.getAll();
        model.addAttribute("AllBooks", AllBooks);
        return "/books/listBooks";
    }
    
    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable(name = "id") Long id, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = ((CustomUserDetails)principal).getRole();
        if (Objects.equals(role, Constants.ROLE_ADMIN)) {
            bookRepository.deleteById(id);
            return "/home";
        }
        else {
            return "/accessDenied";
        }
    }
}
