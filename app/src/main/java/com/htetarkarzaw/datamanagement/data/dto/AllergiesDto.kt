package com.htetarkarzaw.datamanagement.data.dto

data class AllergiesDto(
    val data: List<Allergy>
)

data class Allergy(
    val id: Int,
    val name: String
)