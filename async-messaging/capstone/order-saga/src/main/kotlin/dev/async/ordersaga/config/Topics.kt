package dev.async.ordersaga.config

/** Topic names. Auto-create is off (docker-compose), so they're declared as beans in [KafkaConfig]. */
object Topics {
    const val ORDER_CREATED = "order.created"          // OrderCreated → starts the saga
    const val ORDER_EVENTS = "order.events"            // OrderConfirmed / OrderCancelled (terminal, fan-out)
    const val INVENTORY_COMMANDS = "inventory.commands"// ReserveInventory / ReleaseInventory
    const val INVENTORY_REPLIES = "inventory.replies"  // InventoryReserved / InventoryFailed
    const val PAYMENT_COMMANDS = "payment.commands"    // ChargePayment
    const val PAYMENT_REPLIES = "payment.replies"      // PaymentCharged / PaymentFailed
}
