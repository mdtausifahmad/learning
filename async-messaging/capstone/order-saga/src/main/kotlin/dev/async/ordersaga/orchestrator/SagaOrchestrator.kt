package dev.async.ordersaga.orchestrator

import dev.async.ordersaga.common.InventoryFailed
import dev.async.ordersaga.common.InventoryReserved
import dev.async.ordersaga.common.OrderCreated
import dev.async.ordersaga.common.PaymentCharged
import dev.async.ordersaga.common.PaymentFailed
import dev.async.ordersaga.config.Topics
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

/**
 * Orchestration-based saga (Lessons 13 & 15). One coordinator listens to the order-start topic and
 * both reply topics, and drives the flow with commands:
 *
 *   OrderCreated ─▶ ReserveInventory ─▶ InventoryReserved ─▶ ChargePayment ─▶ PaymentCharged
 *                                                                              │  (pivot) confirm order
 *                                                                              ▼  → OrderConfirmed
 *   failure at any step ─▶ compensate the completed steps ─▶ OrderCancelled
 *
 * All sends are keyed by orderId (Lesson 09). Every handler is reached at-least-once, so keep the
 * state transitions idempotent (guard on the persisted SagaInstance state).
 */
@Component
@KafkaListener(
    topics = [Topics.ORDER_CREATED, Topics.INVENTORY_REPLIES, Topics.PAYMENT_REPLIES],
    groupId = "orchestrator",
)
class SagaOrchestrator(
    private val kafka: KafkaTemplate<String, Any>,
    private val sagas: SagaInstanceRepository,
) {

    /** Milestone 3: start the saga (persist SagaInstance=STARTED) and send ReserveInventory. */
    @KafkaHandler
    fun onOrderCreated(e: OrderCreated) {
        TODO("M3: persist saga STARTED; send ReserveInventory(orderId, sku, qty) to INVENTORY_COMMANDS keyed by orderId")
    }

    /** Milestone 3: inventory reserved → charge payment. */
    @KafkaHandler
    fun onInventoryReserved(r: InventoryReserved) {
        TODO("M3: advance saga to INVENTORY_RESERVED; send ChargePayment(orderId, ...) to PAYMENT_COMMANDS")
    }

    /** Milestone 4: inventory failed → cancel the order (nothing to compensate yet). */
    @KafkaHandler
    fun onInventoryFailed(r: InventoryFailed) {
        TODO("M4: cancel — publish OrderCancelled to ORDER_EVENTS; mark saga CANCELLED")
    }

    /** Milestone 3 (pivot): payment charged → confirm the order. */
    @KafkaHandler
    fun onPaymentCharged(r: PaymentCharged) {
        TODO("M3: pivot — confirm order; publish OrderConfirmed to ORDER_EVENTS; mark saga COMPLETED")
    }

    /** Milestone 4: payment failed → compensate (release inventory), then cancel. */
    @KafkaHandler
    fun onPaymentFailed(r: PaymentFailed) {
        TODO("M4: send ReleaseInventory to compensate; publish OrderCancelled; mark saga COMPENSATING → CANCELLED")
    }

    @KafkaHandler(isDefault = true)
    fun onUnknown(payload: Any) { /* ignore */ }
}
