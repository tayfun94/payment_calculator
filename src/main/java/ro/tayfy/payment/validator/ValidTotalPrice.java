package ro.tayfy.payment.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TotalPriceValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTotalPrice {
    String message() default "Invalid total price for the selected loan type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}