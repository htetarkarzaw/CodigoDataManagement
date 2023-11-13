package com.htetarkarzaw.datamanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.domain.usecase.ExportJsonUsecase
import com.htetarkarzaw.datamanagement.domain.vo.AllergyVO
import com.htetarkarzaw.datamanagement.domain.vo.ExportDietVO
import com.htetarkarzaw.datamanagement.domain.vo.HealthConcernVO
import com.htetarkarzaw.datamanagement.domain.vo.SimpleOutput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val exportJsonUsecase: ExportJsonUsecase
) : ViewModel() {
    private var healthConcerns: MutableList<HealthConcernVO> = mutableListOf()
    private var diets: MutableList<ExportDietVO> = mutableListOf()
    private var allergies: MutableList<AllergyVO> = mutableListOf()
    private var isDailyExposure: Boolean = false
    private var isSmoke: Boolean = false
    private var alcohol: String = ""

    private val _exportJson = MutableSharedFlow<Resource<String>>()
    val exportJson get() = _exportJson.asSharedFlow()
    fun setHealthConcerns(list: MutableList<HealthConcernVO>) {
        healthConcerns = list
    }

    fun setDiets(list: MutableList<ExportDietVO>) {
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

    fun exportJson() {
        viewModelScope.launch {
            _exportJson.emit(Resource.Loading())
            val simpleOutPut = SimpleOutput(
                alchol = alcohol,
                allergies = allergies,
                diets = diets,
                health_concerns = healthConcerns,
                is_daily_exposure = isDailyExposure,
                is_somke = isSmoke
            )
            exportJsonUsecase.invoke(simpleOutPut).collectLatest {
                _exportJson.emit(it)
            }
        }
    }
}