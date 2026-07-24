package dev.async.ordersaga.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Kafka topology + error handling.
 *
 * Partitions = 3 so you can explore ordering & parallelism (Lessons 03 & 09): messages are keyed by
 * orderId, so all events for one order stay ordered on one partition.
 */
@Configuration
@EnableScheduling
class KafkaConfig {

    @Bean fun orderCreatedTopic(): NewTopic = TopicBuilder.name(Topics.ORDER_CREATED).partitions(3).replicas(1).build()
    @Bean fun orderEventsTopic(): NewTopic = TopicBuilder.name(Topics.ORDER_EVENTS).partitions(3).replicas(1).build()
    @Bean fun inventoryCommandsTopic(): NewTopic = TopicBuilder.name(Topics.INVENTORY_COMMANDS).partitions(3).replicas(1).build()
    @Bean fun inventoryRepliesTopic(): NewTopic = TopicBuilder.name(Topics.INVENTORY_REPLIES).partitions(3).replicas(1).build()
    @Bean fun paymentCommandsTopic(): NewTopic = TopicBuilder.name(Topics.PAYMENT_COMMANDS).partitions(3).replicas(1).build()
    @Bean fun paymentRepliesTopic(): NewTopic = TopicBuilder.name(Topics.PAYMENT_REPLIES).partitions(3).replicas(1).build()

    /**
     * Milestone 5 / Lesson 11 — bounded retries with backoff, then a Dead-Letter Topic.
     *
     * TODO(M5): replace the default handler with:
     *   val recoverer = DeadLetterPublishingRecoverer(kafkaTemplate)          // routes to <topic>.DLT
     *   val backOff = ExponentialBackOffWithMaxRetries(3).apply { initialInterval = 500; multiplier = 2.0 }
     *   return DefaultErrorHandler(recoverer, backOff).apply {
     *       addNotRetryableExceptions(IllegalArgumentException::class.java)   // permanent → straight to DLT
     *   }
     * (Also add ErrorHandlingDeserializer in application.yml so an unparseable message can't kill the container.)
     */
    @Bean
    fun errorHandler(): DefaultErrorHandler = DefaultErrorHandler()
}
