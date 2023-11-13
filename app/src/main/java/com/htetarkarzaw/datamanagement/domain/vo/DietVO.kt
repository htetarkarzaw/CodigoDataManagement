package com.htetarkarzaw.datamanagement.domain.vo

data class DietVO(
    val id: Int,
    val name: String,
    val tool_tip: String,
    val isSelected: Boolean = false
){
    fun toExportVo(): ExportDietVO {
        return ExportDietVO(
            id = id,
            name = name,
            tool_tip = tool_tip
        )
    }
}