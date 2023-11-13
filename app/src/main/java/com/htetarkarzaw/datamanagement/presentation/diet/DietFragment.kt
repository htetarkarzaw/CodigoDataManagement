package com.htetarkarzaw.datamanagement.presentation.diet

import androidx.navigation.fragment.findNavController
import com.htetarkarzaw.datamanagement.R
import com.htetarkarzaw.datamanagement.databinding.FragmentDietBinding
import com.htetarkarzaw.datamanagement.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DietFragment : BaseFragment<FragmentDietBinding>(FragmentDietBinding::inflate) {
    override fun observe() {
    }

    override fun initUi() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentDiet_to_allargyFragment)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}