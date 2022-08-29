package com.paynetone.counter.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.paynetone.counter.R
import com.paynetone.counter.adapter.SupportVietQrAdapter
import com.paynetone.counter.databinding.SuccessPaymentDialogBinding
import com.paynetone.counter.databinding.SupportVietQrDialogBinding
import com.paynetone.counter.utils.Constants
import com.paynetone.counter.utils.MarginDecoration
import com.paynetone.counter.utils.autoCleared
import com.paynetone.counter.utils.setSingleClick

class SupportVietQrDialog(private val typeSupport:Int) : BaseDialogFragment<SupportVietQrDialogBinding>() {

    private val adapter by lazy { SupportVietQrAdapter(requireContext(),typeSupport) }

    override fun initStyle(): Int = R.style.DialogStyleVietQr

    override fun initCancelable(): Boolean = true

    override fun initView(){
        binding.apply {
            recycleView.apply {
                adapter = this@SupportVietQrDialog.adapter
                addItemDecoration(MarginDecoration(10,4))
                this@SupportVietQrDialog.adapter.submitList()
            }
            btnClose.setSingleClick {
                dismiss()
            }
            if (typeSupport == Constants.PAYMENT_TYPE_VIETQR){
                tvTitle.text = "Các ứng dụng hỗ trợ VIETQR"
            }else{
                tvTitle.text = "Các ứng dụng hỗ trợ VNPAY"
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = SupportVietQrDialogBinding.inflate(inflater,container,false)
}