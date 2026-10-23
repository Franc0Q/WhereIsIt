package com.franco.whereisit.data.memory

import com.franco.whereisit.domain.model.Claim
import com.franco.whereisit.domain.model.DeliveryMethod
import com.franco.whereisit.domain.model.ItemFilter
import com.franco.whereisit.domain.model.LostItem
import com.franco.whereisit.domain.repository.IItemRepository

class MemoryDataRepository: IItemRepository{
    private val items = mutableListOf<LostItem>()

    override fun getItems(filter: ItemFilter): List<LostItem> {
        TODO("Not yet implemented")
    }

    override fun getItem(id: String): LostItem? {
        TODO("Not yet implemented")
    }

    override fun addItem(item: LostItem) {
        TODO("Not yet implemented")
    }

    override fun updateItem(item: LostItem) {
        TODO("Not yet implemented")
    }

    override fun deleteItem(id: String) {
        TODO("Not yet implemented")
    }

    override fun markAsDelivered(
        id: String,
        method: DeliveryMethod
    ) {
        TODO("Not yet implemented")
    }

    override fun submitClaim(claim: Claim) {
        TODO("Not yet implemented")
    }
}