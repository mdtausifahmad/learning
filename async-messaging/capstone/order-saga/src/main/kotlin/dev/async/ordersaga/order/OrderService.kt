package dev.async.ordersaga.order

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class OrderService(
    private val orders: OrderRepository,
    private val outbox: OutboxRepository,
) {
    /**
     * Milestone 1 (Lesson 14 — the transactional outbox).
     *
     * In ONE @Transactional unit:
     *   1. create an Order in PENDING state (id = a new orderId), and
     *   2. insert an OutboxEvent whose payload is an OrderCreated event, topic = Topics.ORDER_CREATED,
     *      msgKey = orderId (Lesson 09 keying), type = OrderCreated::class.java.name.
     * Return the orderId.
     *
     * Because both writes commit together, you can never end up with an order but no event (or vice
     * versa). The [OutboxRelay] publishes the event afterwards.
     *
     * Tip: serialize the OrderCreated payload to a JSON String (inject a Jackson ObjectMapper, or
     * build the JSON yourself). Keep the messageId stable — it's the consumer's dedup key (Lesson 08).
     */
    @Transactional
    fun placeOrder(customerId: String, sku: String, qty: Int, amount: BigDecimal): String {
        TODO("M1: create Order(PENDING) + insert OutboxEvent(OrderCreated) in this transaction; return orderId")
    }

    fun find(orderId: String): Order? = orders.findById(orderId).orElse(null)
}
