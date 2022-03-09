package com.paynetone.counter.app;

import androidx.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;

public class PaynetOneApplication extends MultiDexApplication {
    static PaynetOneApplication paynetOneApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        paynetOneApplication = this;
    }
    public static PaynetOneApplication getInstance() {
        return paynetOneApplication;
    }
}
