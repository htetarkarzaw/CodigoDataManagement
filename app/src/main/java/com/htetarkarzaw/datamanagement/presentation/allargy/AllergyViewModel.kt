package com.htetarkarzaw.datamanagement.presentation.allargy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.domain.usecase.GetAllergiesUsecase
import com.htetarkarzaw.datamanagement.domain.vo.AllergyVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllergyViewModel @Inject constructor(
    private val getAllergiesUsecase: GetAllergiesUsecase
) : ViewModel() {
    private val _selectedAllergies = MutableStateFlow<List<AllergyVO>>(emptyList())
    val selectedAllergies = _selectedAllergies.asStateFlow()
    private val _allergies =
        MutableStateFlow<Resource<List<AllergyVO>>>(Resource.Nothing())
    val allergies = _allergies.asStateFlow()

    init {
        getAllergies()
    }

    fun getAllergies() {
        viewModelScope.launch {
            getAllergiesUsecase.invoke().collectLatest {
                _allergies.value = it
            }
        }
    }

    fun addAllergy(data: AllergyVO) {
        val temp = selectedAllergies.value.toMutableList()
        temp.removeIf {
            it.id == data.id
        }
        temp.add(data)
        _selectedAllergies.value = temp
    }

    fun removeAllergy(data: AllergyVO) {
        val temp = selectedAllergies.value.toMutableList()
        temp.removeIf {
            it.id == data.id
        }
        _selectedAllergies.value = temp
    }

    fun addNewAllergy(name: String) {
        val id = allergies.value.data?.toMutableList()?.size ?: 0
        val item = AllergyVO(
            id = id + 1,
            name = name
        )
        val temp = selectedAllergies.value.toMutableList()
        temp.add(item)
        _selectedAllergies.value = temp
    }
}