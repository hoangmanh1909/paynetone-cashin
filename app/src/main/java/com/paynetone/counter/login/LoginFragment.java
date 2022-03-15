package com.paynetone.counter.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.core.base.viper.ViewFragment;
import com.core.utils.AppUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.paynetone.counter.callback.CloseCallback;
import com.paynetone.counter.dialog.SelectBusinessTypeDialog;
import com.paynetone.counter.login.regiter.RegisterActivity;
import com.paynetone.counter.main.MainActivity;
import com.paynetone.counter.R;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.NumberUtils;
import com.paynetone.counter.utils.SharedPref;
import com.paynetone.counter.utils.Toast;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends ViewFragment<LoginContract.Presenter> implements LoginContract.View {
    @BindView(R.id.edt_mobile_number)
    TextInputEditText edtPhoneNumber;
    @BindView(R.id.edt_password)
    TextInputEditText edtPass;
    @BindView(R.id.ll_register)
    LinearLayout ll_register;

    SharedPref sharedPref;

    public static LoginFragment getInstance() {
        return new LoginFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void initLayout() {
        super.initLayout();

        sharedPref = new SharedPref(requireContext());
        String mode = sharedPref.getString(Constants.KEY_ANDROID_PAYMENT_MODE,"");
        if(mode.equals(Constants.ANDROID_PAYMENT_MODE_SHOW)){
            ll_register.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.btn_login, R.id.btn_register,R.id.rootView})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                SelectBusinessTypeDialog dialog = new SelectBusinessTypeDialog(requireContext(), () -> {
                    Intent intent = new Intent(requireActivity(),RegisterActivity.class);
                    startActivity(intent);
                });
                dialog.setCancelable(false);
                dialog.show();
                break;
            case R.id.rootView:
                AppUtils.hideKeyboard(view);
                break;
        }
    }

    private void login() {
        String phoneNumber = Objects.requireNonNull(edtPhoneNumber.getText()).toString();
        String passWord = Objects.requireNonNull(edtPass.getText()).toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.showToast(getActivity(), getString(R.string.error_empty_phone));
            return;
        }
        if (!org.apache.commons.lang3.math.NumberUtils.isDigits(phoneNumber) || phoneNumber.contains(".") || phoneNumber.contains(",")) {
            Toast.showToast(getActivity(), getString(R.string.error_warning_phone));
            return;
        }
        String phone = NumberUtils.convertVietNamPhoneNumber(phoneNumber);
        if (phone.length() != 10) {
            Toast.showToast(getActivity(), getString(R.string.error_warning_phone));
            return;
        }
        if (TextUtils.isEmpty(passWord)) {
            Toast.showToast(getActivity(), getString(R.string.error_password_empty));
            return;
        }

        mPresenter.login(phone, passWord);
    }

    @Override
    public void goHome() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
