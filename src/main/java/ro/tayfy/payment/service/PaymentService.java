package ro.tayfy.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.tayfy.payment.dto.PaymentRequestDTO;
import ro.tayfy.payment.model.Payment;
import ro.tayfy.payment.model.User;
import ro.tayfy.payment.repository.PaymentRepository;
import ro.tayfy.payment.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public List<Payment> getAllPayments(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return paymentRepository.findByUser(user);
    }

    public Payment updateMonthlyRate(Long paymentId, BigDecimal newRate) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if (!payment.isRateAdjustable()) {
            throw new IllegalStateException("Monthly rate for this payment cannot be adjusted");
        }
        payment.setMonthlyRate(newRate);
        log.info("Updated monthly rate for payment ID {}: {}", paymentId, newRate);
        return paymentRepository.save(payment);
    }

    public Payment addPayment(String username, PaymentRequestDTO paymentRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        BigDecimal totalPrice = null;
        if (paymentRequest.getLoanType().equalsIgnoreCase("ZERO_INTEREST")) {
            totalPrice = paymentRequest.getTotalPrice();
        }

        Payment payment = Payment.builder()
                .productName(paymentRequest.getProductName())
                .totalPrice(totalPrice)
                .monthlyRate(paymentRequest.getMonthlyRate())
                .remainingMonths(paymentRequest.getRemainingMonths())
                .isRateAdjustable(paymentRequest.isRateAdjustable())
                .loanType(Payment.LoanType.valueOf(paymentRequest.getLoanType())).user(user).build();
        return paymentRepository.save(payment);
    }

    public BigDecimal calculateTotalForCurrentMonth(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return paymentRepository.findTotalForCurrentMonth(user);
    }
}
