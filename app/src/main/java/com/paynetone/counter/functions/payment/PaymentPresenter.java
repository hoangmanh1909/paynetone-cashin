package com.paynetone.counter.functions.payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.core.base.viper.Presenter;
import com.core.base.viper.interfaces.ContainerView;
import com.paynetone.counter.functions.WebViewActivity;
import com.paynetone.counter.functions.payment.qr.QRActivity;
import com.paynetone.counter.functions.payment.qr.QRPresenter;
import com.paynetone.counter.functions.qr.QRDynamicPresenter;
import com.paynetone.counter.model.EmployeeModel;
import com.paynetone.counter.model.SimpleResult;
import com.paynetone.counter.model.request.LoginRequest;
import com.paynetone.counter.model.request.OrderAddRequest;
import com.paynetone.counter.model.response.OrderAddResponse;
import com.paynetone.counter.network.CommonCallback;
import com.paynetone.counter.network.NetWorkController;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.SharedPref;

import retrofit2.Call;
import retrofit2.Response;

public class PaymentPresenter extends Presenter<PaymentContract.View, PaymentContract.Interactor>
        implements PaymentContract.Presenter {

    OrderAddRequest mOrderAddRequest;
    Activity activity;

    public PaymentPresenter(ContainerView containerView, OrderAddRequest orderAddRequest) {
        super(containerView);
        this.mOrderAddRequest = orderAddRequest;

        this.activity = (Activity) containerView;
    }

    @Override
    public void start() {

    }

    @Override
    public PaymentContract.Interactor onCreateInteractor() {
        return new PaymentInteractor(this);
    }

    @Override
    public PaymentContract.View onCreateView() {
        return PaymentFragment.getInstance();
    }

    @Override
    public OrderAddRequest getOrderAddRequest() {
        return mOrderAddRequest;
    }

    @Override
    public void orderAdd(OrderAddRequest request) {
        mView.showProgress();
        mInteractor.orderAdd(request, new CommonCallback<SimpleResult>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    OrderAddResponse orderAddResponse = NetWorkController.getGson().fromJson(response.body().getData(), OrderAddResponse.class);
//                    Intent intent = new Intent(activity, QRActivity.class);
//                    intent.putExtra(Constants.WEB_VIEW_URL,orderAddResponse.getReturnURL());
//                    activity.startActivity(intent);
                    new QRPresenter(mContainerView, orderAddResponse, mOrderAddRequest).pushView();
                } else {
                    mView.showAlertDialog(response.body().getMessage());
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
            }
        });
    }
}
