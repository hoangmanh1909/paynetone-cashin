package com.paynetone.counter.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.paynetone.counter.R

import com.paynetone.counter.databinding.NotifyDialogBinding
import com.paynetone.counter.utils.setSingleClick

class NotifyDialog(val message:String) : BaseDialogFragment<NotifyDialogBinding>() {

    override fun initView() {
        binding.apply {
            tvMessage.text = message
            btnClose.setSingleClick {
                dismiss()
            }
        }
    }

    override fun initStyle(): Int = R.style.DialogStyle

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        NotifyDialogBinding.inflate(inflater, container, false)
}