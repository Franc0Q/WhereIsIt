package com.franco.whereisit.data.memory

import com.franco.whereisit.domain.model.Claim
import com.franco.whereisit.domain.model.DeliveryMethod
import com.franco.whereisit.domain.model.ItemFilter
import com.franco.whereisit.domain.model.ItemStatus
import com.franco.whereisit.domain.model.LostItem
import com.franco.whereisit.domain.model.User
import com.franco.whereisit.domain.repository.IItemRepository
import java.time.LocalDate
import java.util.UUID

class MemoryDataRepository : IItemRepository {

    private val currentUser = User(id = "user-me", name = "Franco")
    private val items = mutableListOf<LostItem>()
    private val claims = mutableListOf<Claim>()

    // Newest first (reversed() makes the last added item win ties on the same day)
    override fun getItems(): List<LostItem> {
        return items.reversed().sortedByDescending { it.createdDate }
    }

    override fun getById(id: String): LostItem? {
        return items.firstOrNull { it.id == id }
    }

    override fun addItem(item: LostItem) {
        items.add(
            item.copy(
                id = UUID.randomUUID().toString(),
                createdDate = LocalDate.now()
            )
        )
    }

    override fun updateItem(item: LostItem) {
        val index = items.indexOfFirst { it.id == item.id }
        if (index != -1) {
            items[index] = item
        }
    }

    override fun deleteItem(id: String) {
        items.removeAll { it.id == id }
    }

    override fun getFilteredItems(filter: ItemFilter): List<LostItem> {
        return getItems().filter { item ->
            val matchesQuery = filter.query.isBlank() ||
                    item.title.contains(filter.query.trim(), ignoreCase = true) ||
                    item.description.contains(filter.query.trim(), ignoreCase = true)
            val matchesStatus = filter.status == null || item.status == filter.status
            val matchesCategory = filter.category == null || item.category == filter.category
            val matchesZone = filter.zone == null || item.zone == filter.zone
            val matchesAuthor = filter.authorId == null || item.authorId == filter.authorId
            val matchesDate = filter.fromDate == null || !item.eventDate.isBefore(filter.fromDate)

            matchesQuery && matchesStatus && matchesCategory &&
                    matchesZone && matchesAuthor && matchesDate
        }
    }

    override fun markAsDelivered(id: String, method: DeliveryMethod) {
        val index = items.indexOfFirst { it.id == id }
        if (index != -1) {
            items[index] = items[index].copy(
                status = ItemStatus.DELIVERED,
                deliveryMethod = method
            )
        }
    }

    override fun submitClaim(claim: Claim) {
        claims.add(
            claim.copy(
                id = UUID.randomUUID().toString(),
                createdDate = LocalDate.now()
            )
        )
    }

    override fun getCurrentUser(): User {
        return currentUser
    }
}