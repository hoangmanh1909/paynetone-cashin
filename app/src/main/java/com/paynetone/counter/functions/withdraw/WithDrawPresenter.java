package com.paynetone.counter.functions.withdraw;

import android.app.Activity;

import com.core.base.viper.Presenter;
import com.core.base.viper.interfaces.ContainerView;
import com.google.gson.reflect.TypeToken;
import com.paynetone.counter.model.BankModel;
import com.paynetone.counter.model.MerchantBalance;
import com.paynetone.counter.model.PaynetModel;
import com.paynetone.counter.model.SimpleResult;
import com.paynetone.counter.model.request.BaseRequest;
import com.paynetone.counter.model.request.WithdrawRequest;
import com.paynetone.counter.model.response.WithdrawResponse;
import com.paynetone.counter.network.CommonCallback;
import com.paynetone.counter.network.NetWorkController;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class WithDrawPresenter  extends Presenter<WithDrawContract.View, WithDrawContract.Interactor>
        implements WithDrawContract.Presenter {

    public WithDrawPresenter(ContainerView containerView) {
        super(containerView);
    }

    @Override
    public void start() {
        mInteractor.getBank(new CommonCallback<SimpleResult>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    List<BankModel> bankModels = NetWorkController.getGson().fromJson(response.body().getData(), new TypeToken<List<BankModel>>() {
                    }.getType());
                    mView.showBanks(bankModels);
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

    @Override
    public WithDrawContract.Interactor onCreateInteractor() {
        return new WithDrawInteractor(this);
    }

    @Override
    public WithDrawContract.View onCreateView() {
        return WithDrawFragment.getInstance();
    }

    @Override
    public void withdraw(WithdrawRequest withdrawRequest) {
        mView.showProgress();
        mInteractor.withdraw(withdrawRequest, new CommonCallback<SimpleResult>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    WithdrawResponse withdrawResponse = NetWorkController.getGson().fromJson(response.body().getData(),WithdrawResponse.class);
                    mView.showSuccess(withdrawResponse.getRetRefNumber());
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