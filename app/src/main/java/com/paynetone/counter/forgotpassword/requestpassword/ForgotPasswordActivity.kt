package com.paynetone.counter.forgotpassword.requestpassword

import android.util.Log
import com.core.base.viper.ViewFragment
import com.paynetone.counter.base.PaynetOneActivity
import com.paynetone.counter.forgotpassword.requestotp.RequestOTPPresenter
import com.paynetone.counter.utils.Constants.EXTRA_OPT

class ForgotPasswordActivity : PaynetOneActivity() {
    override fun onCreateFirstFragment(): ViewFragment<*> {
        return ForgotPasswordPresenter(this).fragment as ViewFragment<*>
    }
}