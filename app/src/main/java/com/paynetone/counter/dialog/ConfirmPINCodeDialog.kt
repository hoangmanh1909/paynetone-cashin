package com.paynetone.counter.dialog

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.view.ActionMode
import com.paynetone.counter.R
import com.paynetone.counter.databinding.DialogConfirmPinCodeBinding
import com.paynetone.counter.utils.InputFilterCharacterNumber
import com.paynetone.counter.utils.Toast
import com.paynetone.counter.utils.disableCopyPaste
import com.paynetone.counter.utils.setSingleClick
import java.io.File


class ConfirmPINCodeDialog(val itemClick : (String) -> Unit) : BaseDialogFragment<DialogConfirmPinCodeBinding>() {

    override fun initView() {
        binding.apply {
            edtPinCode.disableCopyPaste()
            initViewPINCode()
            btnOk.setSingleClick {
                if (edtPinCode.text.toString().isEmpty()){
                    Toast.showToast(requireContext(),"Vui lòng nhập mã PIN")
                    return@setSingleClick
                }
                if (edtPinCode.text.toString().length != 4 ){
                    Toast.showToast(requireContext(),"Mã PIN không được bỏ trống!")
                    return@setSingleClick
                }
                itemClick(edtPinCode.text.toString())
                dismiss()
            }
            btnCancel.setSingleClick {
                dismiss()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun initStyle(): Int = R.style.DialogStyle

    override fun initCancelable(): Boolean = false

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) = DialogConfirmPinCodeBinding.inflate(inflater,container,false)
    
    private fun initViewPINCode(){
        binding.apply {
            val filters = arrayOfNulls<InputFilter>(2)
            filters[0] = InputFilter.LengthFilter(4)
            filters[1] = InputFilterCharacterNumber()
            edtPinCode.filters = filters
        }
    }


}