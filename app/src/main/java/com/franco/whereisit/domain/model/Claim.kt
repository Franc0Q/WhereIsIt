package com.franco.whereisit.domain.model

import java.time.LocalDate

data class Claim (val id: String = "", val itemId: String, val claimerId: String, val claimerName: String, val answer: String, val createdDate: LocalDate){
}