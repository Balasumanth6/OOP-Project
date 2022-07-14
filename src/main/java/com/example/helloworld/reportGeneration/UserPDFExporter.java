package com.example.helloworld.reportGeneration;

import com.example.helloworld.model.User;

import java.util.List;

public class UserPDFExporter {
    private List<User> listusers;
    
    public UserPDFExporter(List<User> listusers) {
        this.listusers = listusers;
    }
    
    private void writeTableHeader() {
        
    }
}
