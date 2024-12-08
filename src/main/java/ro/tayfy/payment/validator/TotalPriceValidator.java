package ro.tayfy.payment.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ro.tayfy.payment.dto.PaymentRequestDTO;

import java.math.BigDecimal;

public class TotalPriceValidator implements ConstraintValidator<ValidTotalPrice, PaymentRequestDTO> {

    @Override
    public boolean isValid(PaymentRequestDTO paymentRequest, ConstraintValidatorContext context) {
        if (paymentRequest.getLoanType().equalsIgnoreCase("ZERO_INTEREST")) {
            return paymentRequest.getTotalPrice() != null && paymentRequest.getTotalPrice().compareTo(BigDecimal.ZERO) > 0;
        } else {
            return paymentRequest.getTotalPrice() == null || paymentRequest.getTotalPrice().doubleValue() == 0.0d;
        }
    }
}
