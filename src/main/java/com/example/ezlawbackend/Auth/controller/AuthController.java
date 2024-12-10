package com.example.ezlawbackend.Auth.controller;

import com.example.ezlawbackend.Auth.model.UserMySQL;
import com.example.ezlawbackend.Auth.repository.UserMySQLRepository;
import com.example.ezlawbackend.Auth.dto.SignUpRequest;
import com.example.ezlawbackend.Auth.model.User;
import com.example.ezlawbackend.Auth.service.StripeService;
import com.example.ezlawbackend.Auth.service.UserService;
import com.stripe.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Subscription;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000",
        allowedHeaders = {"Content-Type", "Accept"},
        methods = {RequestMethod.POST,RequestMethod.GET,RequestMethod.DELETE,RequestMethod.PUT},
        allowCredentials = "true")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private  UserMySQLRepository userMySQLRepository;

    @Autowired
    private StripeService stripeService;

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
    public ResponseEntity<?> upgradeUser(@RequestBody Map<String, String> request, HttpSession session) {
        try {
            String email = (String) session.getAttribute("email");
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "No active session"));
            }

            UserMySQL userMySQL = userMySQLRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create Stripe subscription in THB
            Subscription subscription = stripeService.createThaiSubscription(userMySQL, request.get("planType"));

            String invoiceId = subscription.getLatestInvoice();
            Invoice invoice = Invoice.retrieve(invoiceId); // Fetch the invoice from Stripe
            String paymentIntentId = invoice.getPaymentIntent();

            // Fetch the PaymentIntent using its ID
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            // Get the client secret
            String clientSecret = paymentIntent.getClientSecret();

            return ResponseEntity.ok(Map.of(
                    "message", "Subscription initiated",
                    "subscriptionId", subscription.getId(),
                    "clientSecret", clientSecret
            ));
        } catch (Exception e) {
            logger.error("Upgrade failed", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Upgrade failed"));
        }
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