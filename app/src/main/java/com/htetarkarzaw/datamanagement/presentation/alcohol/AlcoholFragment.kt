package com.htetarkarzaw.datamanagement.presentation.alcohol

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.htetarkarzaw.datamanagement.MainViewModel
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
                        it.data?.let { it1 -> successDialog.setUpDialog(it1) }
                    }
                }
            }
        }
    }

    override fun initUi() {
        binding.btnExport.setOnClickListener {
            sharedModel.exportJson()
        }
    }

}