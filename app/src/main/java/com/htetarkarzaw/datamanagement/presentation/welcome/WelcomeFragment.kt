package com.htetarkarzaw.datamanagement.presentation.welcome

import androidx.navigation.fragment.findNavController
import com.htetarkarzaw.datamanagement.R
import com.htetarkarzaw.datamanagement.databinding.FragmentWelcomeBinding
import com.htetarkarzaw.datamanagement.presentation.base.BaseFragment

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(FragmentWelcomeBinding::inflate) {
    override fun observe() {
    }

    override fun initUi() {
        binding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentWelcome_to_fragmentHealthConcerns)
        }
    }

}