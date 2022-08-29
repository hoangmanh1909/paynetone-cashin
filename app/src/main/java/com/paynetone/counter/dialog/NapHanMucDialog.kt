package com.paynetone.counter.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.paynetone.counter.R
import com.paynetone.counter.databinding.NapHanMucDialogBinding
import com.paynetone.counter.enumClass.StateNotify
import com.paynetone.counter.enumClass.StateView
import com.paynetone.counter.functions.han_muc.bank.ListBankActivity
import com.paynetone.counter.functions.withdraw.WithDrawActivity
import com.paynetone.counter.model.PaynetGetBalanceByIdResponse
import com.paynetone.counter.model.SelectWithDraw
import com.paynetone.counter.model.response.PaynetGetByParentResponse
import com.paynetone.counter.observer.DisplayElement
import com.paynetone.counter.observer.Observer
import com.paynetone.counter.observer.SateViewData
import com.paynetone.counter.observer.StateNotifyData
import com.paynetone.counter.utils.*

class NapHanMucDialog(val mContext: Context,
                      val content:String,
                      val code:String,
                      private val amountOutWard:Long,
                      private val payNetGetBalance:PaynetGetBalanceByIdResponse?=null) :
    BaseDialogFragment<NapHanMucDialogBinding>(), DisplayElement<Any>, Observer<Any> {

    override fun initView() {
        binding.apply {
            tvHanMuc.text = content
            if (SharedPref.getInstance(requireContext()).isMerchantAdmin){
                layoutHanMucDoiSoat.visibility = View.VISIBLE
            }
            ivBack.setSingleClick {
                dismiss()
            }
            tvGuide.setSingleClick {
                GuideRechargeDialog(requireContext(),code).show(childFragmentManager,"GuideRechargeDialog")
            }
            layoutBank.setSingleClick {
                val intent = Intent(requireContext(),ListBankActivity::class.java)
                intent.putExtra(ExtraConst.EXTRA_CODE , code)

                startActivity(intent)
            }
            layoutWallet.setSingleClick {
                DevelopDialog(requireContext()).show(childFragmentManager,"NapHanMucDialog")
            }
            layoutHanMucDoiSoat.setSingleClick {
                val intent = Intent(requireActivity(), WithDrawActivity::class.java)
                intent.apply {
                    putExtra(Constants.AMOUNT_OUTWARD, amountOutWard )
                    putExtra(ExtraConst.EXTRA_WITH_DRAW,SelectWithDraw.HAN_MUC)
                    payNetGetBalance?.let {
                        putExtra(ExtraConst.EXTRA_PAYNET_GET_BALANCE_BY_ID,it)
                    }
                }


                startActivity(intent)
            }
        }

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) = NapHanMucDialogBinding.inflate(inflater,container,false)

    override fun display(data: Any) {
        if (data is SateViewData){
            data.also {  stateViewData ->
                when(stateViewData.state){
                    StateView.GONE ->{
                       dismissAllowingStateLoss()
                    }
                    else->{}
                }
            }
        }
    }

    override fun update(data: Any) {
        display(data)
    }

    override fun onStart() {
        super.onStart()
        SateViewData.registerObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        SateViewData.removeObserver(this)
    }

}