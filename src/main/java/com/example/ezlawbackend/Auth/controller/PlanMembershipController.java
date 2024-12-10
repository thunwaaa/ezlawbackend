package com.example.ezlawbackend.Auth.controller;

import com.example.ezlawbackend.Auth.model.PlanMembershipMySQL;
import com.example.ezlawbackend.Auth.service.PlanMembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlanMembershipController {

    @Autowired
    private PlanMembershipService planMembershipService;

    @GetMapping("/api/plans")
    public List<PlanMembershipMySQL> getPlans() {
        return planMembershipService.getAllPlans();
    }
}
