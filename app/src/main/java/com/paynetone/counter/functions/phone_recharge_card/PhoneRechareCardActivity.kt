package com.paynetone.counter.functions.phone_recharge_card

import com.core.base.viper.ViewFragment
import com.core.base.viper.interfaces.IPresenter
import com.paynetone.counter.base.PaynetOneActivity
import com.paynetone.counter.forgotpassword.requestpassword.ForgotPasswordPresenter
import com.paynetone.counter.utils.ExtraConst
import com.paynetone.counter.utils.ExtraConst.Companion.EXTRA_URL_TOPUP_ADDRESS

class PhoneRechareCardActivity : PaynetOneActivity() {
    override fun onCreateFirstFragment(): ViewFragment<out IPresenter<*, *>> {
        return PhoneRechareCardPresenter(this,intent.getStringExtra(EXTRA_URL_TOPUP_ADDRESS) ?: "").fragment as ViewFragment<*>
    }
}