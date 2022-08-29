package com.paynetone.counter.functions.han_muc.bank

import android.util.Log
import com.core.base.viper.ViewFragment
import com.paynetone.counter.base.PaynetOneActivity
import com.paynetone.counter.utils.ExtraConst

class ListBankActivity : PaynetOneActivity() {
    override fun onCreateFirstFragment(): ViewFragment<*> {

        return ListBankPresenter(this).fragment as ViewFragment<*>
    }
}