package dev.async.ordersaga.payment

import dev.async.ordersaga.common.ChargePayment
import dev.async.ordersaga.common.ProcessedMessageRepository
import dev.async.ordersaga.config.Topics
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PaymentListener(
    private val payments: PaymentRepository,
    private val processed: ProcessedMessageRepository,
    private val kafka: KafkaTemplate<String, Any>,
) {
    /**
     * Milestone 3 (Lessons 08 + 15). Idempotent charge:
     *   - skip if already processed (dedup on cmd.messageId), else
     *   - "charge": save a Payment row + a ProcessedMessage in this tx, and
     *   - reply PaymentCharged to PAYMENT_REPLIES keyed by orderId.
     *
     * Simulate a decline to drive compensation (Milestone 4): if amount > 1000, reply PaymentFailed.
     */
    @KafkaListener(topics = [Topics.PAYMENT_COMMANDS], groupId = "payment")
    @Transactional
    fun onCharge(cmd: ChargePayment) {
        TODO("M3: idempotent charge → reply PaymentCharged; if amount > 1000 → PaymentFailed (drives M4 compensation)")
    }
}
