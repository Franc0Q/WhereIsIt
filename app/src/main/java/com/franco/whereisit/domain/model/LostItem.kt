package com.franco.whereisit.domain.model

import java.time.LocalDate

data class LostItem(val id: String = "", val title: String, val description: String, val category: ItemCategory, val status: ItemStatus, val zone: CampusZone, val coordinates: Coordinates,
    val photoUri: String? = null, val eventDate: LocalDate, val createdDate: LocalDate, val authorId: String, val authorName: String, val deliveryMethod: DeliveryMethod? = null){

}