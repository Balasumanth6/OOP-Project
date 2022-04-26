package com.example.helloworld.controller;

import com.example.helloworld.common.Constants;
import com.example.helloworld.model.Book;
import com.example.helloworld.model.CustomUserDetails;
import com.example.helloworld.model.TimeAndPlace;
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

import java.time.LocalDateTime;
import java.util.Date;
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
    
    @GetMapping("/firstPage")
    public String firstPage() {
        return "/";
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
        book.setCreatedDate(new Date());
        book.setLoanAccepted(false);
        book.setExtensionRequest(false);
        book.setUsedLoanRequest(false);
        book.setRequestedNumberOfDays(1L);
        bookRepository.save(book);
        return "/books/addSuccessful";
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
    
    @GetMapping("/books/borrowRequestPage/{id}")
    public String borrowRequestPage(@PathVariable(name = "id") Long id){
        return "/books/borrowRequestPage";
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
    
    @GetMapping("/acceptBorrowRequestPage/{id}")
    public String acceptBorrowRequestPage(@PathVariable(value = "id") Long id, Model model){
        Book book = bookRepository.getById(id);
        String requestedByUserName = book.getRequestedByUserName();
        Long requestedNumberOfDays = book.getRequestedNumberOfDays();
        TimeAndPlace timeAndPlace = new TimeAndPlace();
        model.addAttribute("timeAndPlace", timeAndPlace);
        model.addAttribute("requestedByUserName", requestedByUserName);
        model.addAttribute("requestedNumberOfDays", requestedNumberOfDays);
        model.addAttribute("bookId", id);
        return "/books/acceptBorrowRequestPage";
    }
    
    @PostMapping("/acceptBorrowRequest/{id}")
    public String acceptBorrowRequest(@PathVariable(name = "id") Long id, TimeAndPlace timeAndPlace) {
        Book bookToAcceptRequest = bookRepository.getById(id);
        bookToAcceptRequest.setIssuedToUserId(bookToAcceptRequest.getRequestedByUserId());
        bookToAcceptRequest.setIssuedToUserName(bookToAcceptRequest.getRequestedByUserName());
        bookToAcceptRequest.setRequestedByUserId(null);
        bookToAcceptRequest.setRequestedByUserName(null);
        bookToAcceptRequest.setPendingStatus(Constants.BOOK_STATUS_PENDING_FALSE);
        bookToAcceptRequest.setAvailableStatus(Constants.BOOK_STATUS_ISSUED);
        bookToAcceptRequest.setDateOfCollection(timeAndPlace.getDateOfCollection());
        bookToAcceptRequest.setDueDate(new Date(timeAndPlace.getDateOfCollection().getTime() + 14*24*60*60*1000));
        bookToAcceptRequest.setIssuedDate(timeAndPlace.getDateOfCollection());
        bookToAcceptRequest.setPlaceOfCollection(timeAndPlace.getPlaceOfCollection());
        bookRepository.save(bookToAcceptRequest);
        return "/home";
    }
    
    @PostMapping("/returnBook/{id}")
    public String returnBook(@PathVariable(name = "id") Long id) {
        Book bookToBeReturned = bookRepository.getById(id);
        bookToBeReturned.setIssuedToUserId(null);
        bookToBeReturned.setIssuedToUserName(null);
        bookToBeReturned.setAvailableStatus(Constants.BOOK_STATUS_AVAILABLE);
        bookToBeReturned.setDueDate(null);
        bookToBeReturned.setIssuedDate(null);
        bookToBeReturned.setExtensionRequest(false);
        bookToBeReturned.setUsedLoanRequest(false);
        bookRepository.save(bookToBeReturned);
        return "/home";
    }

    @PostMapping("extendLoanRequest/{id}")
    public String extendLoad(@PathVariable(name = "id") Long id) {
        Book book = bookRepository.getById(id);
        book.setExtensionRequest(true);
        bookRepository.save(book);
        return "/home";
    }
    
    @PostMapping("/acceptLoanRequest/{id}")
    public String acceptLoanRequest(@PathVariable(name = "id") Long id) {
        Book book = bookRepository.getById(id);
        book.setDueDate(new Date(book.getDueDate().getTime()+15*24*60*60*1000));
        book.setLoanAccepted(true);
        book.setUsedLoanRequest(true);
        bookRepository.save(book);
        return "/home";
    }
    
    @PostMapping("/declineLoanRequest/{id}")
    public String declineLoanRequest(@PathVariable(name = "id") Long id) {
        Book book = bookRepository.getById(id);
        book.setExtensionRequest(false);
        bookRepository.save(book);
        return "/home";
    }
}
