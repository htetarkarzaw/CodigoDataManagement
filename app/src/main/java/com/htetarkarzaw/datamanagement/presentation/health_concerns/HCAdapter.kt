package com.htetarkarzaw.datamanagement.presentation.health_concerns

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.htetarkarzaw.datamanagement.databinding.ViewHolderHealthConcernBinding
import com.htetarkarzaw.datamanagement.domain.vo.HealthConcernVO

@Suppress("DEPRECATION")
class HCAdapter(
    private val onDragState: (viewHolder: RecyclerView.ViewHolder) -> Unit,
    private val onItemReorder: (fromPosition: Int, toPosition: Int) -> Unit
) :
    ListAdapter<HealthConcernVO, RecyclerView.ViewHolder>(hcDiffUtil), ItemTouchHelperAdapter {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ViewHolderHealthConcernBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HCViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HCViewHolder).bind(getItem(position))
    }

    companion object {
        val hcDiffUtil = object : DiffUtil.ItemCallback<HealthConcernVO>() {
            override fun areItemsTheSame(
                oldItem: HealthConcernVO,
                newItem: HealthConcernVO
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: HealthConcernVO,
                newItem: HealthConcernVO
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class HCViewHolder(private val binding: ViewHolderHealthConcernBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: HealthConcernVO) {
            binding.tvTitle.text = item.name
            binding.root.setOnTouchListener { v, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onDragState(this@HCViewHolder)
                }
                false
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        onItemReorder(fromPosition, toPosition)
        return true
    }
}