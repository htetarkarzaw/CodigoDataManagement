package com.htetarkarzaw.datamanagement.presentation.alcohol

import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.htetarkarzaw.datamanagement.MainViewModel
import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.databinding.FragmentAlcoholBinding
import com.htetarkarzaw.datamanagement.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class AlcoholFragment : BaseFragment<FragmentAlcoholBinding>(FragmentAlcoholBinding::inflate) {
    private val sharedModel: MainViewModel by activityViewModels()

    private val permissions =
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    private val permissionForJsonFile =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            val readPermission = it[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
            val writePermission = it[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false
            if (readPermission && writePermission) {
                createJsonFile()
            }
        }

    private fun createJsonFile() {
        val filesDir = requireContext().filesDir
        val jsonFile = File(filesDir, "simple_output.json").apply {
            createNewFile()
            deleteOnExit()
        }
        sharedModel.exportJson(jsonFile)
    }

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
            permissionForJsonFile.launch(permissions)
        }
    }

}