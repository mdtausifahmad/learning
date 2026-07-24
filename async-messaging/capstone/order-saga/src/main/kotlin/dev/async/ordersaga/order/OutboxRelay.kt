package dev.async.ordersaga.order

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Milestone 1 (Lesson 14 — the message relay, polling-publisher variant).
 *
 * Periodically publish unsent outbox rows to Kafka, in insertion order, then mark them sent.
 * This is at-least-once (a crash after publishing but before marking sent → republish on the next
 * tick), which is exactly why every consumer in this app must be idempotent (Lesson 08).
 */
@Component
class OutboxRelay(
    private val outbox: OutboxRepository,
    private val kafka: KafkaTemplate<String, Any>,
) {
    @Scheduled(fixedDelay = 500)
    @Transactional
    fun publishPending() {
        // TODO(M1):
        //   for each row in outbox.findTop100BySentIsFalseOrderByCreatedAtAsc():
        //       deserialize row.payload back into its event object (row.type tells you the class), and
        //       kafka.send(row.topic, row.msgKey, event)   // keyed → per-order ordering (Lesson 09)
        //       row.sent = true
        //   (rows are managed entities inside this tx, so setting sent=true persists on commit)
        //
        // Prove it: kill the app between the DB commit and the send — on restart the event still
        // publishes. No lost events, no phantom events.
    }
}
