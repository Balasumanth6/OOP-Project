package com.example.helloworld;

import com.example.helloworld.common.Constants;
import com.example.helloworld.model.User;
import com.example.helloworld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component  
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("In Application StartUp");
        User user = userRepository.findByEmail("admin@gmail.com");
        if(user == null) {
            user = new User();
            user.setFirstName("Admin");
            user.setLastName("BPHC");
            user.setEmail("admin@gmail.com");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole(Constants.ROLE_ADMIN);
            userRepository.save(user);
        }
        else {
            System.out.println("Admin user already present");
        }
    }
}
