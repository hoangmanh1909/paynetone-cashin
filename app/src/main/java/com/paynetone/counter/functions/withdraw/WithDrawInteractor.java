package com.paynetone.counter.functions.withdraw;

import com.core.base.viper.Interactor;
import com.paynetone.counter.home.HomeContract;
import com.paynetone.counter.model.SimpleResult;
import com.paynetone.counter.model.request.WithdrawRequest;
import com.paynetone.counter.network.CommonCallback;
import com.paynetone.counter.network.NetWorkController;

public class WithDrawInteractor extends Interactor<WithDrawContract.Presenter>
        implements WithDrawContract.Interactor {

    public WithDrawInteractor(WithDrawContract.Presenter presenter) {
        super(presenter);
    }

    @Override
    public void getBank(CommonCallback<SimpleResult> callback) {
        NetWorkController.getBank(callback);
    }

    @Override
    public void withdraw(WithdrawRequest withdrawRequest, CommonCallback<SimpleResult> callback) {
        NetWorkController.withdraw(withdrawRequest, callback);
    }
}
