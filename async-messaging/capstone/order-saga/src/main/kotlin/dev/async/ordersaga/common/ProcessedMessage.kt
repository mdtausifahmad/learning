package dev.async.ordersaga.common

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import java.io.Serializable
import java.time.Instant

/**
 * Idempotent-consumer dedup store (Lesson 08).  One row per (consumer, messageId);
 * the composite primary key is the race-safe backstop against duplicate processing.
 */
@Entity
@Table(name = "processed_messages")
@IdClass(ProcessedMessageId::class)
class ProcessedMessage(
    @Id var consumer: String = "",
    @Id @Column(name = "message_id") var messageId: String = "",
    @Column(name = "processed_at", insertable = false, updatable = false) var processedAt: Instant? = null,
)

data class ProcessedMessageId(var consumer: String = "", var messageId: String = "") : Serializable

interface ProcessedMessageRepository : JpaRepository<ProcessedMessage, ProcessedMessageId> {
    fun existsByConsumerAndMessageId(consumer: String, messageId: String): Boolean
}
