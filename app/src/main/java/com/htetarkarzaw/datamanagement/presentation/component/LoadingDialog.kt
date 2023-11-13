package com.htetarkarzaw.datamanagement.presentation.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.htetarkarzaw.datamanagement.databinding.DialogLoadingBinding

class LoadingDialog(
    context: Context
) : AlertDialog(context) {

    private var _binding: DialogLoadingBinding? = null
    val binding get() = _binding!!
    private val messageTextView: TextView

    init {
        _binding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
        setView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        messageTextView = binding.tvLoading
    }

    override fun setMessage(message: CharSequence?) {
        this.messageTextView.text = message.toString()
    }


}