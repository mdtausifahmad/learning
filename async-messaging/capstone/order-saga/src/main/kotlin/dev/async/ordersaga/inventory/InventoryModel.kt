package dev.async.ordersaga.inventory

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "inventory_item")
class InventoryItem(
    @Id var sku: String = "",
    var available: Int = 0,
    var reserved: Int = 0,
)

interface InventoryItemRepository : JpaRepository<InventoryItem, String>
