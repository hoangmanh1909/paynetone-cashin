package com.paynetone.counter.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.google.gson.reflect.TypeToken
import com.paynetone.counter.R
import com.paynetone.counter.adapter.PinCodeViewPager
import com.paynetone.counter.app.PaynetOneApplication
import com.paynetone.counter.databinding.DialogConfirmPasswordBinding
import com.paynetone.counter.databinding.DialogPinCodeBinding
import com.paynetone.counter.model.request.PINAddRequest
import com.paynetone.counter.model.response.AddPINCodeResponse
import com.paynetone.counter.network.NetWorkController
import com.paynetone.counter.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PinCodeDialog(val mContext: Context,val title:String,val password:String) : BaseDialogFragment<DialogPinCodeBinding>() {

    private val sharedPref by lazy { SharedPref.getInstance(mContext) }

    override fun loadControlsAndResize(binding: DialogPinCodeBinding) {
        binding.apply {
            initViewPINCode()
            tvTitle.text = title
            edtPinCode.isPasswordHidden = true
            edtPinCodeConfirm.isPasswordHidden = true
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }

    override fun initView(){
        binding.apply {
            ivBack.setSingleClick {
                dismiss()
            }
            buttonPasswordToggle.setSingleClick {
                edtPinCode.isPasswordHidden = !edtPinCode.isPasswordHidden
                if (edtPinCode.isPasswordHidden)
                    buttonPasswordToggle.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_hide_password))
                else
                    buttonPasswordToggle.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.ic_show_password))


            }
            buttonPasswordToggleConfirm.setSingleClick {
                edtPinCodeConfirm.isPasswordHidden = !edtPinCodeConfirm.isPasswordHidden
                if (edtPinCodeConfirm.isPasswordHidden)
                    buttonPasswordToggleConfirm.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_hide_password))
                else buttonPasswordToggleConfirm.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.ic_show_password))
            }
            btnSendRequest.setSingleClick {
                if (confirmInput()) requestPinCode()
            }
            rootView.setSingleClick {
                it.hideKeyboard()
            }
            edtPinCode.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) layoutPinCode.background =  ResourcesCompat.getDrawable(resources,R.drawable.bg_pin_code_selected,null)
                else layoutPinCode.background = ResourcesCompat.getDrawable(resources,R.drawable.bg_edt_forgot_password,null)
            }
            edtPinCodeConfirm.onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) layoutPinCodeConfirm.background =  ResourcesCompat.getDrawable(resources,R.drawable.bg_pin_code_selected,null)
                    else layoutPinCodeConfirm.background = ResourcesCompat.getDrawable(resources,R.drawable.bg_edt_forgot_password,null)
                }

        }
    }

    private fun validatePINCode():Boolean{
        val pinCode = binding.edtPinCode.text.toString()
        if (pinCode.isBlank()){
            Toast.showToast(requireContext(),resources.getString(R.string.str_not_enter_pin_code))
            return false
        }
        return true
    }
    private fun validatePINCodeConfirm():Boolean{
        val pinCode = binding.edtPinCodeConfirm.text.toString()
        if (pinCode.isBlank()){
            Toast.showToast(requireContext(),resources.getString(R.string.str_not_enter_pin_code))
            return false
        }
        return true
    }
    private fun validateNotMatchPinCode() : Boolean{
        val pinCode = binding.edtPinCode.text.toString()
        val pinCodeConfirm = binding.edtPinCodeConfirm.text.toString()
        if (pinCode != pinCodeConfirm){
            Toast.showToast(requireContext(),resources.getString(R.string.str_not_match_pin_code))
            return false
        }
        return true
    }

    private fun confirmInput(): Boolean {
        if (!validatePINCode() || !validatePINCodeConfirm() || !validateNotMatchPinCode()) {
            return false
        }
        return true
    }

    private fun requestPinCode(): Disposable {
        val empId = SharedPref.getInstance(requireContext()).employeeModel.id
        val pinCode = binding.edtPinCode.text.toString()
        val mobileNumber = SharedPref.getInstance(requireContext()).employeeModel.mobileNumber
        showProgressDialog()
        return NetWorkController.requestAddPinCode(PINAddRequest(empId ?: 0,pinCode,password,mobileNumber))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                hideProgressDialog()
                if (result.errorCode == "00"){
                    val response = NetWorkController.getGson().fromJson<AddPINCodeResponse>(result.data,object : TypeToken<AddPINCodeResponse>(){}.type)
                    response.pinCode?.let {
                        sharedPref.putString(PrefConst.PREF_IS_EXIST_PIN_CODE,it)
                    }
                    SuccessPaymentDialog(requireContext(),getString(R.string.str_setting_pin_code_success),object : SuccessPaymentDialog.CallBackListener{
                        override fun onCloseClicked() {
                            this@PinCodeDialog.dismiss()
                        }

                    }).show(childFragmentManager,"PinCodeDialog")
                }else{
                    Toast.showToast(requireContext(),result.message)
                }
            }) { throwable ->
                throwable.printStackTrace()
                hideProgressDialog()
            }
    }


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = DialogPinCodeBinding.inflate(inflater,container,false)

    private fun initViewPINCode(){
        binding.apply {
            val filters = arrayOfNulls<InputFilter>(2)
            filters[0] = InputFilter.LengthFilter(4)
            filters[1] = InputFilterCharacterNumber()
            edtPinCode.filters = filters
            edtPinCodeConfirm.filters = filters
        }
    }


}