package com.paynetone.counter.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import com.paynetone.counter.R
import com.paynetone.counter.databinding.ConfirmDialogBinding
import com.paynetone.counter.utils.autoCleared
import com.paynetone.counter.utils.setSingleClick

class ConfirmDialog(val mContext: Context, val message:String, private val callBackDialog: CallBackDialog) : BaseDialogFragment<ConfirmDialogBinding>() {

    override fun initView(){
        binding.apply {
            tvMessage.text = HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY)
            btnAgree.setSingleClick {
                callBackDialog.onAgreeClick()
                dismiss()
            }
            btnCancel.setSingleClick {
                dismiss()
            }

        }
    }

    interface CallBackDialog{
        fun onAgreeClick()
    }

    override fun initCancelable(): Boolean = true

    override fun initStyle(): Int = R.style.DialogStyle

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = ConfirmDialogBinding.inflate(inflater,container,false)


}