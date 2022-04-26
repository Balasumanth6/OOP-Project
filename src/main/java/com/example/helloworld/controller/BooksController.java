package com.example.helloworld.controller;

import com.example.helloworld.common.Constants;
import com.example.helloworld.model.*;
import com.example.helloworld.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

@Controller
@RequestMapping(value = "/books")
public class BooksController {
    
    @Autowired
    private BookRepository bookRepository;
    
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
        bookRepository.save(book);
        return "/books/addSuccessful";
    }
    
    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable(name = "id") Long id) {
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
    
    @GetMapping("/borrowRequestPage/{id}")
    public String borrowRequestPage(@PathVariable(name = "id") Long id, Model model){
        model.addAttribute("numberOfDays", new NumberOfDays());
        model.addAttribute("bookId", id);
        return "/books/borrowRequestPage";
    }
    
    @PostMapping("/borrowRequest/{id}")
    public String borrowBook(@PathVariable(name = "id") Long id, NumberOfDays numberOfDays) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = ((CustomUserDetails)principal).getId();
        String currentUserName = ((CustomUserDetails)principal).getFullName();
        Book bookForRequest = bookRepository.getById(id);
        bookForRequest.setRequestedByUserId(currentUserId);
        bookForRequest.setRequestedByUserName(currentUserName);
        bookForRequest.setPendingStatus(Constants.BOOK_STATUS_PENDING_TRUE);
        bookForRequest.setRequestedNumberOfDays(numberOfDays.getNumberOfDaysRequired());
        bookRepository.save(bookForRequest);
        return "redirect:/home";
    }
    
    @GetMapping("/cancelBorrowRequest")
    public String cancelBorrowRequest() {
        return "redirect:/home";
    }
    
    @PostMapping("/cancelRequest/{id}")
    public String cancelRequest(@PathVariable(name = "id") Long id) {
        Book bookToCancelRequest = bookRepository.getById(id);
        bookToCancelRequest.setRequestedByUserId(null);
        bookToCancelRequest.setRequestedByUserName(null);
        bookToCancelRequest.setPendingStatus(Constants.BOOK_STATUS_PENDING_FALSE);
        bookRepository.save(bookToCancelRequest);
        return "redirect:/home";
    }
    
    @PostMapping("/declineBorrowRequest/{id}")
    public String declineBorrowRequest(@PathVariable(name = "id") Long id) {
        Book bookToDeclineBorrowRequest = bookRepository.getById(id);
        bookToDeclineBorrowRequest.setRequestedByUserId(null);
        bookToDeclineBorrowRequest.setRequestedByUserName(null);
        bookToDeclineBorrowRequest.setPendingStatus(Constants.BOOK_STATUS_PENDING_FALSE);
        bookRepository.save(bookToDeclineBorrowRequest);
        return "redirect:/home";
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
        bookToAcceptRequest.setDueDate(new Date(timeAndPlace.getDateOfCollection().getTime() + (bookToAcceptRequest.getRequestedNumberOfDays())*24*60*60*1000));
        bookToAcceptRequest.setIssuedDate(timeAndPlace.getDateOfCollection());
        bookToAcceptRequest.setPlaceOfCollection(timeAndPlace.getPlaceOfCollection());
        bookRepository.save(bookToAcceptRequest);
        return "redirect:/home";
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
        return "redirect:/home";
    }

    @PostMapping("extendLoanRequest/{id}")
    public String extendLoad(@PathVariable(name = "id") Long id) {
        Book book = bookRepository.getById(id);
        book.setExtensionRequest(true);
        bookRepository.save(book);
        return "redirect:/home";
    }
    
    @PostMapping("/acceptLoanRequest/{id}")
    public String acceptLoanRequest(@PathVariable(name = "id") Long id) {
        Book book = bookRepository.getById(id);
        book.setDueDate(new Date(book.getDueDate().getTime()+15*24*60*60*1000));
        book.setLoanAccepted(true);
        book.setUsedLoanRequest(true);
        bookRepository.save(book);
        return "redirect:/home";
    }
    
    @PostMapping("/declineLoanRequest/{id}")
    public String declineLoanRequest(@PathVariable(name = "id") Long id) {
        Book book = bookRepository.getById(id);
        book.setExtensionRequest(false);
        bookRepository.save(book);
        return "redirect:/home";
    }
}
