package com.htetarkarzaw.datamanagement.data.dto

data class DietsDto(
    val data: List<Diet>
)

data class Diet(
    val id: Int,
    val name: String,
    val tool_tip: String
)