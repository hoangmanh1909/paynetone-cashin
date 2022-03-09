package com.paynetone.counter.functions.qr;

import com.core.base.viper.Presenter;
import com.core.base.viper.interfaces.ContainerView;

public class QRDynamicPresenter extends Presenter<QRDynamicContract.View, QRDynamicContract.Interactor>
        implements QRDynamicContract.Presenter {

    public QRDynamicPresenter(ContainerView containerView) {
        super(containerView);
    }

    @Override
    public void start() {

    }

    @Override
    public QRDynamicContract.Interactor onCreateInteractor() {
        return null;
    }

    @Override
    public QRDynamicContract.View onCreateView() {
        return QRDynamicFragment.getInstance();
    }
}
