package com.htetarkarzaw.datamanagement.data.dto

data class HealthConcernsDto(
    val data: List<HealthConcerns>
)

data class HealthConcerns(
    val id: Int,
    val name: String
)