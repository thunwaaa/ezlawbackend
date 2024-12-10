package com.example.ezlawbackend.Auth.service;

import com.example.ezlawbackend.Auth.model.User;
import com.example.ezlawbackend.Auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User register(String firstname,String lastname,String email,String password,String phone,String gender) {
        if(userRepository.findByEmail(email) != null){
            throw new RuntimeException("Email already exists");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(firstname,lastname,email,hashedPassword,"user",phone,gender);
        return userRepository.save(user);
    }

    public User login(String email,String password){
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new RuntimeException("User not found");
        }
        if(!passwordEncoder.matches(password,user.getHashedPassword())){
            throw new RuntimeException("Invalid password");
        }
        return user;

    }

    public void upgradeToMembership(String email){
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new RuntimeException("User not found");
        }
        user.setRole("Membership");
        userRepository.save(user);
    }

    public User updateProfile(String email,String firstname,String lastname,String phone,String gender){
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new RuntimeException("User not fround");
        }

        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPhone(phone);
        user.setGender(gender);

        return userRepository.save(user);
    }

    public User getUserProfile(String email){
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new RuntimeException("user not found");
        }
        return user;
    }
}