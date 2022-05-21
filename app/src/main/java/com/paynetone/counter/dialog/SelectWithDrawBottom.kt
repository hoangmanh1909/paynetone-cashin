package com.paynetone.counter.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.paynetone.counter.R
import com.paynetone.counter.databinding.BottomWithDrawBinding
import com.paynetone.counter.utils.BindingAdapter.Companion.setShowOrHideDrawable
import com.paynetone.counter.utils.ExtraConst.Companion.EXTRA_IS_WITH_DRAW_BANK
import com.paynetone.counter.utils.autoCleared
import com.paynetone.counter.utils.setSingleClick

class SelectWithDrawBottom(isSelectWithDrawBank: Boolean) : BottomSheetDialogFragment() {

    private var binding: BottomWithDrawBinding by autoCleared()
    private lateinit var callBackListener: CallBackListener
    private var isSelectWithDrawBank:Boolean?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        arguments?.apply {
            isSelectWithDrawBank = getBoolean(EXTRA_IS_WITH_DRAW_BANK)
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations=R.style.MyDialogAnimation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomWithDrawBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView(){

        binding.apply {
            layoutBank.setSingleClick {
                callBackListener.onSelectWithDrawBank(true)
                icTickBank.setShowOrHideDrawable(true)
                icTickWallet.setShowOrHideDrawable(false)
            }
            layoutWallet.setSingleClick {
                callBackListener.onSelectWithDrawBank(false)
                icTickBank.setShowOrHideDrawable(false)
                icTickWallet.setShowOrHideDrawable(true)
            }
            isSelectWithDrawBank?.let {
                if (it) icTickBank.setShowOrHideDrawable(true)
                else icTickWallet.setShowOrHideDrawable(true)
            }
        }
    }

    fun onCallBackListener(callBackListener: CallBackListener){
        this.callBackListener=callBackListener
    }

    interface CallBackListener{
        fun onSelectWithDrawBank(isSelectWithDrawBank:Boolean)
    }
    companion object{
        @JvmStatic
        fun getInstance(isSelectWithDrawBank:Boolean) = SelectWithDrawBottom(isSelectWithDrawBank).apply {
            arguments = Bundle().apply {
                putBoolean(EXTRA_IS_WITH_DRAW_BANK,isSelectWithDrawBank)
            }
        }
    }
}