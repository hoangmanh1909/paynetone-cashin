package com.paynetone.counter.main;

import com.core.base.viper.Interactor;
import com.paynetone.counter.home.HomeContract;
import com.paynetone.counter.model.SimpleResult;
import com.paynetone.counter.model.request.BaseRequest;
import com.paynetone.counter.network.CommonCallback;
import com.paynetone.counter.network.NetWorkController;

public class MainInteractor extends Interactor<MainContract.Presenter>
        implements MainContract.Interactor {

    public MainInteractor(MainContract.Presenter presenter) {
        super(presenter);
    }

    @Override
    public void getBalance(BaseRequest baseRequest, CommonCallback<SimpleResult> callback) {
        NetWorkController.getBalance(baseRequest, callback);
    }

    @Override
    public void requestProvider(CommonCallback<SimpleResult> callback) {
        NetWorkController.requestProvider(callback);
    }
}
