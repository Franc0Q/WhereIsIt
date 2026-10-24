package com.franco.whereisit.viewmodel

import androidx.lifecycle.ViewModel
import com.franco.whereisit.domain.model.ItemFilter
import com.franco.whereisit.domain.model.ItemStatus
import com.franco.whereisit.domain.model.LostItem
import com.franco.whereisit.domain.repository.IItemRepository

class MapViewModel(private val repository: IItemRepository) : ViewModel() {

    var statusFilter: ItemStatus? = null
        private set

    // The item whose card is open at the bottom of the map
    var selectedItem: LostItem? = null
        private set

    // Items for the pins. Delivered items are hidden unless the filter asks for them.
    fun getMapItems(): List<LostItem> {
        val items = repository.getFilteredItems(ItemFilter(status = statusFilter))
        return if (statusFilter == null) {
            items.filter { it.status != ItemStatus.DELIVERED }
        } else {
            items
        }
    }

    fun setStatusFilter(status: ItemStatus?) {
        statusFilter = status
        selectedItem = null
    }

    // Call when the user taps a pin
    fun selectItem(id: String) {
        selectedItem = repository.getById(id)
    }

    fun clearSelection() {
        selectedItem = null
    }
}