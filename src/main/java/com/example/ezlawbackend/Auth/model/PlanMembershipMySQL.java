package com.example.ezlawbackend.Auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "plan_membership")
public class PlanMembershipMySQL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String planName;

    @Column(nullable = false)
    private String stripeLink;

    @Column(nullable = false)
    private String priceId;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String duration;

    // Default Constructor
    public PlanMembershipMySQL() {}

    // Constructor
    public PlanMembershipMySQL(String planName, String stripeLink, String priceId, Double price, String duration) {
        this.planName = planName;
        this.stripeLink = stripeLink;
        this.priceId = priceId;
        this.price = price;
        this.duration = duration;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getStripeLink() {
        return stripeLink;
    }

    public void setStripeLink(String stripeLink) {
        this.stripeLink = stripeLink;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
