package com.htetarkarzaw.datamanagement.presentation.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.htetarkarzaw.datamanagement.presentation.component.ErrorDialog
import com.htetarkarzaw.datamanagement.presentation.component.LoadingDialog
import com.htetarkarzaw.datamanagement.presentation.component.SuccessDialog
import com.htetarkarzaw.datamanagement.utils.InflateFragment
abstract class BaseFragment<VB : ViewBinding>(private val inflate: InflateFragment<VB>) :
    Fragment() {
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireContext()) }
    val errorDialog: ErrorDialog by lazy { ErrorDialog(requireContext()) }
    val successDialog: SuccessDialog by lazy { SuccessDialog(requireContext()) }
    private var _binding: VB? = null
    val binding get() = _binding!!
    abstract fun observe()
    abstract fun initUi()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observe()
    }

    fun showLoadingDialog(text: String) {
        loadingDialog.apply {
            setMessage(text)
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            show()
        }
    }

    fun hideLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    open fun hideSoftKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken,
                0
            )
        }
    }
}