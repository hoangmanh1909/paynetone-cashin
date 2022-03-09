package com.paynetone.counter.login.regiter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.core.base.viper.ViewFragment;
import com.paynetone.counter.R;
import com.paynetone.counter.adapter.StepperAdapter;
import com.paynetone.counter.interfaces.RegisterPassData;
import com.paynetone.counter.model.RegisterPassDataModel;
import com.stepstone.stepper.StepperLayout;

import butterknife.BindView;

public class RegisterFragment extends ViewFragment<RegisterContract.Presenter> implements RegisterContract.View, RegisterPassData {
    @BindView(R.id.stepperLayout)
    StepperLayout mStepperLayout;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    String merchantName, mobileNumber, email;
    RegisterPassDataModel registerPassData;

    public static RegisterFragment getInstance() {
        return new RegisterFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void initLayout() {
        super.initLayout();
        ivBack.setOnClickListener(view -> mPresenter.back());
        mStepperLayout.setAdapter(new StepperAdapter(getChildFragmentManager(), requireActivity(), this));
    }

    @Override
    public void saveData(RegisterPassDataModel dataModel) {
        this.registerPassData = dataModel;
    }

    @Override
    public RegisterPassDataModel getData() {
        return registerPassData;
    }
}
