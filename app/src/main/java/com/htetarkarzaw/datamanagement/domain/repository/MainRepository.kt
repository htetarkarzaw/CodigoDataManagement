package com.htetarkarzaw.datamanagement.domain.repository

import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.domain.vo.AllergyVO
import com.htetarkarzaw.datamanagement.domain.vo.DietVO
import com.htetarkarzaw.datamanagement.domain.vo.HealthConcernVO
import com.htetarkarzaw.datamanagement.domain.vo.SimpleOutput
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun getHealthConcernsFromJson(): Flow<Resource<List<HealthConcernVO>>>
    suspend fun getDietsFromJson(): Flow<Resource<List<DietVO>>>
    suspend fun getAllergiesFromJson(): Flow<Resource<List<AllergyVO>>>
    suspend fun exportJson(simpleOutput: SimpleOutput): Flow<Resource<String>>
}