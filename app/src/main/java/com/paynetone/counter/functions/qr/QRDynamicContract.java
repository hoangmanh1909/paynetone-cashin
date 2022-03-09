package com.paynetone.counter.functions.qr;

import com.core.base.viper.interfaces.IInteractor;
import com.core.base.viper.interfaces.IPresenter;
import com.core.base.viper.interfaces.PresentView;

public interface QRDynamicContract {
    interface Interactor extends IInteractor<QRDynamicContract.Presenter> {
    }

    interface View extends PresentView<QRDynamicContract.Presenter> {
    }

    interface Presenter extends IPresenter<QRDynamicContract.View, QRDynamicContract.Interactor> {

    }
}
