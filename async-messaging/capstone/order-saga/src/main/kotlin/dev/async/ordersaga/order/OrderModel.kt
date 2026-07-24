package dev.async.ordersaga.order

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

enum class OrderStatus { PENDING, CONFIRMED, CANCELLED }

@Entity
@Table(name = "orders")
class Order(
    @Id var id: String = "",
    @Column(name = "customer_id") var customerId: String = "",
    var amount: BigDecimal = BigDecimal.ZERO,
    @Enumerated(EnumType.STRING) var status: OrderStatus = OrderStatus.PENDING,
)

/** Transactional outbox row (Lesson 14). Written in the same tx as the business change; a relay publishes it. */
@Entity
@Table(name = "outbox")
class OutboxEvent(
    @Id var id: UUID = UUID.randomUUID(),
    var topic: String = "",
    @Column(name = "msg_key") var msgKey: String = "",
    var type: String = "",
    var payload: String = "",
    var sent: Boolean = false,
    @Column(name = "created_at", insertable = false, updatable = false) var createdAt: Instant? = null,
)

interface OrderRepository : JpaRepository<Order, String>

interface OutboxRepository : JpaRepository<OutboxEvent, UUID> {
    fun findTop100BySentIsFalseOrderByCreatedAtAsc(): List<OutboxEvent>
}
