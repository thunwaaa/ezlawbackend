package com.example.ezlawbackend.Auth.repository;

import com.example.ezlawbackend.Auth.model.InvoiceMySQL;
import com.example.ezlawbackend.Auth.model.UserMySQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceMySQLRepository extends JpaRepository<InvoiceMySQL, Long> {
    List<InvoiceMySQL> findByUser(UserMySQL user);

    Optional<InvoiceMySQL> findByStripeSubscriptionId(String stripeSubscriptionId);

    List<InvoiceMySQL> findByUserAndStatus(UserMySQL user, String status);
}
