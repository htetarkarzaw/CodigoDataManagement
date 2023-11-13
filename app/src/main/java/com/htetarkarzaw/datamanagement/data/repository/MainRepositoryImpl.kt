package com.htetarkarzaw.datamanagement.data.repository

import android.content.Context
import android.os.Environment
import android.util.Log
import com.htetarkarzaw.datamanagement.R
import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.data.dto.AllergiesDto
import com.htetarkarzaw.datamanagement.data.dto.DietsDto
import com.htetarkarzaw.datamanagement.data.dto.HealthConcernsDto
import com.htetarkarzaw.datamanagement.domain.repository.MainRepository
import com.htetarkarzaw.datamanagement.domain.vo.AllergyVO
import com.htetarkarzaw.datamanagement.domain.vo.DietVO
import com.htetarkarzaw.datamanagement.domain.vo.HealthConcernVO
import com.htetarkarzaw.datamanagement.domain.vo.SimpleOutput
import com.squareup.moshi.Moshi
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

    override suspend fun getDietsFromJson(): Flow<Resource<List<DietVO>>> = flow {
        try {
            val jsonString =
                context.resources.openRawResource(R.raw.diets).bufferedReader().use {
                    it.readText()
                }
            val moshi: Moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory()).build()
            val result =
                moshi.adapter(DietsDto::class.java).fromJson(jsonString)?.data?.map {
                    DietVO(
                        id = it.id,
                        name = it.name,
                        tool_tip = it.tool_tip
                    )
                }
            if (result.isNullOrEmpty()) {
                emit(Resource.Error("Something went wrong in getDietsFromJson!"))
            } else {
                emit(Resource.Success(result))
            }
        } catch (e: Exception) {
            Log.e("getDietsFromJson", "${e.localizedMessage}")
            emit(Resource.Error("Something went wrong in getDietsFromJson!"))
        }
    }

    override suspend fun getAllergiesFromJson(): Flow<Resource<List<AllergyVO>>> = flow {
        try {
            val jsonString =
                context.resources.openRawResource(R.raw.allergies).bufferedReader().use {
                    it.readText()
                }
            val moshi: Moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory()).build()
            val result =
                moshi.adapter(AllergiesDto::class.java).fromJson(jsonString)?.data?.map {
                    AllergyVO(
                        id = it.id,
                        name = it.name
                    )
                }
            if (result.isNullOrEmpty()) {
                emit(Resource.Error("Something went wrong in getAllergiesFromJson!"))
            } else {
                emit(Resource.Success(result))
            }
        } catch (e: Exception) {
            Log.e("getAllergiesFromJson", "${e.localizedMessage}")
            emit(Resource.Error("Something went wrong in getAllergiesFromJson!"))
        }
    }

    override suspend fun exportJson(
        simpleOutput: SimpleOutput
    ): Flow<Resource<String>> = flow {
        val fileName = "simple_output.json"
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val jsonFile = File(storageDir, fileName)
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
//        val outputType = Types.newParameterizedType(List::class.java, SimpleOutput::class.java)
//        val jsonAdapter: JsonAdapter<SimpleOutput> = moshi.adapter(outputType)
        val adapter = moshi.adapter(SimpleOutput::class.java)
        val json = adapter.toJson(simpleOutput)
        try {
            withContext(Dispatchers.IO) {
                FileWriter(jsonFile).use { writer ->
                    writer.write(json)
                }
            }
            Log.e("hakz.repo.createfile", "Your data is saved to ${jsonFile.absolutePath}")
            emit(Resource.Success("Your data is saved to ${jsonFile.absolutePath}"))
        } catch (e: Exception) {
            emit(Resource.Error("Something went wrong!"))
        }
    }
}