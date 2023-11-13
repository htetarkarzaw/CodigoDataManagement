package com.htetarkarzaw.datamanagement.presentation.alcohol

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.htetarkarzaw.datamanagement.MainViewModel
import com.htetarkarzaw.datamanagement.R
import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.databinding.FragmentAlcoholBinding
import com.htetarkarzaw.datamanagement.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlcoholFragment : BaseFragment<FragmentAlcoholBinding>(FragmentAlcoholBinding::inflate) {
    private val sharedModel: MainViewModel by activityViewModels()

    override fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            sharedModel.exportJson.collectLatest {
                hideLoadingDialog()
                when (it) {
                    is Resource.Error -> {
                        errorDialog.setUpDialog("Export Json:${it.message}", isRetry = false) {
                            errorDialog.dismiss()
                        }
                    }

                    is Resource.Loading -> {
                        showLoadingDialog("Exporting...")
                    }

                    is Resource.Nothing -> {}
                    is Resource.Success -> {
                        it.data?.let { it1 ->
                            successDialog.setUpDialog(it1) {
                                findNavController().navigate(R.id.action_alcoholFragment_to_fragmentWelcome)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun initUi() {
        binding.rgDailyExposure.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbDEYes -> sharedModel.setIsDailyExposure(true)
                R.id.rbDENo -> sharedModel.setIsDailyExposure(false)
            }
        }
        binding.rgSmoke.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbSmokeYes -> sharedModel.setIsSmoke(true)
                R.id.rbSmokeNo -> sharedModel.setIsSmoke(false)
            }
        }


        binding.btnExport.setOnClickListener {
            val alcohol = when (binding.rgAlcohol.checkedRadioButtonId) {
                R.id.rbAlcohol1 -> {
                    "0-1"
                }

                R.id.rbAlcohol2 -> "2-5"
                R.id.rbAlcohol3 -> "5+"
                else -> "0-1"
            }
            sharedModel.setAlcohol(alcohol)
            sharedModel.exportJson()
        }
    }

}