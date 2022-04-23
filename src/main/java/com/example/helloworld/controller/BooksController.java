package com.example.helloworld.controller;

import com.example.helloworld.common.Constants;
import com.example.helloworld.model.Book;
import com.example.helloworld.model.CustomUserDetails;
import com.example.helloworld.model.User;
import com.example.helloworld.repository.BookRepository;
import com.example.helloworld.repository.UserRepository;
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
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/addBook")
    public String showBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "/books/form";
    }
    @PostMapping("/book_register")
    public String bookRegister(Book book) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((CustomUserDetails)principal).getId();
        String userName = ((CustomUserDetails)principal).getFullName();
        book.setUserId(userId);
        book.setUserName(userName);
        book.setAvailableStatus(Constants.BOOK_STATUS_AVAILABLE);
        book.setPendingStatus(Constants.BOOK_STATUS_PENDING_FALSE);
        bookRepository.save(book);
        return "/books/addSuccessful";
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
    
    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable(name = "id") Long id, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = ((CustomUserDetails)principal).getRole();
        Long userId = ((CustomUserDetails)principal).getId();
        Book book = bookRepository.getById(id);
        Long bookUserId = book.getUserId();
        if (Objects.equals(role, Constants.ROLE_ADMIN) || (userId == bookUserId)) {
            bookRepository.deleteById(id);
            return "/home";
        }
        else {
            return "/accessDenied";
        }
    }
    
    @PostMapping("/borrowRequest/{id}")
    public String borrowBook(@PathVariable(name = "id") Long id, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = ((CustomUserDetails)principal).getId();
        String currentUserName = ((CustomUserDetails)principal).getFullName();
        Book bookForRequest = bookRepository.getById(id);
        bookForRequest.setRequestedByUserId(currentUserId);
        bookForRequest.setRequestedByUserName(currentUserName);
        bookForRequest.setPendingStatus(Constants.BOOK_STATUS_PENDING_TRUE);
        bookRepository.save(bookForRequest);
        return "/home";
    }
    
    @PostMapping("/cancelRequest/{id}")
    public String cancelRequest(@PathVariable(name = "id") Long id, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Book bookToCancelRequest = bookRepository.getById(id);
        bookToCancelRequest.setRequestedByUserId(null);
        bookToCancelRequest.setRequestedByUserName(null);
        bookToCancelRequest.setPendingStatus(Constants.BOOK_STATUS_PENDING_FALSE);
        bookRepository.save(bookToCancelRequest);
        return "/home";
    }
    
    @PostMapping("/declineBorrowRequest/{id}")
    public String declineBorrowRequest(@PathVariable(name = "id") Long id, Model model) {
        Book bookToDeclineBorrowRequest = bookRepository.getById(id);
        bookToDeclineBorrowRequest.setRequestedByUserId(null);
        bookToDeclineBorrowRequest.setRequestedByUserName(null);
        bookToDeclineBorrowRequest.setPendingStatus(Constants.BOOK_STATUS_PENDING_FALSE);
        bookRepository.save(bookToDeclineBorrowRequest);
        return "/home";
    }
    
    @PostMapping("/acceptBorrowRequest/{id}")
    public String acceptBorrowRequest(@PathVariable(name = "id") Long id) {
        Book bookToAcceptRequest = bookRepository.getById(id);
        bookToAcceptRequest.setIssuedToUserId(bookToAcceptRequest.getRequestedByUserId());
        bookToAcceptRequest.setIssuedToUserName(bookToAcceptRequest.getRequestedByUserName());
        bookToAcceptRequest.setRequestedByUserId(null);
        bookToAcceptRequest.setRequestedByUserName(null);
        bookToAcceptRequest.setPendingStatus(Constants.BOOK_STATUS_PENDING_FALSE);
        bookToAcceptRequest.setAvailableStatus(Constants.BOOK_STATUS_ISSUED);
        bookRepository.save(bookToAcceptRequest);
        return "/home";
    }
    
    @PostMapping("/returnBook/{id}")
    public String returnBook(@PathVariable(name = "id") Long id) {
        Book bookToBeReturned = bookRepository.getById(id);
        bookToBeReturned.setIssuedToUserId(null);
        bookToBeReturned.setIssuedToUserName(null);
        bookToBeReturned.setAvailableStatus(Constants.BOOK_STATUS_AVAILABLE);
        bookRepository.save(bookToBeReturned);
        return "/home";
    }
}
