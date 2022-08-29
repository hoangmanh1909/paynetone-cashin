package com.paynetone.counter.functions.phone_recharge_card

import com.core.base.viper.Interactor
import com.paynetone.counter.functions.service.ServiceContract
import com.paynetone.counter.model.SimpleResult
import com.paynetone.counter.model.request.TopupAddressRequest
import com.paynetone.counter.network.CommonCallback
import com.paynetone.counter.network.NetWorkController

class PhoneRechareCardInteractor(presenter: PhoneRechareCardContract.Presenter) : Interactor<PhoneRechareCardContract.Presenter>(presenter),
    PhoneRechareCardContract.Interactor {


}