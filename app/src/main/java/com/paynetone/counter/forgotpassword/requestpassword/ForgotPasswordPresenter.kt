package com.paynetone.counter.forgotpassword.requestpassword

import android.app.Activity
import android.util.Log
import com.core.base.viper.Presenter
import com.core.base.viper.interfaces.ContainerView
import com.paynetone.counter.model.SimpleResult
import com.paynetone.counter.model.request.UpdatePasswordRequest
import com.paynetone.counter.network.CommonCallback
import com.paynetone.counter.utils.Toast
import retrofit2.Call
import retrofit2.Response

class ForgotPasswordPresenter(containerView: ContainerView)  : Presenter<ForgotPasswordContract.View, ForgotPasswordContract.Interactor>(containerView),
    ForgotPasswordContract.Presenter {
    override fun updatePassword(request: UpdatePasswordRequest) {
        mView.showProgress()
        Log.e("TAG", "updatePassword: 1212", )
        mInteractor.updatePassword(request,object : CommonCallback<SimpleResult>(mContainerView as Activity) {
            override fun onSuccess(call: Call<SimpleResult>, response: Response<SimpleResult>) {
                super.onSuccess(call, response)
                if ("00" == response.body().errorCode) {
                    mView.updatePasswordSuccess()
                } else {
                    mView.showError(response.body().message)
                }
            }

            override fun onError(call: Call<SimpleResult>, message: String) {
                super.onError(call, message)
                mView.showError(message)
            }
        })
    }

    override fun start() {

    }

    override fun onCreateInteractor(): ForgotPasswordContract.Interactor = ForgotPasswordInteractor(this)

    override fun onCreateView(): ForgotPasswordContract.View = ForgotPasswordFragment.instance

}