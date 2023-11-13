package com.htetarkarzaw.datamanagement.domain.vo

data class HealthConcernVO(
    val id: Int,
    val name: String,
    val priority: Int? = null
)