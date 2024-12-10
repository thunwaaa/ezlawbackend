package com.example.ezlawbackend.Auth.repository;

import com.example.ezlawbackend.Auth.model.PlanMembershipMySQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanMemberMySQLRepository extends JpaRepository<PlanMembershipMySQL, Long> {
    PlanMembershipMySQL findByPriceId(String priceId);
    PlanMembershipMySQL findByPlanName(String planName);
}
