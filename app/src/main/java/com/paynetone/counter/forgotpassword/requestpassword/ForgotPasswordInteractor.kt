package com.paynetone.counter.forgotpassword.requestpassword

import com.core.base.viper.Interactor
import com.paynetone.counter.model.SimpleResult
import com.paynetone.counter.model.request.UpdatePasswordRequest
import com.paynetone.counter.network.CommonCallback
import com.paynetone.counter.network.NetWorkController

internal class ForgotPasswordInteractor(presenter: ForgotPasswordContract.Presenter) : Interactor<ForgotPasswordContract.Presenter>(presenter),
    ForgotPasswordContract.Interactor {
    override fun updatePassword(request: UpdatePasswordRequest, callback: CommonCallback<SimpleResult>) {
        NetWorkController.updatePassword(request,callback)
    }


}