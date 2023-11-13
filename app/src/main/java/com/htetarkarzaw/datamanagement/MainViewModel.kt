package com.htetarkarzaw.datamanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.domain.usecase.ExportJsonUsecase
import com.htetarkarzaw.datamanagement.domain.vo.AllergyVO
import com.htetarkarzaw.datamanagement.domain.vo.DietVO
import com.htetarkarzaw.datamanagement.domain.vo.HealthConcernVO
import com.htetarkarzaw.datamanagement.domain.vo.SimpleOutput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val exportJsonUsecase: ExportJsonUsecase
) : ViewModel() {
    private var healthConcerns: MutableList<HealthConcernVO> = mutableListOf()
    private var diets: MutableList<DietVO> = mutableListOf()
    private var allergies: MutableList<AllergyVO> = mutableListOf()
    private var isDailyExposure: Boolean = false
    private var isSmoke: Boolean = false
    private var alcohol: String = ""

    private val _exportJson = MutableStateFlow<Resource<String>>(Resource.Nothing())
    val exportJson get() = _exportJson.asStateFlow()
    fun setHealthConcerns(list: MutableList<HealthConcernVO>) {
        healthConcerns = list
    }

    fun setDiets(list: MutableList<DietVO>) {
        diets = list
    }

    fun setAllergies(list: MutableList<AllergyVO>) {
        allergies = list
    }

    fun setIsDailyExposure(isDailyExposure: Boolean) {
        this.isDailyExposure = isDailyExposure
    }

    fun setIsSmoke(isSmoke: Boolean) {
        this.isSmoke = isSmoke
    }

    fun setAlcohol(alcohol: String) {
        this.alcohol = alcohol
    }

    fun exportJson(file: File) {
        _exportJson.value = Resource.Loading()
        viewModelScope.launch {
            val simpleOutPut = SimpleOutput(
                alchol = alcohol,
                allergies = allergies,
                diets = diets,
                health_concerns = healthConcerns,
                is_daily_exposure = isDailyExposure,
                is_somke = isSmoke
            )
            exportJsonUsecase.invoke(simpleOutPut, file).collectLatest {
                _exportJson.value = it
            }
        }
    }
}