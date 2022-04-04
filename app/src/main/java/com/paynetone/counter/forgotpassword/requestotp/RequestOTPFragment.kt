package com.paynetone.counter.forgotpassword.requestotp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.core.base.viper.ViewFragment
import com.paynetone.counter.R
import com.paynetone.counter.databinding.FragmentRequestOtpBinding
import com.paynetone.counter.forgotpassword.requestpassword.ForgotPasswordActivity
import com.paynetone.counter.utils.Constants.EXTRA_OPT
import com.paynetone.counter.utils.Constants.TYPE_GET_OTP_FORGOT_PASSWORD
import com.paynetone.counter.utils.Toast
import com.paynetone.counter.utils.hideKeyboard
import com.paynetone.counter.utils.setSingleClick
import kotlinx.android.synthetic.main.fragment_request_otp.*


class RequestOTPFragment : ViewFragment<RequestOTPContract.Presenter>(),RequestOTPContract.View {

    private lateinit var binding:FragmentRequestOtpBinding

    companion object {
        val instance: RequestOTPFragment
            get() = RequestOTPFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),layoutId,container,false)
        binding.lifecycleOwner=this
        initView()
        return binding.root
    }

    private fun initView(){
        onListener()
    }

    private fun onListener(){
        binding.apply {
            ivBack.setSingleClick {
                activity?.finish()
            }
            btnSendRequest.setSingleClick {
                val mobileNumber = edtMobileNumber.text.toString()
                if (mobileNumber.isBlank()){
                    Toast.showToast(requireContext(), resources.getString(R.string.str_did_not_phone_number))
                }
                mPresenter.getOTP(mobileNumber,TYPE_GET_OTP_FORGOT_PASSWORD)
            }
            tvLogin.setSingleClick {
                activity?.finish()
            }
            rootView.setSingleClick {
                it.hideKeyboard()
            }

        }
    }


    override fun getLayoutId(): Int  = R.layout.fragment_request_otp

    override fun showSuccess() {
    }

    override fun showError(message: String?) {
    }

    override fun showSuccessOTP() {
        startActivity(Intent(requireActivity(), ForgotPasswordActivity::class.java))
        activity?.finish()
    }

}