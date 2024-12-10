package com.example.ezlawbackend.Auth.controller;

import com.example.ezlawbackend.Auth.repository.UserRepository;
import com.example.ezlawbackend.Auth.dto.SignUpRequest;
import com.example.ezlawbackend.Auth.model.User;
import com.example.ezlawbackend.Auth.service.UserService;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000",
        allowedHeaders = {"Content-Type", "Accept"},
        methods = {RequestMethod.POST,RequestMethod.GET,RequestMethod.DELETE,RequestMethod.PUT},
        allowCredentials = "true")
public class AuthController {
    @Autowired
    private UserService userService;

    private static final String USER_ID_KEY = "userId";
    private static final String EMAIL_KEY = "email";
    private static final String ROLE_KEY = "role";

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        User user = userService.register(
                request.getFirstname(),
                request.getLastname(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone(),
                request.getGender()
        );
        return ResponseEntity.ok("User registered successfully with ID: " + user.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request, HttpSession session) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            if (session.getAttribute(EMAIL_KEY) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Already logged in"));
            }
            User user = userService.login(email, password);

            // Set session attributes
            session.setAttribute(USER_ID_KEY, user.getId());
            session.setAttribute(EMAIL_KEY, user.getEmail());
            session.setAttribute(ROLE_KEY, user.getRole());

            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("firstname", user.getFirstname());
            response.put("lastname", user.getLastname());
            response.put("role", user.getRole());
            response.put("phone",user.getPhone());
            response.put("gender",user.getGender());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        String email = (String) session.getAttribute(EMAIL_KEY);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "No active session"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("userId", session.getAttribute(USER_ID_KEY));
        response.put("email", email);
        response.put("role", session.getAttribute(ROLE_KEY));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/upgrade")
    public String upgradeUser(@RequestBody Map<String, String > request){
        String email = request.get("email");
        userService.upgradeToMembership(email);
        return "User upgrade to membership";
    }

    @PostMapping("/edit-profile")
    public ResponseEntity<?> editprofile(@RequestBody Map<String,String>request,HttpSession session){
        try{
            String email = (String) session.getAttribute(EMAIL_KEY);
            if(email == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "No active session"));
            }
            User updateUser = userService.updateProfile(
                    email,
                    request.get("firstname"),
                    request.get("lastname"),
                    request.get("phone"),
                    request.get("gender")
            );

            Map<String, Object> response = new HashMap<>();
            response.put("id", updateUser.getId());
            response.put("firstname", updateUser.getFirstname());
            response.put("lastname", updateUser.getLastname());
            response.put("phone", updateUser.getPhone());
            response.put("gender",updateUser.getGender());

            return  ResponseEntity.ok(response);

        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> Userprofile(
            HttpSession session){
        String email = (String) session.getAttribute(EMAIL_KEY);
        if(email == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error","No active session"));
        }

        try{
            User user = userService.getUserProfile(email);
            return ResponseEntity.ok(user);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }

    }
}