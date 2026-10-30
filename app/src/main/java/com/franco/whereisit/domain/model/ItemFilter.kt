package com.franco.whereisit.domain.model

import java.time.LocalDate

data class ItemFilter(val query: String = "", val status: ItemStatus? = null, val category: ItemCategory? = null, val zone: CampusZone? = null, val authorId: String? = null, val fromDate: LocalDate? = null){

}