package com.example.ezlawbackend.Auth.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice")
public class InvoiceMySQL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserMySQL user;

    @Column(nullable = false)
    private String planType; // weekly, monthly, yearly

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String stripeSubscriptionId;

    @Column(nullable = false)
    private LocalDateTime subscriptionStart;

    @Column(nullable = false)
    private LocalDateTime subscriptionEnd;

    @Column(nullable = false)
    private String status; // active, cancelled, expired

    @Column
    private LocalDateTime cancelledAt;

    @Column(nullable = false)
    private String currency = "thb"; // Add currency field

    @Column
    private String paymentMethod; // Optional: track payment method

    @Column
    private String stripePaymentIntentId;

    // Default constructor
    public InvoiceMySQL() {}

    // Constructor with fields
    public InvoiceMySQL(UserMySQL user, String planType, double amount, String stripeSubscriptionId,
                   LocalDateTime subscriptionStart, LocalDateTime subscriptionEnd) {
        this.user = user;
        this.planType = planType;
        this.amount = amount;
        this.stripeSubscriptionId = stripeSubscriptionId;
        this.subscriptionStart = subscriptionStart;
        this.subscriptionEnd = subscriptionEnd;
        this.status = "active";
        this.currency = "thb";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserMySQL getUser() { return user; }
    public void setUser(UserMySQL user) { this.user = user; }

    public String getPlanType() { return planType; }
    public void setPlanType(String planType) { this.planType = planType; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getStripeSubscriptionId() { return stripeSubscriptionId; }
    public void setStripeSubscriptionId(String stripeSubscriptionId) { this.stripeSubscriptionId = stripeSubscriptionId; }

    public LocalDateTime getSubscriptionStart() { return subscriptionStart; }
    public void setSubscriptionStart(LocalDateTime subscriptionStart) { this.subscriptionStart = subscriptionStart; }

    public LocalDateTime getSubscriptionEnd() { return subscriptionEnd; }
    public void setSubscriptionEnd(LocalDateTime subscriptionEnd) { this.subscriptionEnd = subscriptionEnd; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }

    public String getCurrency() {return currency;}

    public void setCurrency(String currency) {this.currency = currency;}

    public String getPaymentMethod() {return paymentMethod;}

    public void setPaymentMethod(String paymentMethod) {this.paymentMethod = paymentMethod;}

    public String getStripePaymentIntentId() {return stripePaymentIntentId;}
    public void setStripePaymentIntentId(String stripePaymentIntentId) {this.stripePaymentIntentId = stripePaymentIntentId;}
}
