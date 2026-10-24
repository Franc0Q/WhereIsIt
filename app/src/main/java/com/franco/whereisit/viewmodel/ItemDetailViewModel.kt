package com.franco.whereisit.viewmodel

import androidx.lifecycle.ViewModel
import com.franco.whereisit.domain.model.Claim
import com.franco.whereisit.domain.model.DeliveryMethod
import com.franco.whereisit.domain.model.LostItem
import com.franco.whereisit.domain.repository.IItemRepository
import java.time.LocalDate

class ItemDetailViewModel(private val repository: IItemRepository) : ViewModel() {

    var item: LostItem? = null
        private set

    fun loadItem(id: String): LostItem? {
        item = repository.getById(id)
        return item
    }

    fun isOwner(): Boolean = item?.authorId == repository.getCurrentUser().id

    // Returns false if the answer is empty
    fun submitClaim(answer: String): Boolean {
        val current = item ?: return false
        if (answer.isBlank()) return false

        val user = repository.getCurrentUser()
        repository.submitClaim(
            Claim(
                itemId = current.id,
                claimerId = user.id,
                claimerName = user.name,
                answer = answer.trim(),
                createdDate = LocalDate.now()
            )
        )
        return true
    }

    fun markAsDelivered(method: DeliveryMethod) {
        val current = item ?: return
        repository.markAsDelivered(current.id, method)
        item = repository.getById(current.id)
    }
}