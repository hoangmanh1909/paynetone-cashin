package com.paynetone.counter.forgotpassword.requestpassword

import com.core.base.viper.interfaces.IInteractor
import com.core.base.viper.interfaces.IPresenter
import com.core.base.viper.interfaces.PresentView
import com.paynetone.counter.model.SimpleResult
import com.paynetone.counter.model.request.UpdatePasswordRequest
import com.paynetone.counter.network.CommonCallback

interface ForgotPasswordContract {
    interface Interactor : IInteractor<Presenter> {
        fun updatePassword(request:UpdatePasswordRequest, callback: CommonCallback<SimpleResult>)
    }

    interface View : PresentView<Presenter> {
        fun showError(message: String?)
        fun updatePasswordSuccess()
    }

    interface Presenter : IPresenter<View, Interactor> {

        fun updatePassword(request: UpdatePasswordRequest)

    }

}