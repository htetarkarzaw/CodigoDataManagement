package com.htetarkarzaw.datamanagement.domain.usecase

import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.domain.repository.MainRepository
import com.htetarkarzaw.datamanagement.domain.vo.HealthConcernVO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHealthConcernsUsecase @Inject constructor(private val repo: MainRepository) {
    suspend operator fun invoke(): Flow<Resource<List<HealthConcernVO>>> {
        return repo.getHealthConcernsFromJson()
    }
}