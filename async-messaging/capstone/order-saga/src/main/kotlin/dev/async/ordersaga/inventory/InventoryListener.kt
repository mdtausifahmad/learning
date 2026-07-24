package dev.async.ordersaga.inventory

import dev.async.ordersaga.common.ProcessedMessageRepository
import dev.async.ordersaga.common.ReleaseInventory
import dev.async.ordersaga.common.ReserveInventory
import dev.async.ordersaga.config.Topics
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Inventory participant. One class-level @KafkaListener + @KafkaHandler per message type
 * (the topic carries both ReserveInventory and ReleaseInventory).
 */
@Component
@KafkaListener(topics = [Topics.INVENTORY_COMMANDS], groupId = "inventory")
class InventoryListener(
    private val items: InventoryItemRepository,
    private val processed: ProcessedMessageRepository,
    private val kafka: KafkaTemplate<String, Any>,
) {

    /**
     * Milestone 2 (Lessons 08 + 05). Make this idempotent AND atomic:
     *   - if processed.existsByConsumerAndMessageId("inventory", cmd.messageId) → skip (duplicate), and
     *   - otherwise reserve stock (available -= qty, reserved += qty) AND save a ProcessedMessage,
     *     all in this @Transactional method (so the dedup mark commits with the side effect).
     *   - reply InventoryReserved (success) or InventoryFailed (not enough stock) to INVENTORY_REPLIES,
     *     keyed by orderId.
     *
     * Prove it: send the same ReserveInventory twice → stock reserved exactly once.
     */
    @KafkaHandler
    @Transactional
    fun onReserve(cmd: ReserveInventory) {
        TODO("M2: idempotent reserve → reply InventoryReserved / InventoryFailed")
    }

    /** Milestone 4 (Lesson 15 — compensation). Idempotently release a prior reservation. */
    @KafkaHandler
    @Transactional
    fun onRelease(cmd: ReleaseInventory) {
        TODO("M4: idempotent compensation — release the reservation (reserved -= qty, available += qty)")
    }

    @KafkaHandler(isDefault = true)
    fun onUnknown(payload: Any) { /* ignore other types on this topic */ }
}
