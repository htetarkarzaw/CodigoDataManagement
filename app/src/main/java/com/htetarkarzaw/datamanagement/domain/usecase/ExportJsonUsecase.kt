package com.htetarkarzaw.datamanagement.domain.usecase

import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.domain.repository.MainRepository
import com.htetarkarzaw.datamanagement.domain.vo.HealthConcernVO
import com.htetarkarzaw.datamanagement.domain.vo.SimpleOutput
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class ExportJsonUsecase @Inject constructor(private val repo: MainRepository) {
    suspend operator fun invoke(simpleOutput: SimpleOutput,file: File): Flow<Resource<String>> {
        return repo.exportJson(file,simpleOutput)
    }
}