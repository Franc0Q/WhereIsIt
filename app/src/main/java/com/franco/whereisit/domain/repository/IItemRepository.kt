package com.franco.whereisit.domain.repository

import com.franco.whereisit.domain.model.Claim
import com.franco.whereisit.domain.model.DeliveryMethod
import com.franco.whereisit.domain.model.ItemFilter
import com.franco.whereisit.domain.model.LostItem

interface IItemRepository {
    fun getItems(filter: ItemFilter): List<LostItem>
    fun getItem(id: String): LostItem?
    fun addItem(item: LostItem)
    fun updateItem(item: LostItem)
    fun deleteItem(id: String)
    fun markAsDelivered(id: String, method: DeliveryMethod)
    fun submitClaim(claim: Claim)
}