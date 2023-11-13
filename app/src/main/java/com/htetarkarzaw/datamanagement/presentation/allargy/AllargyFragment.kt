package com.htetarkarzaw.datamanagement.presentation.allargy

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.htetarkarzaw.datamanagement.MainViewModel
import com.htetarkarzaw.datamanagement.R
import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.databinding.FragmentAllargyBinding
import com.htetarkarzaw.datamanagement.domain.vo.AllergyVO
import com.htetarkarzaw.datamanagement.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AllargyFragment : BaseFragment<FragmentAllargyBinding>(FragmentAllargyBinding::inflate) {

    private val sharedModel: MainViewModel by activityViewModels()
    private val viewModel: AllergyViewModel by viewModels()
    private val allergyAdapter by lazy {
        AllergyAdapter {
            viewModel.addAllergy(it)
        }
    }

    override fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allergies.collectLatest {
                hideLoadingDialog()
                when (it) {
                    is Resource.Error -> {
                        errorDialog.setUpDialog(it.message ?: "Somethings wrong!", isRetry = true) {
                            viewModel.getAllergies()
                            errorDialog.dismiss()
                        }
                    }

                    is Resource.Loading -> {
                        showLoadingDialog("Loading...")
                    }

                    is Resource.Nothing -> {
                    }

                    is Resource.Success -> {
                        allergyAdapter.submitList(it.data)
                        Log.e("diets->", "${it.data?.size}")
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedAllergies.collectLatest {
                setUpChip(it)
            }
        }
    }

    private fun setUpChip(data: List<AllergyVO>?) {
        binding.cgAllergies.removeAllViews()
        if (!data.isNullOrEmpty()) {
            val checkedColor = ContextCompat.getColor(requireContext(), R.color.colorNavyBlue)
            val uncheckedColor =
                ContextCompat.getColor(requireContext(), R.color.colorPrimaryVariant)
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf(-android.R.attr.state_checked)
                ), intArrayOf(
                    checkedColor,
                    uncheckedColor
                )
            )
            data.map {
                val chip = Chip(requireContext())
                chip.id = it.id
                chip.text = it.name
                chip.checkedIcon = null
                chip.chipBackgroundColor = colorStateList
                chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                chip.setOnClickListener { _ ->
                    viewModel.removeAllergy(it)
                }
                binding.cgAllergies.addView(chip)
            }
        }
    }

    override fun initUi() {
        binding.rvAllergy.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvAllergy.adapter = allergyAdapter
        binding.rvAllergy.setHasFixedSize(false)
        binding.btnNext.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                sharedModel.setAllergies(viewModel.selectedAllergies.first().toMutableList())
                withContext(Dispatchers.Main) {
                    findNavController().navigate(R.id.action_allargyFragment_to_alcoholFragment)
                }
            }
        }
        binding.btnBack.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                sharedModel.setAllergies(viewModel.selectedAllergies.first().toMutableList())
                withContext(Dispatchers.Main) {
                    findNavController().popBackStack()
                }
            }
        }
        binding.edAllergy.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                if (binding.edAllergy.text.isNotEmpty()) {
                    viewModel.addNewAllergy(binding.edAllergy.text.toString())
                    binding.edAllergy.text.clear()
                }
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.edAllergy.windowToken, 0)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

}