package com.paynetone.counter.functions.service

import com.core.base.viper.interfaces.IInteractor
import com.core.base.viper.interfaces.IPresenter
import com.core.base.viper.interfaces.PresentView
import com.paynetone.counter.forgotpassword.requestotp.RequestOTPContract
import com.paynetone.counter.model.SimpleResult
import com.paynetone.counter.model.request.PINAddRequest
import com.paynetone.counter.model.request.TopupAddressRequest
import com.paynetone.counter.network.CommonCallback

class ServiceContract {

    interface Interactor : IInteractor<Presenter> {
        fun topUpAddress(request: TopupAddressRequest, callback: CommonCallback<SimpleResult>?)
        fun requestVerifyPinCode(request:PINAddRequest,callback: CommonCallback<SimpleResult>?)
    }

    interface View : PresentView<Presenter> {
        fun requestTopUpAddressSuccess(url:String)
        fun requestVerifyPINCodeSuccess()
        fun requestError(message:String)

    }

    interface Presenter : IPresenter<View,Interactor> {
        fun topUpAddress(request: TopupAddressRequest)
        fun requestVerifyPinCode(request:PINAddRequest)
    }
}