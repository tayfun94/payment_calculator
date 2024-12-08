package ro.tayfy.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.tayfy.payment.model.Payment;
import ro.tayfy.payment.model.User;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUser(User user);

    @Query("SELECT COALESCE(SUM(p.monthlyRate), 0) FROM Payment p WHERE p.user = :user")
    BigDecimal findTotalForCurrentMonth(@Param("user") User user);
}
