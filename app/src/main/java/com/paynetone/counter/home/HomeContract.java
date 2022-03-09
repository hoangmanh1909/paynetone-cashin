package com.paynetone.counter.home;

import com.core.base.viper.interfaces.IInteractor;
import com.core.base.viper.interfaces.IPresenter;
import com.core.base.viper.interfaces.PresentView;
import com.paynetone.counter.model.MerchantBalance;
import com.paynetone.counter.model.SimpleResult;
import com.paynetone.counter.model.request.BaseRequest;
import com.paynetone.counter.network.CommonCallback;

import java.util.List;

public interface HomeContract {
    interface Interactor extends IInteractor<Presenter> {
        void getBalance(BaseRequest baseRequest, CommonCallback<SimpleResult> callback);
    }

    interface View extends PresentView<Presenter> {
        void showBalance(List<MerchantBalance> merchantBalances);
    }

    interface Presenter extends IPresenter<View, Interactor> {
        void getBalance();
    }
}
