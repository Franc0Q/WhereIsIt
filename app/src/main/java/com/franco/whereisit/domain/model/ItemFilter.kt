package com.franco.whereisit.domain.model

data class ItemFilter(val query: String = "", val status: ItemStatus? = null, val category: ItemCategory? = null, val zone: CampusZone? = null, val authorId: String? = null){

}