package com.paynetone.counter.personal;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;

import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.paynetone.counter.R;
import com.paynetone.counter.interfaces.RegisterPassData;
import com.paynetone.counter.login.LoginActivity;
import com.paynetone.counter.login.regiter.merchant.MerchantPresenter;
import com.paynetone.counter.main.SplashScreenActivity;
import com.paynetone.counter.model.EmployeeModel;
import com.paynetone.counter.model.RegisterPassDataModel;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.DialogUtils;
import com.paynetone.counter.utils.SharedPref;

import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalFragment extends ViewFragment<PersonalContract.Presenter> implements PersonalContract.View {
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.rl_merchant_info)
    RelativeLayout rl_merchant_info;

    SharedPref sharedPref;

    public static PersonalFragment getInstance() {
        return new PersonalFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    public void initLayout() {
        super.initLayout();

        sharedPref = new SharedPref(requireActivity());

        EmployeeModel employeeModel = sharedPref.getEmployeeModel();
        tv_email.setText(employeeModel.getEmail());
        tv_mobile.setText(employeeModel.getMobileNumber());
        tv_name.setText(employeeModel.getFullName());

        String mode = sharedPref.getString(Constants.KEY_ANDROID_PAYMENT_MODE,"");

        try {
            if(mode.equals(Constants.ANDROID_PAYMENT_MODE_SHOW)){
                rl_merchant_info.setVisibility(View.VISIBLE);
            }
            if (Integer.parseInt(sharedPref.getPaynet().getPnoLevel())==Constants.PNOLEVEL_STALL){
                rl_merchant_info.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @OnClick({R.id.rl_logout, R.id.rl_merchant_info})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.rl_logout:
                DialogUtils.showOptionAction(
                        getContext(),
                        "Bạn có chắc chắn muốn thoát?",
                        "Đồng ý",
                        "Hủy",
                        (dialog, which) -> {
                            sharedPref.clear();
                            Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            dialog.dismiss();
                        }, (dialog, which) -> {
                            dialog.dismiss();
                        });
                break;
            case R.id.rl_merchant_info:
                new MerchantPresenter((ContainerView) requireContext(), Constants.MERCHANT_MODE_VIEW, new RegisterPassData() {
                    @Override
                    public void saveData(RegisterPassDataModel dataModel) {

                    }

                    @Override
                    public RegisterPassDataModel getData() {
                        return null;
                    }
                }).pushView();
                break;
        }
    }
}
