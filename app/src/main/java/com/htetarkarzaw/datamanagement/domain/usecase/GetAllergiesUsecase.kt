package com.htetarkarzaw.datamanagement.domain.usecase

import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.domain.repository.MainRepository
import com.htetarkarzaw.datamanagement.domain.vo.AllergyVO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllergiesUsecase @Inject constructor(private val repo: MainRepository) {
    suspend operator fun invoke(): Flow<Resource<List<AllergyVO>>> {
        return repo.getAllergiesFromJson()
    }
}