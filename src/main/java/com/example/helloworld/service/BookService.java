package com.example.helloworld.service;

import com.example.helloworld.common.Constants;
import com.example.helloworld.model.Book;
import com.example.helloworld.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    
    public Long getTotalCount() { return bookRepository.count(); }
    
    public List<Book> getAll() { return bookRepository.findAll(); }
    public Book getByTag(Long id) { return bookRepository.findById(id).get(); }
    
    public Book addNew(Book book) {
        book.setStatus(Constants.BOOK_STATUS_AVAILABLE);
        return bookRepository.save(book);
    }
    
    public Book save(Book book) { return bookRepository.save(book); }
}
