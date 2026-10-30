package com.franco.whereisit.viewmodel

import androidx.lifecycle.ViewModel
import com.franco.whereisit.domain.model.CampusZone
import com.franco.whereisit.domain.model.ItemCategory
import com.franco.whereisit.domain.model.ItemFilter
import com.franco.whereisit.domain.model.ItemStatus
import com.franco.whereisit.domain.model.LostItem
import com.franco.whereisit.domain.repository.IItemRepository
import java.time.LocalDate

class ItemListViewModel(private val repository: IItemRepository) : ViewModel() {

    var filter = ItemFilter()
        private set

    // Call this after changing a filter to refresh the list
    fun getItems(): List<LostItem> = repository.getFilteredItems(filter)

    fun setQuery(query: String) {
        filter = filter.copy(query = query)
    }

    fun setStatus(status: ItemStatus?) {
        filter = filter.copy(status = status)
    }

    fun setCategory(category: ItemCategory?) {
        filter = filter.copy(category = category)
    }

    fun setZone(zone: CampusZone?) {
        filter = filter.copy(zone = zone)
    }

    fun setFromDate(date: LocalDate?) {
        filter = filter.copy(fromDate = date)
    }

    fun clearFilters() {
        filter = ItemFilter()
    }

    fun hasActiveFilters(): Boolean = filter != ItemFilter()
}