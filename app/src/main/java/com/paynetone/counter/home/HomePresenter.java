package com.paynetone.counter.home;

import android.app.Activity;
import android.content.Context;

import com.core.base.viper.Presenter;
import com.core.base.viper.interfaces.ContainerView;
import com.google.gson.reflect.TypeToken;
import com.paynetone.counter.model.EmployeeModel;
import com.paynetone.counter.model.MerchantBalance;
import com.paynetone.counter.model.OrderModel;
import com.paynetone.counter.model.PaynetModel;
import com.paynetone.counter.model.SimpleResult;
import com.paynetone.counter.model.request.BaseRequest;
import com.paynetone.counter.network.CommonCallback;
import com.paynetone.counter.network.NetWorkController;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.SharedPref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HomePresenter extends Presenter<HomeContract.View, HomeContract.Interactor>
        implements HomeContract.Presenter {
    SharedPref sharedPref;
    Activity activity;
    PaynetModel paynetModel;

    public HomePresenter(ContainerView containerView) {
        super(containerView);
        this.activity = (Activity) containerView;
    }

    @Override
    public void start() {
        sharedPref = new SharedPref(activity);
        paynetModel = sharedPref.getPaynet();
        getBalance();
    }

    @Override
    public HomeContract.Interactor onCreateInteractor() {
        return new HomeInteractor(this);
    }

    @Override
    public HomeContract.View onCreateView() {
        return HomeFragment.getInstance();
    }

    @Override
    public void getBalance() {
        if (paynetModel.getBusinessType() == Constants.BUSINESS_TYPE_PERSONAL ||
                paynetModel.getBusinessType() == Constants.BUSINESS_TYPE_VIETLOTT ||
                paynetModel.getBusinessType() == Constants.BUSINESS_TYPE_SYNTHETIC) {
            BaseRequest baseRequest = new BaseRequest();
            baseRequest.setMerchantID(paynetModel.getMerchantID());
            mInteractor.getBalance(baseRequest, new CommonCallback<SimpleResult>((Activity) mContainerView) {
                @Override
                protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                    super.onSuccess(call, response);

                    if ("00".equals(response.body().getErrorCode())) {
                        List<MerchantBalance> merchantBalance = NetWorkController.getGson().fromJson(response.body().getData(), new TypeToken<List<MerchantBalance>>() {
                        }.getType());
                        mView.showBalance(merchantBalance);
                    }
                }

                @Override
                protected void onError(Call<SimpleResult> call, String message) {
                    mView.hideProgress();
//                    super.onError(call, message);
                }
            });
        }
    }
}
