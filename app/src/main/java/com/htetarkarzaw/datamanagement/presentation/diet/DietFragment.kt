package com.htetarkarzaw.datamanagement.presentation.diet

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.htetarkarzaw.datamanagement.MainViewModel
import com.htetarkarzaw.datamanagement.R
import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.databinding.FragmentDietBinding
import com.htetarkarzaw.datamanagement.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DietFragment : BaseFragment<FragmentDietBinding>(FragmentDietBinding::inflate) {
    private val sharedModel: MainViewModel by activityViewModels()
    private val viewModel: DietViewModel by viewModels()
    private val dietAdapter by lazy {
        DietAdapter {
            viewModel.toggleDiet(it)
        }
    }

    override fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.diets.collectLatest {
                hideLoadingDialog()
                when (it) {
                    is Resource.Error -> {
                        errorDialog.setUpDialog(it.message ?: "Somethings wrong!", isRetry = true) {
                            viewModel.getDiets()
                            errorDialog.dismiss()
                        }
                    }

                    is Resource.Loading -> {
                        showLoadingDialog("Loading...")
                    }

                    is Resource.Nothing -> {
                    }

                    is Resource.Success -> {
                        dietAdapter.submitList(it.data)
                        Log.e("diets->", "${it.data?.size}")
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedDiet.collectLatest {
                binding.cbNone.isChecked = (it.isEmpty())
            }
        }
    }

    override fun initUi() {
        binding.rvDiet.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvDiet.adapter = dietAdapter
        binding.rvDiet.setHasFixedSize(false)
        binding.btnNext.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val list = viewModel.selectedDiet.first().toMutableList()
                sharedModel.setDiets(list)
                withContext(Dispatchers.Main) {
                    findNavController().navigate(R.id.action_fragmentDiet_to_allargyFragment)
                }

            }
        }
        binding.btnBack.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val list = viewModel.selectedDiet.first().toMutableList()
                sharedModel.setDiets(list)
                withContext(Dispatchers.Main) {
                    findNavController().popBackStack()
                }

            }
        }
        binding.cbNone.setOnClickListener() {
            viewModel.clearAllDiet()
        }
    }

}