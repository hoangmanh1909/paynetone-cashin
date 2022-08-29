package com.paynetone.counter.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.paynetone.counter.R
import com.paynetone.counter.databinding.GuideRechargeDialogBinding
import com.paynetone.counter.databinding.NapHanMucDialogBinding
import com.paynetone.counter.utils.autoCleared
import com.paynetone.counter.utils.setSingleClick

class GuideRechargeDialog(val mContext: Context, val code:String) : BaseDialogFragment<GuideRechargeDialogBinding>() {

    override fun initView() {
        binding.apply {
            btnConfirm.setSingleClick {
                dismiss()
            }
            tvContent1.text = "${mContext.getText(R.string.str_content_guide1)} $code"

        }

    }
    override fun initStyle(): Int = R.style.DialogStyle

    override fun initCancelable(): Boolean = false

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) = GuideRechargeDialogBinding .inflate(inflater,container,false)

}