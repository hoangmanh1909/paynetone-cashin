package com.paynetone.counter.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.paynetone.counter.R;
import com.paynetone.counter.base.PaynetOneActivity;

public class MainActivity extends PaynetOneActivity {

    @Override
    public ViewFragment onCreateFirstFragment() {
        return (ViewFragment)new MainPresenter((ContainerView)getBaseActivity()).getFragment();
    }
}