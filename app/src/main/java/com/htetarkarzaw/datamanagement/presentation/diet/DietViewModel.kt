package com.htetarkarzaw.datamanagement.presentation.diet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.domain.usecase.GetDietsUsecase
import com.htetarkarzaw.datamanagement.domain.vo.DietVO
import com.htetarkarzaw.datamanagement.domain.vo.ExportDietVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DietViewModel @Inject constructor(
    private val getDietsUsecase: GetDietsUsecase
) : ViewModel() {
    private val _diets =
        MutableStateFlow<Resource<List<DietVO>>>(Resource.Nothing())
    val diets get() = _diets.asStateFlow()

    private val _selectedDiet = MutableStateFlow<List<ExportDietVO>>(mutableListOf())

    val selectedDiet get() = _selectedDiet.asSharedFlow()

    init {
        getDiets()
        observeDiets()
    }

    private fun observeDiets() {
        viewModelScope.launch {
            _diets.collectLatest {
                if (it is Resource.Success) {
                    val temp = mutableListOf<ExportDietVO>()
                    it.data?.map { dietVO ->
                        if (dietVO.isSelected) {
                            temp.add(dietVO.toExportVo())
                        }
                    }
                    _selectedDiet.value = temp
                }
            }
        }
    }

    fun getDiets() {
        viewModelScope.launch {
            getDietsUsecase.invoke().collectLatest {
                _diets.value = it
            }
        }
    }

    fun toggleDiet(data: DietVO) {
        val temp = _diets.value.data?.toMutableList()
        if (temp != null) {
            val updateData = temp.map {
                if (it.id == data.id) data else it
            }
            _diets.value = Resource.Success(updateData)
        }
    }

    fun clearAllDiet() {
        val temp = _diets.value.data?.toMutableList()
        if (temp != null) {
            val updateData = temp.map {
                it.copy(isSelected = false)
            }
            _diets.value = Resource.Success(updateData)
        }
    }
}