package dev.async.ordersaga.payment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal

@Entity
@Table(name = "payment")
class Payment(
    @Id @Column(name = "order_id") var orderId: String = "",
    var amount: BigDecimal = BigDecimal.ZERO,
    var status: String = "",   // CHARGED | REFUNDED | FAILED
)

interface PaymentRepository : JpaRepository<Payment, String>
