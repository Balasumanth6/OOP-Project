package com.example.helloworld.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "books")
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Column(nullable = false, name = "title")
    private String title;
    
    @Column(nullable = false, name = "author")
    private String author;
    
    @Column(nullable = false, name = "issuedStatus")
    private boolean availableStatus;
    
    @Column(nullable = false, name = "pendingStatus")
    private boolean pendingStatus;

    @Column(nullable = false, name = "userId")
    private Long userId;

    @Column(nullable = false, name = "userName")
    private String userName;
    
    @Column(nullable = true, name = "issuedToUserId")
    private Long issuedToUserId;
    
    @Column(nullable = true, name = "issuedToUserName")
    private String issuedToUserName;
    
    @Column(nullable = true, name = "requestedByUserId")
    private Long requestedByUserId;
    
    @Column(nullable = true, name = "requestedByUserName")
    private String requestedByUserName;

    @Column(nullable = false, name = "createdDate")
    private LocalDateTime createdDate;
    
    @Column(nullable = true, name = "dueDate")
    private LocalDateTime dueDate;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isAvailableStatus() {
        return availableStatus;
    }
    public void setAvailableStatus(boolean availableStatus) {
        this.availableStatus = availableStatus;
    }
    public boolean isPendingStatus() {
        return pendingStatus;
    }
    public void setPendingStatus(boolean pendingStatus) {
        this.pendingStatus = pendingStatus;
    }
    public String getIssuedToUserName() {
        return issuedToUserName;
    }
    public void setIssuedToUserName(String issuedToUserName) {
        this.issuedToUserName = issuedToUserName;
    }
    public Long getRequestedByUserId() {
        return requestedByUserId;
    }
    public void setRequestedByUserId(Long requestedByUserId) {
        this.requestedByUserId = requestedByUserId;
    }
    public String getRequestedByUserName() {
        return requestedByUserName;
    }
    public void setRequestedByUserName(String requestedByUserName) {
        this.requestedByUserName = requestedByUserName;
    }
    public Long getIssuedToUserId() {
        return issuedToUserId;
    }
    public void setIssuedToUserId(Long issuedToUserId) {
        this.issuedToUserId = issuedToUserId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}