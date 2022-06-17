package com.paynetone.counter.personal;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationManagerCompat;

import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.paynetone.counter.BuildConfig;
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
    @BindView(R.id.id_version)
    TextView tv_version;
    @BindView(R.id.switchCompat)
    SwitchCompat switchCompat;

    @BindView(R.id.rl_setting)
    LinearLayout rl_setting;

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
        tv_version.setText("v."+BuildConfig.VERSION_NAME);

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

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isCheckedChangeByCode) {
                turnOnOffNotification();
            } else {
                isCheckedChangeByCode = false;
            }
        });

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
                            // clear data SharedPref
                            sharedPref.clear();

                            //remove notification
                            NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();

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

    @Override
    public void onResume() {
        super.onResume();
        if (!isShowOnOffBackground) {
            changeStateBackgroundSetting();
        }
    }

    private Boolean isShowOnOffBackground = false;
    private void turnOnOffNotification() {
        try {
            if (getActivity()!=null){
                isShowOnOffBackground=true;


                new MaterialAlertDialogBuilder(requireContext(),R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
                        .setMessage(R.string.str_allow_running_background)
                        .setPositiveButton("Cho phép", (dialog, which) -> {
                            isShowOnOffBackground = false;
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                            startActivity(intent);
                        })
                        .setNegativeButton("Hủy", (dialog, which) -> {
                            isShowOnOffBackground = false;
                            changeStateBackgroundSetting();
                        })
                        .setOnCancelListener(dialog -> {
                            isShowOnOffBackground = false;
                            changeStateBackgroundSetting();
                        })
                        .show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * check and change state allow background setting
     */
    private Boolean isCheckedChangeByCode = false;
    private void changeStateBackgroundSetting() {

        try {
            int newState = 1;
            String packageName = getActivity().getPackageName();
            PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                newState = 0;
                // it is not enabled. Ask the user to do so from the settings.
            }else {
                newState = 1;
                // good news! It works fine even in the background.
            }

            int currentState;

            if (switchCompat.isChecked()) currentState = 1;
            else  currentState = 0;

            if (currentState != newState){
                isCheckedChangeByCode=true;
            }
            switchCompat.setChecked(newState == 1);
        }catch (Exception e){

        }
    }
}
