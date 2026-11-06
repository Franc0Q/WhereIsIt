package com.franco.whereisit.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.franco.whereisit.R
import com.franco.whereisit.domain.model.CampusZone
import com.franco.whereisit.domain.model.Coordinates
import com.franco.whereisit.domain.model.ItemCategory
import com.franco.whereisit.domain.model.ItemFilter
import com.franco.whereisit.domain.model.ItemStatus
import com.franco.whereisit.domain.model.LostItem
import com.franco.whereisit.domain.repository.IItemRepository
import com.franco.whereisit.util.Util
import java.time.LocalDate

class ItemFormViewModel(private val repository: IItemRepository) : ViewModel() {

    // Not null when we are editing an existing post
    var editingItem: LostItem? = null
        private set

    var photoUri: String? = null
    var coordinates: Coordinates? = null

    fun loadItem(id: String): Boolean {
        val item = repository.getById(id) ?: return false
        editingItem = item
        photoUri = item.photoUri
        coordinates = item.coordinates
        return true
    }

    // Searches only the posts of the current user. Exact title first, then the newest match.
    fun searchByTitle(query: String): LostItem? {
        val text = query.trim()
        val authorId = repository.getCurrentUser()?.id ?: return null
        val results = repository.getFilteredItems(ItemFilter(query = text, authorId = authorId))
        return results.firstOrNull { it.title.equals(text, ignoreCase = true) }
            ?: results.firstOrNull()
    }

    fun deleteItem() {
        val item = editingItem ?: return
        repository.deleteItem(item.id)
        editingItem = null
    }

    fun reset() {
        editingItem = null
        photoUri = null
        coordinates = null
    }

    // Returns the error message (string resource) or null when everything is valid
    @StringRes
    fun validate(title: String, description: String): Int? {
        return when {
            title.trim().length < Util.MIN_TITLE_LENGTH -> R.string.error_title_required
            description.isBlank() -> R.string.error_description_required
            coordinates == null -> R.string.error_location_required
            else -> null
        }
    }

    // Call validate() first. Returns false if there is no location yet.
    fun saveItem(
        title: String,
        description: String,
        category: ItemCategory,
        status: ItemStatus,
        zone: CampusZone,
        eventDate: LocalDate
    ): Boolean {
        val location = coordinates ?: return false
        val existing = editingItem

        if (existing == null) {
            val user = repository.getCurrentUser() ?: return false
            repository.addItem(
                LostItem(
                    title = title.trim(),
                    description = description.trim(),
                    category = category,
                    status = status,
                    zone = zone,
                    coordinates = location,
                    photoUri = photoUri,
                    eventDate = eventDate,
                    createdDate = LocalDate.now(),
                    authorId = user.id,
                    authorName = user.name
                )
            )
        } else {
            repository.updateItem(
                existing.copy(
                    title = title.trim(),
                    description = description.trim(),
                    category = category,
                    status = status,
                    zone = zone,
                    coordinates = location,
                    photoUri = photoUri,
                    eventDate = eventDate
                )
            )
        }
        return true
    }
}