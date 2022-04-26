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

        User user3 = userRepository.findByEmail("swayam375@gmail.com");
        if(user3 == null) {
            user3 = new User();
            user3.setFirstName("Swayam");
            user3.setLastName("Bhargava");
            user3.setEmail("swayam375@gmail.com");
            user3.setPassword(passwordEncoder.encode("Swayam"));
            user3.setRole(Constants.MEMBER_STUDENT);
            user3.setAddress("1602");
            user3.setPhoneNumber("7073892224");
            userRepository.save(user3);
        }
        else {
            System.out.println("Admin user already present");
        }
        User user4 = userRepository.findByEmail("Bhumika@gmail.com");
        if(user4 == null) {
            user4 = new User();
            user4.setFirstName("Bhumika");
            user4.setLastName("Srivastava");
            user4.setEmail("Bhumika@gmail.com");
            user4.setPassword(passwordEncoder.encode("Bhumika"));
            user4.setRole(Constants.MEMBER_STUDENT);
            user4.setAddress("1601");
            user4.setPhoneNumber("85223344");
            userRepository.save(user4);
        }
        else {
            System.out.println("Admin user already present");
        }
    }
}
