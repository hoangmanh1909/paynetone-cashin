package com.paynetone.counter.personal;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.paynetone.counter.BuildConfig;
import com.paynetone.counter.R;
import com.paynetone.counter.dialog.ConfirmPasswordDialog;
import com.paynetone.counter.dialog.ContactDialog;
import com.paynetone.counter.dialog.DevelopDialog;
import com.paynetone.counter.dialog.HistoryDialog;
import com.paynetone.counter.dialog.ManagerHanMucDialog;
import com.paynetone.counter.dialog.NapHanMucDialog;
import com.paynetone.counter.dialog.PinCodeDialog;
import com.paynetone.counter.dialog.SettingTabMainDialog;
import com.paynetone.counter.dialog.TermPoliciesDialog;
import com.paynetone.counter.functions.history.HistoryActivity;
import com.paynetone.counter.interfaces.RegisterPassData;
import com.paynetone.counter.login.regiter.merchant.MerchantPresenter;
import com.paynetone.counter.main.SplashScreenActivity;
import com.paynetone.counter.model.EmployeeModel;
import com.paynetone.counter.model.MerchantBalance;
import com.paynetone.counter.model.PaynetGetBalanceByIdResponse;
import com.paynetone.counter.model.PaynetModel;
import com.paynetone.counter.model.RegisterPassDataModel;
import com.paynetone.counter.model.request.PINAddRequest;
import com.paynetone.counter.model.response.PINCodeResponse;
import com.paynetone.counter.model.response.TranSearchResponse;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.DialogUtils;
import com.paynetone.counter.utils.NumberUtils;
import com.paynetone.counter.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;
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
    RelativeLayout rl_setting;
    @BindView(R.id.layout_han_muc)
    ConstraintLayout layoutHanMuc;
    @BindView(R.id.tv_han_muc)
    AppCompatTextView tvHanMuc;
    @BindView(R.id.btn_nap)
    AppCompatButton btnNapHanMuc;
    @BindView(R.id.rl_han_muc_store)
    RelativeLayout rlHanMucStore;
    @BindView(R.id.rl_pin_code)
    RelativeLayout rlPinCode;

    SharedPref sharedPref;
    PaynetModel paynetModel;
    EmployeeModel employeeModel;
    Long amountOutWard = 0L;
    String password = "";
    String mode = "";

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


        employeeModel = sharedPref.getEmployeeModel();
        paynetModel = sharedPref.getPaynet();
        mode = sharedPref.getString(Constants.KEY_ANDROID_PAYMENT_MODE, "");

        try {
            if (sharedPref.isMerchantAdmin() ){ // merchant admin
                btnNapHanMuc.setVisibility(View.VISIBLE);
                rlHanMucStore.setVisibility(View.VISIBLE);
                rlPinCode.setVisibility(View.VISIBLE);
            }

            if (sharedPref.isManagerStore()) btnNapHanMuc.setVisibility(View.VISIBLE); // quản lý của hàng

            if ((sharedPref.isAccountBranch() &&
                    (sharedPref.isAccountant() || // kế toán
                     sharedPref.isAdmin() || // admin
                     sharedPref.isManagerStore())) || // quản lý chi nhánh)
                     sharedPref.isBranchStore()|| // quản lý cửa hàng
                     sharedPref.isAccountStall()   ){ // tải khoản quầy
                rl_merchant_info.setVisibility(View.GONE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if (employeeModel !=null) mPresenter.getBalance(employeeModel.getPaynetID());
        tv_email.setText(employeeModel.getEmail());
        if (employeeModel.getEmail().isEmpty()) tv_email.setVisibility(View.GONE);
        tv_mobile.setText(employeeModel.getMobileNumber());
        tv_name.setText(employeeModel.getFullName());
        tv_version.setText("v." + BuildConfig.VERSION_NAME);


        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isCheckedChangeByCode) {
                turnOnOffNotification();
            } else {
                isCheckedChangeByCode = false;
            }
        });
        hideViewToServer();
    }

    @OnClick({R.id.rl_logout, R.id.rl_merchant_info, R.id.iv_back, R.id.layout_transaction_history, R.id.layout_notify,
            R.id.rl_change_password, R.id.rl_han_muc_store, R.id.rl_terms_policies, R.id.rl_news, R.id.rl_contact,R.id.btn_nap,
            R.id.rl_order,R.id.rl_option_tab_main,R.id.rl_pin_code})
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
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.layout_transaction_history:
                new HistoryDialog(requireContext()).show(getChildFragmentManager(),"PersonalFragment");
                break;

            case R.id.layout_notify:
            case R.id.rl_change_password:
            case R.id.rl_news:
                new DevelopDialog(requireContext()).show(getChildFragmentManager(), "PersonalFragment");
                break;
            case R.id.rl_terms_policies:
                new TermPoliciesDialog(requireContext()).show(getChildFragmentManager(), "PersonalFragment");
                break;
            case R.id.rl_contact:
                new ContactDialog(requireContext()).show(getChildFragmentManager(), "PersonalFragment");
                break;
            case R.id.rl_han_muc_store:{
                if (employeeModel != null) mPresenter.paynetGetBalanceByID(employeeModel.getPaynetID());
                break;
            }
            case R.id.btn_nap:
                if (paynetModel.getCode() != null ){
                    new NapHanMucDialog(requireContext(),tvHanMuc.getText().toString(),paynetModel.getCode(),amountOutWard,null).show(getChildFragmentManager(),"PersonalFragment");
                }
                break;
            case R.id.rl_order:{
                Intent intent = new Intent(requireActivity(), HistoryActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.rl_option_tab_main:{
                new SettingTabMainDialog(requireContext()).show(getChildFragmentManager(),"PersonalFragment");
                break;
            }
            case R.id.rl_pin_code:
                showConfirmPassword();
                break;

        }
    }


    private void showConfirmPassword(){
        new ConfirmPasswordDialog(requireContext(), password -> {
            this.password = password;
            mPresenter.requestPINCode(new PINAddRequest(null,null,password,employeeModel.getMobileNumber()));
        }).show(getChildFragmentManager(),"NewPinCodeDialog");
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
            if (getActivity() != null) {
                isShowOnOffBackground = true;


                new MaterialAlertDialogBuilder(requireContext(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
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
        } catch (Exception e) {
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
            } else {
                newState = 1;
                // good news! It works fine even in the background.
            }

            int currentState;

            if (switchCompat.isChecked()) currentState = 1;
            else currentState = 0;

            if (currentState != newState) {
                isCheckedChangeByCode = true;
            }
            switchCompat.setChecked(newState == 1);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void showBalance(List<MerchantBalance> merchantBalances) {
        for (MerchantBalance merchantBalance : merchantBalances) {
            if (merchantBalance.getAccountType().equals("S")) {
                tvHanMuc.setText(NumberUtils.formatPriceNumber(merchantBalance.getBalance()) + " VND");
            } else if (merchantBalance.getAccountType().equals("P")){
                amountOutWard = merchantBalance.getBalance();
            }
        }
    }

    @Override
    public void showManagerHanMuc(ArrayList<PaynetGetBalanceByIdResponse> responses) {
        new ManagerHanMucDialog(requireContext(),responses,amountOutWard).show(getChildFragmentManager(),"PersonalFragment");
    }

    @Override
    public void showPINCodeSuccess(PINCodeResponse response) {
        if (response.getHasPin().equals(Constants.NOT_EXIST_PIN_CODE)){ // chưa có mã PIN
            new PinCodeDialog(requireContext(),getString(R.string.str_title_new_pin_code),password)
                    .show(getChildFragmentManager(),"PersonalFragment");

        }else if (response.getHasPin().equals(Constants.EXIST_PIN_CODE)){ // đã tạo mã PIN
            new PinCodeDialog(requireContext(),getString(R.string.str_title_change_pin_code),password)
                    .show(getChildFragmentManager(), "PersonalFragment");
        }
    }
    private void hideViewToServer(){
        if (mode.equals(Constants.ANDROID_PAYMENT_MODE_HIDE)) {
            layoutHanMuc.setVisibility(View.GONE);
            rlHanMucStore.setVisibility(View.GONE);
            rl_merchant_info.setVisibility(View.GONE);
        }
    }

}
