package com.example.ezlawbackend.Auth.util;

import com.stripe.model.Subscription;

public class StripeUtil {
    public static String extractPlantype(Subscription subscription){

        String priceId = subscription.getItems().getData().get(0).getPrice().getId();
        if (priceId.contains("weekly")) return "weekly";
        if (priceId.contains("monthly")) return "monthly";
        if (priceId.contains("yearly")) return "yearly";
        throw new RuntimeException("Unknown plan type");
    }

    public static double extractSubscriptionAmount(Subscription subscription){
        return subscription.getItems().getData().get(0).getPrice().getUnitAmount() / 100.0;
    }
}
