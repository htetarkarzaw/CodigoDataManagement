package com.htetarkarzaw.datamanagement.data.repository

import android.content.Context
import android.util.Log
import com.htetarkarzaw.datamanagement.R
import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.data.dto.HealthConcernsDto
import com.htetarkarzaw.datamanagement.domain.repository.MainRepository
import com.htetarkarzaw.datamanagement.domain.vo.HealthConcernVO
import com.htetarkarzaw.datamanagement.domain.vo.SimpleOutput
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val context: Context
) : MainRepository {
    override suspend fun getHealthConcernsFromJson(): Flow<Resource<List<HealthConcernVO>>> = flow {
        try {
            val jsonString =
                context.resources.openRawResource(R.raw.health_concern).bufferedReader().use {
                    it.readText()
                }
            val moshi: Moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory()).build()
            val result =
                moshi.adapter(HealthConcernsDto::class.java).fromJson(jsonString)?.data?.map {
                    HealthConcernVO(
                        id = it.id,
                        name = it.name
                    )
                }
            if (result.isNullOrEmpty()) {
                emit(Resource.Error("Something went wrong in getHealthConcernsFromJson!"))
            } else {
                emit(Resource.Success(result))
            }
        } catch (e: Exception) {
            Log.e("getHealthConcernsFromJson", "${e.localizedMessage}")
            emit(Resource.Error("Something went wrong in getHealthConcernsFromJson!"))
        }
    }

    override suspend fun exportJson(
        file: File,
        simpleOutput: SimpleOutput
    ): Flow<Resource<String>> = flow {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val outputType = Types.newParameterizedType(List::class.java, SimpleOutput::class.java)
        val jsonAdapter: JsonAdapter<SimpleOutput> = moshi.adapter(outputType)
        val json = jsonAdapter.toJson(simpleOutput)
        try {
            withContext(Dispatchers.IO) {
                FileWriter(file).use { writer ->
                    writer.write(json)
                }
            }
            Log.e("hakz.repo.createfile", "Your data is saved to ${file.absolutePath}")
            emit(Resource.Success("Your data is saved to ${file.absolutePath}"))
        } catch (e: Exception) {
            emit(Resource.Error("Something went wrong!"))
        }
    }
}