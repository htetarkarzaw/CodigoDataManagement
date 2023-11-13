package com.htetarkarzaw.datamanagement.presentation.allargy

import androidx.navigation.fragment.findNavController
import com.htetarkarzaw.datamanagement.R
import com.htetarkarzaw.datamanagement.databinding.FragmentAllargyBinding
import com.htetarkarzaw.datamanagement.databinding.FragmentDietBinding
import com.htetarkarzaw.datamanagement.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllargyFragment : BaseFragment<FragmentAllargyBinding>(FragmentAllargyBinding::inflate) {
    override fun observe() {
    }

    override fun initUi() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_allargyFragment_to_alcoholFragment)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}