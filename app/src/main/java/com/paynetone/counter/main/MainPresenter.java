package com.paynetone.counter.main;

import com.core.base.viper.Presenter;
import com.core.base.viper.interfaces.ContainerView;

public class MainPresenter extends Presenter<MainContract.View, MainContract.Interactor>
        implements MainContract.Presenter{

    public MainPresenter(ContainerView containerView) {
        super(containerView);
    }

    @Override
    public void start() {

    }

    @Override
    public MainContract.Interactor onCreateInteractor() {
        return null;
    }

    @Override
    public MainContract.View onCreateView() {
        return MainFragment.getInstance();
    }
}
