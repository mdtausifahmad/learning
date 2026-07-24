package dev.async.ordersaga.orchestrator

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository

/** Saga state (Lesson 15). One row per order; the orchestrator advances it as replies arrive. */
@Entity
@Table(name = "saga_instance")
class SagaInstance(
    @Id @Column(name = "order_id") var orderId: String = "",
    var state: String = "STARTED",   // STARTED | INVENTORY_RESERVED | COMPLETED | COMPENSATING | CANCELLED
)

interface SagaInstanceRepository : JpaRepository<SagaInstance, String>
