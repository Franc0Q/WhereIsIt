package com.franco.whereisit.viewmodel

import androidx.lifecycle.ViewModel
import com.franco.whereisit.domain.model.ItemFilter
import com.franco.whereisit.domain.model.LostItem
import com.franco.whereisit.domain.repository.IItemRepository

class MyItemsViewModel(private val repository: IItemRepository) : ViewModel() {

    // The item waiting for the "Delete post?" dialog answer
    var itemPendingDelete: LostItem? = null
        private set

    fun getMyItems(): List<LostItem> {
        val userId = repository.getCurrentUser().id
        return repository.getFilteredItems(ItemFilter(authorId = userId))
    }

    // Call when the user taps "Delete" (then show the dialog)
    fun requestDelete(item: LostItem) {
        itemPendingDelete = item
    }

    fun cancelDelete() {
        itemPendingDelete = null
    }

    // Call when the user confirms in the dialog
    fun confirmDelete(): Boolean {
        val item = itemPendingDelete ?: return false
        repository.deleteItem(item.id)
        itemPendingDelete = null
        return true
    }
}