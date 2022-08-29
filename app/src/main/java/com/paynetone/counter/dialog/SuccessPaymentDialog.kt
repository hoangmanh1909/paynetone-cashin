package com.paynetone.counter.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.DialogFragment
import com.paynetone.counter.R
import com.paynetone.counter.databinding.SuccessPaymentDialogBinding
import com.paynetone.counter.utils.autoCleared
import com.paynetone.counter.utils.setSingleClick

class SuccessPaymentDialog(val mContext: Context,val message:String,val listener:CallBackListener) : BaseDialogFragment<SuccessPaymentDialogBinding>() {


    override fun initView(){
        binding.apply {
            btnClose.setSingleClick {
                dismiss()
                listener.onCloseClicked()
            }
            if (message.isNotEmpty()) tvMessage.text = message
        }
    }

    override fun initStyle(): Int  = R.style.DialogStyle

    override fun initCancelable(): Boolean  = true

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) =  SuccessPaymentDialogBinding.inflate(inflater,container,false)

    interface CallBackListener{
        fun onCloseClicked()
    }

}