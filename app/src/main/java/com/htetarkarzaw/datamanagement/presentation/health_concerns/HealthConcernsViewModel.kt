package com.htetarkarzaw.datamanagement.presentation.health_concerns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.domain.usecase.GetHealthConcernsUsecase
import com.htetarkarzaw.datamanagement.domain.vo.HealthConcernVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthConcernsViewModel @Inject constructor(
    private val getHealthConcernsUsecase: GetHealthConcernsUsecase
) : ViewModel() {

    private val _healthConcerns =
        MutableStateFlow<Resource<List<HealthConcernVO>>>(Resource.Nothing())
    val healthConcerns get() = _healthConcerns.asStateFlow()

    private val _selectedHC = MutableStateFlow<List<HealthConcernVO>>(emptyList())
    val selectedHC get() = _selectedHC.asStateFlow()

    init {
        getHealthConcerns()
    }

    fun getHealthConcerns() {
        viewModelScope.launch {
            getHealthConcernsUsecase.invoke().collectLatest {
                _healthConcerns.value = it
            }
        }
    }

    fun selectHealthConcerns(selectedHC: HealthConcernVO) {
        val temp = _selectedHC.value.mapIndexed { index, it ->
            it.copy(priority = index + 1)
        }.toMutableList()
        temp.add(
            HealthConcernVO(selectedHC.id, selectedHC.name, temp.size + 1)
        )
        _selectedHC.value = temp
    }

    fun unselectHealthConcern(unSelectedHC: HealthConcernVO) {
        val temp = _selectedHC.value.toMutableList()
        temp.removeIf { it.id == unSelectedHC.id }
        val result = temp.mapIndexed { index, it ->
            it.copy(priority = index + 1)
        }
        _selectedHC.value = result
    }

    fun changePosition(from: Int, to: Int) {
        val temp = _selectedHC.value.toMutableList()
        val item = temp[from]
        temp.removeIf { it.id == item.id }
        temp.add(to, item)
        val result =temp.mapIndexed { index, it ->
            it.copy(priority = index + 1)
        }
        _selectedHC.value = result
    }
}