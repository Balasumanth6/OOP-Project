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
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setRole(Constants.ROLE_ADMIN);
            user.setAddress("Bits Hyderabad");
            user.setPhoneNumber("9381578257");
            userRepository.save(user);
        }
        else {
            System.out.println("Admin user already present");
        }
        
        User user2 = userRepository.findByEmail("bala@gmail.com");
        if(user2 == null) {
            user2 = new User();
            user2.setFirstName("Sumanth");
            user2.setLastName("B");
            user2.setEmail("bala@gmail.com");
            user2.setPassword(passwordEncoder.encode("sumanth6"));
            user2.setRole(Constants.MEMBER_STUDENT);
            user2.setAddress("119");
            user2.setPhoneNumber("93");
            userRepository.save(user2);
        }
        else {
            System.out.println("Admin user already present");
        }
    }
}
