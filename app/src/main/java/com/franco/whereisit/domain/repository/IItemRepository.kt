package com.franco.whereisit.domain.repository

import com.franco.whereisit.domain.model.Claim
import com.franco.whereisit.domain.model.DeliveryMethod
import com.franco.whereisit.domain.model.ItemFilter
import com.franco.whereisit.domain.model.LostItem
import com.franco.whereisit.domain.model.User

interface IItemRepository {
    fun getItems(): List<LostItem>
    fun getById(id: String): LostItem?
    fun addItem(item: LostItem)
    fun updateItem(item: LostItem)
    fun deleteItem(id: String)
    fun getFilteredItems(filter: ItemFilter): List<LostItem>
    fun markAsDelivered(id: String, method: DeliveryMethod)
    fun submitClaim(claim: Claim)
    fun getCurrentUser(): User
}