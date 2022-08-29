package com.paynetone.counter.functions.phone_recharge_card

import android.app.Activity
import android.util.Log
import com.core.base.viper.Presenter
import com.core.base.viper.interfaces.ContainerView
import com.google.gson.reflect.TypeToken
import com.paynetone.counter.functions.service.ServiceContract
import com.paynetone.counter.functions.service.ServiceFragment
import com.paynetone.counter.functions.service.ServiceInteractor
import com.paynetone.counter.model.PaymentModel
import com.paynetone.counter.model.SimpleResult
import com.paynetone.counter.model.request.TopupAddressRequest
import com.paynetone.counter.model.response.TopUpAddressResponse
import com.paynetone.counter.network.CommonCallback
import com.paynetone.counter.network.NetWorkController
import com.paynetone.counter.utils.SharedPref
import com.paynetone.counter.utils.Toast
import retrofit2.Call
import retrofit2.Response

class PhoneRechareCardPresenter(containerView: ContainerView,val url:String) :
    Presenter<PhoneRechareCardContract.View, PhoneRechareCardContract.Interactor>(containerView),
    PhoneRechareCardContract.Presenter {


    override fun start() {


    }

    override fun onCreateInteractor(): PhoneRechareCardContract.Interactor = PhoneRechareCardInteractor(this)

    override fun onCreateView(): PhoneRechareCardContract.View = PhoneRechareCardFragment.instance

    override fun urlTopUpAddress(): String  = url

}