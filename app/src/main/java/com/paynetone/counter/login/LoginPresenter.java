package com.paynetone.counter.login;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.core.base.viper.Presenter;
import com.core.base.viper.interfaces.ContainerView;
import com.paynetone.counter.R;
import com.paynetone.counter.interfaces.RegisterPassData;
import com.paynetone.counter.login.regiter.merchant.MerchantPresenter;
import com.paynetone.counter.model.EmployeeModel;
import com.paynetone.counter.model.PaynetModel;
import com.paynetone.counter.model.RegisterPassDataModel;
import com.paynetone.counter.model.SimpleResult;
import com.paynetone.counter.model.request.BaseRequest;
import com.paynetone.counter.model.request.LoginRequest;
import com.paynetone.counter.network.CommonCallback;
import com.paynetone.counter.network.NetWorkController;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.SharedPref;
import com.paynetone.counter.utils.Toast;

import retrofit2.Call;
import retrofit2.Response;

public class LoginPresenter extends Presenter<LoginContract.View, LoginContract.Interactor>
        implements LoginContract.Presenter {

    public LoginPresenter(ContainerView containerView) {
        super(containerView);
    }

    @Override
    public void start() {

    }

    @Override
    public LoginContract.Interactor onCreateInteractor() {
        return new LoginInteractor(this);
    }

    @Override
    public LoginContract.View onCreateView() {
        return LoginFragment.getInstance();
    }

    @Override
    public void login(String phone, String passWord) {
        mView.showProgress();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMobileNumber(phone);
        loginRequest.setPassword(passWord);
        mInteractor.login(loginRequest, new CommonCallback<SimpleResult>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    SharedPref sharedPref = new SharedPref((Context) mContainerView);
                    EmployeeModel employeeModel = NetWorkController.getGson().fromJson(response.body().getData(), EmployeeModel.class);
                    sharedPref.saveEmployee(employeeModel);
                    if (employeeModel.getPaynetID() > 0)
                        getPaynet(employeeModel);
                    else {
                        new MerchantPresenter(mContainerView, Constants.MERCHANT_MODE_EDIT, new RegisterPassData() {
                            @Override
                            public void saveData(RegisterPassDataModel dataModel) {

                            }

                            @Override
                            public RegisterPassDataModel getData() {
                                RegisterPassDataModel registerPassDataModel = new RegisterPassDataModel();
                                registerPassDataModel.setMobileNumber(employeeModel.getMobileNumber());
                                return registerPassDataModel;
                            }
                        }).pushView();
                    }
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

    void getPaynet(EmployeeModel employeeModel) {
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setID(employeeModel.getPaynetID());
        mInteractor.getPaynet(baseRequest, new CommonCallback<SimpleResult>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    SharedPref sharedPref = new SharedPref((Context) mContainerView);
                    PaynetModel paynetModel = NetWorkController.getGson().fromJson(response.body().getData(), PaynetModel.class);
                    if (Integer.parseInt(paynetModel.getPnoLevel()) == Constants.PNOLEVEL_STALL || Integer.parseInt(paynetModel.getPnoLevel()) == Constants.PNOLEVEL_MERCHANT){
                        sharedPref.savePaynet(paynetModel);
                        mView.goHome();
                    }else {
                        mView.showAlertDialog(getViewContext().getResources().getString(R.string.str_allow_account_personal_and_stall_login));
                    }
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
