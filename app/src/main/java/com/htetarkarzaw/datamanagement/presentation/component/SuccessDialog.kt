package com.htetarkarzaw.datamanagement.presentation.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.htetarkarzaw.datamanagement.databinding.DialogSuccessBinding

class SuccessDialog(
    context: Context
) : AlertDialog(context) {

    private var _binding: DialogSuccessBinding? = null
    val binding get() = _binding!!

    init {
        _binding = DialogSuccessBinding.inflate(LayoutInflater.from(context))
        setView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
    }

    fun setUpDialog(message: String,onClickOkay:()->Unit) {
        binding.tvMessage.text = message
        binding.btnOkay.setOnClickListener {
            onClickOkay()
            dismiss()
        }
        show()
    }
}