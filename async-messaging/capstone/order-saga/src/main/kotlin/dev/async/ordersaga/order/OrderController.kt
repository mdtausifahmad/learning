package dev.async.ordersaga.order

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/orders")
class OrderController(private val orderService: OrderService) {

    data class PlaceOrderRequest(
        @field:NotBlank val customerId: String,
        val sku: String = "SKU-1",
        @field:Positive val qty: Int = 1,
        @field:Positive val amount: BigDecimal,
    )

    /** Place an order → returns immediately with a PENDING orderId (Lesson 01: async, the saga runs in the background). */
    @PostMapping
    fun place(@RequestBody req: PlaceOrderRequest): Map<String, String> {
        val orderId = orderService.placeOrder(req.customerId, req.sku, req.qty, req.amount)
        return mapOf("orderId" to orderId, "status" to "PENDING")
    }

    /** Poll the outcome (Lesson 13/15: an async saga initiated by a sync request). */
    @GetMapping("/{id}")
    fun status(@PathVariable id: String): Map<String, String> {
        val order = orderService.find(id) ?: return mapOf("orderId" to id, "status" to "NOT_FOUND")
        return mapOf("orderId" to order.id, "status" to order.status.name)
    }
}
