package ro.tayfy.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import ro.tayfy.payment.validator.ValidTotalPrice;

import java.math.BigDecimal;

@Data
@ValidTotalPrice
public class PaymentRequestDTO {
    private String productName;

    private BigDecimal monthlyRate;

    @DecimalMin(value = "0.00", message = "Total price must be a positive number")
    private BigDecimal totalPrice;

    private int remainingMonths;

    private boolean isRateAdjustable;

    private String loanType; // Should be ZERO_INTEREST, PERSONAL, or MORTGAGE
}
