package com.paynetone.counter.functions.qr;

import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.paynetone.counter.base.PaynetOneActivity;

public class QRDynamicActivity extends PaynetOneActivity {
    @Override
    public ViewFragment onCreateFirstFragment() {
        return (ViewFragment)new QRDynamicPresenter((ContainerView)getBaseActivity()).getFragment();
    }
}
