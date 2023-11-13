package com.htetarkarzaw.datamanagement.presentation.health_concerns

import android.content.res.ColorStateList
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.htetarkarzaw.datamanagement.MainViewModel
import com.htetarkarzaw.datamanagement.R
import com.htetarkarzaw.datamanagement.data.Resource
import com.htetarkarzaw.datamanagement.databinding.FragmentHealthConcernsBinding
import com.htetarkarzaw.datamanagement.domain.vo.HealthConcernVO
import com.htetarkarzaw.datamanagement.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class HealthConcernsFragment :
    BaseFragment<FragmentHealthConcernsBinding>(FragmentHealthConcernsBinding::inflate) {
    private val sharedModel: MainViewModel by activityViewModels()
    private val viewModel: HealthConcernsViewModel by viewModels()
    private var mItemTouchHelper: ItemTouchHelper? = null
    private var checkedChipCount = 0
    private val hcAdapter by lazy {
        HCAdapter(onDragState = {
            it.let {
                mItemTouchHelper?.startDrag(it)
            }
        }, onItemReorder = { fromPosition, toPosition ->
            viewModel.changePosition(fromPosition, toPosition)
        })


    }

    override fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.healthConcerns.collectLatest {
                hideLoadingDialog()
                when (it) {
                    is Resource.Error -> {
                        errorDialog.setUpDialog(it.message ?: "Somethings wrong!", isRetry = true) {
                            viewModel.getHealthConcerns()
                            errorDialog.dismiss()
                        }
                    }

                    is Resource.Loading -> {
                        showLoadingDialog("Loading...")
                    }

                    is Resource.Nothing -> {
                    }

                    is Resource.Success -> {
                        setUpChip(it.data)
                        Log.e("HealthC->", "${it.data?.size}")
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedHC.collectLatest { list ->
                hcAdapter.submitList(list)
                Log.e("HHHHHH->", "${list.map { it.priority }}")
            }
        }
    }

    private fun setUpChip(data: List<HealthConcernVO>?) {
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
                chip.isCheckable = true
                chip.checkedIcon = null
                chip.chipBackgroundColor = colorStateList
                chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // If the checked chip count is less than the maximum, allow the chip to be checked
                        if (checkedChipCount < 5) {
                            checkedChipCount++
                            viewModel.selectHealthConcerns(it)
                        } else {
                            // If the maximum checkable chips are reached, uncheck the chip
                            chip.isChecked = false
                        }
                    } else {
                        // If a chip is unchecked, decrement the checked chip count
                        checkedChipCount--
                        viewModel.unselectHealthConcern(it)
                    }
                }
                binding.cgHealthConcern.addView(chip)
            }
        }
    }

    override fun initUi() {
        binding.rvHealthC.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvHealthC.adapter = hcAdapter
        binding.rvHealthC.setHasFixedSize(false)

        val callback: ItemTouchHelper.Callback = ReorderHelperCallBack(hcAdapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper?.attachToRecyclerView(binding.rvHealthC)
        binding.btnNext.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                sharedModel.setHealthConcerns(
                    viewModel.selectedHC.first().toMutableList()
                )
                withContext(Dispatchers.Main) {
                    findNavController().navigate(R.id.action_fragmentHealthConcerns_to_fragmentDiet)
                }
            }
        }
        binding.btnBack.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                sharedModel.setHealthConcerns(
                    viewModel.selectedHC.first().toMutableList()
                )
                withContext(Dispatchers.Main) {
                    findNavController().popBackStack()
                }
            }
        }
    }
}