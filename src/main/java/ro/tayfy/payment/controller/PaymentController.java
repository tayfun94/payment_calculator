package ro.tayfy.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ro.tayfy.payment.dto.PaymentRequestDTO;
import ro.tayfy.payment.model.Payment;
import ro.tayfy.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public List<Payment> getAllPayments(Authentication authentication) {
        String username = authentication.getName();
        return paymentService.getAllPayments(username);
    }

    @PutMapping("/{paymentId}/rate")
    public Payment updateMonthlyRate(@PathVariable Long paymentId, @RequestParam BigDecimal newRate) {
        return paymentService.updateMonthlyRate(paymentId, newRate);
    }

    @PostMapping
    public Payment addPayment(Authentication authentication, @Valid @RequestBody PaymentRequestDTO paymentRequest) {
        String username = authentication.getName();
        return paymentService.addPayment(username, paymentRequest);
    }

    @GetMapping("/total-current-month")
    public BigDecimal getTotalForCurrentMonth(Authentication authentication) {
        return paymentService.calculateTotalForCurrentMonth(authentication.getName());
    }
}
