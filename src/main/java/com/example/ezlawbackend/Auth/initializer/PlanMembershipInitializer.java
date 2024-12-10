package com.example.ezlawbackend.Auth.initializer;

import com.example.ezlawbackend.Auth.model.PlanMembershipMySQL;
import com.example.ezlawbackend.Auth.repository.PlanMemberMySQLRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlanMembershipInitializer {

    @Autowired
    private PlanMemberMySQLRepository planMembershipRepository;

    @PostConstruct
    public void initializePlanMemberships() {
        if (planMembershipRepository.count() == 0) {
            planMembershipRepository.save(new PlanMembershipMySQL(
                    "Weekly",
                    "https://buy.stripe.com/test_fZedSzak77cm0Ks148",
                    "price_1QRg0fGLXc22ItMyOrM9mzrf",
                    29.0,
                    "weekly"
            ));
            planMembershipRepository.save(new PlanMembershipMySQL(
                    "Monthly",
                    "https://buy.stripe.com/test_5kAg0HgIv0NY50I6ot",
                    "price_1QRg1sGLXc22ItMy9O01X6Lz",
                    59.0,
                    "monthly"
            ));
            planMembershipRepository.save(new PlanMembershipMySQL(
                    "Yearly",
                    "https://buy.stripe.com/test_7sIcOv3VJgMW64M7sy",
                    "price_1QRg2LGLXc22ItMy3JAZd9u1",
                    99.0,
                    "yearly"
            ));
            System.out.println("Plan membership data initialized.");
        }
    }
}
