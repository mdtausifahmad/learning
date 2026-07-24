package dev.async.ordersaga.common

import java.math.BigDecimal
import java.util.UUID

/**
 * Message contracts (Lesson 04: event vs command).  Each carries a stable [messageId]
 * that consumers use as the idempotency key (Lesson 08).
 *
 * Serialization: Spring Kafka's JsonSerializer writes the concrete type into a header, so each
 * topic can carry more than one type and the right class is deserialized on the other side.
 */

// ----- Commands (imperative: "do this") sent by the orchestrator -----
data class ReserveInventory(val orderId: String, val sku: String, val qty: Int, val messageId: String = UUID.randomUUID().toString())
data class ReleaseInventory(val orderId: String, val sku: String, val qty: Int, val messageId: String = UUID.randomUUID().toString())
data class ChargePayment(val orderId: String, val customerId: String, val amount: BigDecimal, val messageId: String = UUID.randomUUID().toString())

// ----- Events (past tense: "this happened") -----
data class OrderCreated(val orderId: String, val customerId: String, val sku: String, val qty: Int, val amount: BigDecimal, val messageId: String = UUID.randomUUID().toString())
data class OrderConfirmed(val orderId: String, val messageId: String = UUID.randomUUID().toString())
data class OrderCancelled(val orderId: String, val reason: String, val messageId: String = UUID.randomUUID().toString())

// ----- Replies from participant services -----
data class InventoryReserved(val orderId: String, val messageId: String = UUID.randomUUID().toString())
data class InventoryFailed(val orderId: String, val reason: String, val messageId: String = UUID.randomUUID().toString())
data class PaymentCharged(val orderId: String, val messageId: String = UUID.randomUUID().toString())
data class PaymentFailed(val orderId: String, val reason: String, val messageId: String = UUID.randomUUID().toString())
