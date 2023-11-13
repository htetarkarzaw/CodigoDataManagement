package com.htetarkarzaw.datamanagement.domain.vo

data class SimpleOutput(
    val alchol: String,
    val allergies: List<AllergyVO>,
    val diets: List<ExportDietVO>,
    val health_concerns: List<HealthConcernVO>,
    val is_daily_exposure: Boolean,
    val is_somke: Boolean
)