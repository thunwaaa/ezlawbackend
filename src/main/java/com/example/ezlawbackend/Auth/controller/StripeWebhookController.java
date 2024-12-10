package com.example.ezlawbackend.Auth.controller;

import com.example.ezlawbackend.Auth.service.StripeService;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook/stripe")
public class StripeWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);

    @Autowired
    private StripeService stripeService;

    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        try {
            Event event = stripeService.constructEvent(payload, sigHeader);

            switch (event.getType()) {
                case "checkout.session.completed":
                    Session session = (Session) event.getData().getObject();
                    stripeService.handleCheckoutSessionCompleted(session);
                    break;

                case "customer.subscription.deleted":
                    Subscription subscription = (Subscription) event.getData().getObject();
                    stripeService.handleSubscriptionDeleted(subscription);
                    break;

                default:
                    logger.info("Unhandled event type: {}", event.getType());
            }

            return ResponseEntity.ok("Webhook processed successfully");
        } catch (Exception e) {
            logger.error("Webhook processing failed", e);
            return ResponseEntity.badRequest().body("Webhook processing failed");
        }
    }
}
