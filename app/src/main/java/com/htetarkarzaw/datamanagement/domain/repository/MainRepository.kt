package com.htetarkarzaw.datamanagement.domain.repository

import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.domain.vo.HealthConcernVO
import com.htetarkarzaw.datamanagement.domain.vo.SimpleOutput
import kotlinx.coroutines.flow.Flow
import java.io.File

interface MainRepository {
    suspend fun getHealthConcernsFromJson(): Flow<Resource<List<HealthConcernVO>>>
    suspend fun exportJson(file: File,simpleOutput: SimpleOutput): Flow<Resource<String>>
}