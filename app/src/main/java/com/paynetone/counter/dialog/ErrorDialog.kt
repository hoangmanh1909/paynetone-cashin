package com.paynetone.counter.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.paynetone.counter.R
import com.paynetone.counter.databinding.DevelopDialogBinding
import com.paynetone.counter.databinding.ErrorDialogBinding
import com.paynetone.counter.utils.autoCleared
import com.paynetone.counter.utils.setSingleClick

class ErrorDialog(val message:String) : BaseDialogFragment<ErrorDialogBinding>() {

    override fun initView(){
        binding.apply {
            tvMessage.text = message
            btnClose.setSingleClick {
                dismiss()
            }
        }
    }

    override fun initStyle(): Int = R.style.DialogStyle

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) = ErrorDialogBinding.inflate(inflater,container,false)
}