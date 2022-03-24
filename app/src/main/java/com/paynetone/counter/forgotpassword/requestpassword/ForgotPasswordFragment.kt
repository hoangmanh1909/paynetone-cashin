package com.paynetone.counter.forgotpassword.requestpassword

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.core.base.viper.ViewFragment
import com.paynetone.counter.R
import com.paynetone.counter.databinding.FragmentForgotPasswordBinding
import com.paynetone.counter.forgotpassword.requestotp.RequestOTPActivity
import com.paynetone.counter.login.LoginActivity
import com.paynetone.counter.model.request.UpdatePasswordRequest
import com.paynetone.counter.utils.Constants
import com.paynetone.counter.utils.Toast
import com.paynetone.counter.utils.hideKeyboard
import com.paynetone.counter.utils.setSingleClick
import org.apache.commons.lang3.StringUtils
import java.util.concurrent.TimeUnit

class ForgotPasswordFragment : ViewFragment<ForgotPasswordContract.Presenter>(), ForgotPasswordContract.View {

    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var countDownTimer: CountDownTimer
    private val timer by lazy { (3 * 60 * 1000).toLong() }

    companion object {
        val instance: ForgotPasswordFragment
            get() = ForgotPasswordFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_forgot_password

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),layoutId,container,false)
        binding.lifecycleOwner=this
        initView()
        return binding.root
    }

    private fun initView(){
        countDownOTP(timer)
        onListener()
    }

    private fun onListener(){
        binding.apply {
            btnSendRequest.setSingleClick {
//                mPresenter.updatePassword()
                if (confirmInput()){
                    val phoneNumber = edtMobileNumber.text.toString().trim()
                    val password = edtPassword.text.toString().trim()
                    val otp = edtOtp.text.toString().trim()
                    mPresenter.updatePassword(UpdatePasswordRequest(phoneNumber,password,Constants.TYPE_GET_OTP_REGISTER_ACCOUNT,otp))
                }
            }
            tvLogin.setSingleClick {
                activity?.finish()
                countDownTimer.onFinish()
            }
            rootView.setSingleClick {
                it.hideKeyboard()
            }

        }
    }
    override fun showError(message: String?) {
    }

    override fun updatePasswordSuccess() {
        Toast.showToast(requireContext(),resources.getString(R.string.str_change_password_success))
        countDownTimer.onFinish()
        startActivity(Intent(requireActivity(), LoginActivity::class.java))
    }

    private fun countDownOTP(timer: Long) {
        countDownTimer = object : CountDownTimer(timer, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                var millisUntilFinished = millisUntilFinished
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                val textTime = " ${resources.getString(R.string.str_opt_zalo_fragment_forgot_password)} <font color='#03A9F4'>${StringUtils.leftPad(minutes.toString(), 2, '0') + ":" + StringUtils.leftPad(seconds.toString(), 2, '0')}</font>"
                binding.tvZalo.setText(Html.fromHtml(textTime), TextView.BufferType.SPANNABLE)
            }

            override fun onFinish() {
                cancel()
                activity?.finish()
            }
        }
        countDownTimer.start()
    }
    private fun validatePhoneNumber(): Boolean {
        val email = binding.edtMobileNumber.text.toString().trim()
        if (email.isBlank()) {
            Toast.showToast(requireContext(),resources.getString(R.string.str_not_enter_phone_number))
            return false
        }
        return true
    }
    private fun validatePassword(): Boolean {
        val password = binding.edtPassword.text.toString().trim()
        if (password.isBlank()) {
            Toast.showToast(requireContext(),resources.getString(R.string.str_not_enter_password))
            return false
        }
        return true

    }
    private fun validateConfirmPassword(): Boolean {
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()
        if (confirmPassword.isBlank()) {
            Toast.showToast(requireContext(),resources.getString(R.string.str_not_enter_confirm_password))
            return false
        }
        return true
    }
    private fun validateNotMatchPassword() : Boolean{
        val password = binding.edtPassword.text.toString().trim()
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()
        if (password != confirmPassword){
            Toast.showToast(requireContext(),resources.getString(R.string.str_not_match_password))
            return false
        }
        return true
    }
    private fun validateOTP():Boolean{
        val otp = binding.edtOtp.text.toString().trim()
        if (otp.isBlank()){
            Toast.showToast(requireContext(),resources.getString(R.string.str_not_enter_otp))
            return false
        }
        return true
    }
    private fun confirmInput(): Boolean {
        if (!validatePhoneNumber() || !validatePassword() || !validateConfirmPassword() || !validateNotMatchPassword() ||!validateOTP()) {
            return false
        }
        return true
    }


}