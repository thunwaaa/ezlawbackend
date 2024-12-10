package com.example.ezlawbackend.Auth.service;

import com.example.ezlawbackend.Auth.model.PlanMembershipMySQL;
import com.example.ezlawbackend.Auth.repository.PlanMemberMySQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanMembershipService {

    @Autowired
    private PlanMemberMySQLRepository planMemberMySQLRepository;

    public List<PlanMembershipMySQL> getAllPlans() {
        return planMemberMySQLRepository.findAll();
    }

    public PlanMembershipMySQL savePlan(PlanMembershipMySQL plan) {
        return planMemberMySQLRepository.save(plan);
    }

    public Optional<PlanMembershipMySQL> findPlanByPriceId(String priceId) {
        return Optional.ofNullable(planMemberMySQLRepository.findByPriceId(priceId));
    }

    public Optional<PlanMembershipMySQL> findPlanByName(String planName) {
        return Optional.ofNullable(planMemberMySQLRepository.findByPlanName(planName));
    }


    public boolean validatePlan(String priceId, Double amount) {
        PlanMembershipMySQL plan = planMemberMySQLRepository.findByPriceId(priceId);
        if (plan == null) {
            throw new RuntimeException("Invalid plan selected.");
        }
        return plan.getPrice().equals(amount);
    }

}
