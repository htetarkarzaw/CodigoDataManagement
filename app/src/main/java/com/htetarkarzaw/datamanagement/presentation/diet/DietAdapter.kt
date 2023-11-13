package com.htetarkarzaw.datamanagement.presentation.diet

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.htetarkarzaw.datamanagement.R
import com.htetarkarzaw.datamanagement.databinding.ViewHolderDietBinding
import com.htetarkarzaw.datamanagement.domain.vo.DietVO
import com.skydoves.balloon.createBalloon

@Suppress("DEPRECATION")
class DietAdapter(
    private val onItemClick: (DietVO) -> Unit,
) :
    ListAdapter<DietVO, RecyclerView.ViewHolder>(dietDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ViewHolderDietBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return DietViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DietViewHolder).bind(getItem(position))
    }

    companion object {
        val dietDiffUtil = object : DiffUtil.ItemCallback<DietVO>() {
            override fun areItemsTheSame(
                oldItem: DietVO,
                newItem: DietVO
            ): Boolean {
                return oldItem.id == newItem.id && oldItem.isSelected == newItem.isSelected
            }

            override fun areContentsTheSame(
                oldItem: DietVO,
                newItem: DietVO
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class DietViewHolder(private val binding: ViewHolderDietBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DietVO) {
            val balloon = createBalloon(itemView.context) {
                setText(item.tool_tip)
                setBackgroundColor(Color.WHITE)
                setSize(200, 100)
                setArrowPosition(0.1f)
                setTextColor(ContextCompat.getColor(itemView.context, R.color.colorNavyBlue))
            }
            binding.cbDiet.isChecked = item.isSelected
            binding.cbDiet.text = item.name
            binding.clickCheckBox.setOnClickListener {
                onItemClick(item.copy(isSelected = !item.isSelected))
            }
            binding.ivTip.setOnClickListener {
                if (balloon.isShowing) {
                    balloon.dismiss()
                } else {
                    balloon.showAlignTop(binding.ivTip, xOff = 200)
                }
            }
        }
    }
}