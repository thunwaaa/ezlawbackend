package com.example.ezlawbackend.Auth.service;


import com.example.ezlawbackend.Auth.model.InvoiceMySQL;
import com.example.ezlawbackend.Auth.model.PlanMembershipMySQL;
import com.example.ezlawbackend.Auth.model.UserMySQL;
import com.example.ezlawbackend.Auth.repository.InvoiceMySQLRepository;
import com.example.ezlawbackend.Auth.repository.UserMySQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceMySQLRepository invoiceMySQLRepository;

    @Autowired
    private UserMySQLRepository userMySQLRepository;

    @Autowired
    private PlanMembershipService planMembershipService;

    private InvoiceMySQL createInvoice(String email, String planType, double amount, String stripeSubscriptionId,
                                       LocalDateTime subscriptionStart, LocalDateTime subscriptionEnd
    ){
        UserMySQL user = userMySQLRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InvoiceMySQL invoiceMySQL = new InvoiceMySQL(user, planType, amount, stripeSubscriptionId, subscriptionStart,
                subscriptionEnd);

        return invoiceMySQLRepository.save(invoiceMySQL);
    }

    public List<InvoiceMySQL> getUserInvoices(String email){
        UserMySQL user = userMySQLRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return invoiceMySQLRepository.findByUser(user);
    }

    public Optional<InvoiceMySQL> getActiveInvoice(String email){
        UserMySQL user = userMySQLRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<InvoiceMySQL> activeInvoices = invoiceMySQLRepository.findByUserAndStatus(user, "active");

        return activeInvoices.isEmpty() ? Optional.empty() : Optional.of(activeInvoices.get(0));
    }

    public void cancelInvoice(String stripeSubscriptionId) {
        InvoiceMySQL invoiceMySQL = invoiceMySQLRepository.findByStripeSubscriptionId(stripeSubscriptionId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoiceMySQL.setStatus("cancelled");
        invoiceMySQL.setCancelledAt(LocalDateTime.now());
        invoiceMySQLRepository.save(invoiceMySQL);
    }

    public InvoiceMySQL createThaiInvoice(String email, String planType, double amount, String stripeSubscriptionId) {
        UserMySQL user = userMySQLRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PlanMembershipMySQL plan = planMembershipService.findPlanByName(planType)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime subscriptionEnd = calculateSubscriptionEnd(plan.getDuration(), now);

        InvoiceMySQL invoiceMySQL = new InvoiceMySQL(user, planType, amount, stripeSubscriptionId, now, subscriptionEnd);
        invoiceMySQL.setCurrency("thb");

        return invoiceMySQLRepository.save(invoiceMySQL);
    }

    private LocalDateTime calculateSubscriptionEnd(String duration, LocalDateTime start) {
        switch (duration.toLowerCase()) {
            case "weekly": return start.plusWeeks(1);
            case "monthly": return start.plusMonths(1);
            case "yearly": return start.plusYears(1);
            default: throw new IllegalArgumentException("Invalid duration");
        }
    }
}
