package com.htetarkarzaw.datamanagement.presentation.allargy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.htetarkarzaw.datamanagement.databinding.ViewHolderAllergyBinding
import com.htetarkarzaw.datamanagement.domain.vo.AllergyVO

@Suppress("DEPRECATION")
class AllergyAdapter(
    private val onItemClick: (AllergyVO) -> Unit,
) :
    ListAdapter<AllergyVO, RecyclerView.ViewHolder>(allergyDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ViewHolderAllergyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return AllergyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AllergyViewHolder).bind(getItem(position))
    }

    companion object {
        val allergyDiffUtil = object : DiffUtil.ItemCallback<AllergyVO>() {
            override fun areItemsTheSame(
                oldItem: AllergyVO,
                newItem: AllergyVO
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AllergyVO,
                newItem: AllergyVO
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class AllergyViewHolder(private val binding: ViewHolderAllergyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AllergyVO) {
            binding.tvTitle.text = item.name
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}