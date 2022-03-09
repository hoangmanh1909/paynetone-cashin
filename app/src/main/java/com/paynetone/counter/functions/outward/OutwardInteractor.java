package com.paynetone.counter.functions.outward;

import com.core.base.viper.Interactor;
import com.paynetone.counter.model.SimpleResult;
import com.paynetone.counter.network.CommonCallback;
import com.paynetone.counter.network.NetWorkController;

public class OutwardInteractor  extends Interactor<OutwardContract.Presenter>
        implements OutwardContract.Interactor{

    public OutwardInteractor(OutwardContract.Presenter presenter) {
        super(presenter);
    }

    @Override
    public void getOutward(int merchantID, CommonCallback<SimpleResult> callback) {
        NetWorkController.getOutward(merchantID, callback);
    }
}
