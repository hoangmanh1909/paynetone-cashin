package com.paynetone.counter.functions.phone_recharge_card

import com.core.base.viper.interfaces.IInteractor
import com.core.base.viper.interfaces.IPresenter
import com.core.base.viper.interfaces.PresentView
import com.paynetone.counter.model.SimpleResult
import com.paynetone.counter.model.request.BaseRequest
import com.paynetone.counter.model.request.TopupAddressRequest
import com.paynetone.counter.network.CommonCallback

class PhoneRechareCardContract {

    interface Interactor : IInteractor<Presenter> {
    }

    interface View : PresentView<Presenter> {


    }

    interface Presenter : IPresenter<View, Interactor> {

        fun urlTopUpAddress():String

    }
}