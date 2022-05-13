package com.paynetone.counter.functions.withdraw;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.base.log.Logger;
import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.google.android.material.textfield.TextInputEditText;
import com.paynetone.counter.R;
import com.paynetone.counter.callback.BankDialogCallback;
import com.paynetone.counter.callback.CloseCallback;
import com.paynetone.counter.dialog.BankBottomDialog;
import com.paynetone.counter.dialog.SuccessDialog;
import com.paynetone.counter.functions.withdraw.history.HistoryPresenter;
import com.paynetone.counter.home.HomeContract;
import com.paynetone.counter.model.BankModel;
import com.paynetone.counter.model.EmployeeModel;
import com.paynetone.counter.model.PaynetModel;
import com.paynetone.counter.model.request.WithdrawRequest;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.NumberUtils;
import com.paynetone.counter.utils.SharedPref;
import com.paynetone.counter.utils.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class WithDrawFragment extends ViewFragment<WithDrawContract.Presenter> implements WithDrawContract.View {

    @BindView(R.id.tv_amount_p)
    TextView tv_amount_p;
    @BindView(R.id.tv_bank)
    TextView tv_bank;
    @BindView(R.id.edt_amount)
    TextInputEditText edt_amount;
    @BindView(R.id.edt_account_number)
    TextInputEditText edt_account_number;
    @BindView(R.id.edt_full_name)
    TextInputEditText edt_full_name;

    long amountOutward;
    List<BankModel> mBankModels;
    BankModel mBankModel;

    SharedPref sharedPref;
    EmployeeModel employeeModel;
    PaynetModel paynetModel;

    public static WithDrawFragment getInstance() {
        return new WithDrawFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_with_draw;
    }

    @Override
    public void initLayout() {
        super.initLayout();
        mBankModels = new ArrayList<>();

        sharedPref = new SharedPref(requireActivity());
        employeeModel = sharedPref.getEmployeeModel();
        paynetModel = sharedPref.getPaynet();

        amountOutward = requireActivity().getIntent().getLongExtra(Constants.AMOUNT_OUTWARD, 0);
        tv_amount_p.setText(NumberUtils.formatPriceNumber(amountOutward) + " đ");

        edt_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edt_amount.removeTextChangedListener(this);
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    try {
                        edt_amount.setText(NumberUtils.formatPriceNumber(Integer.parseInt(charSequence.toString().replace(",", ""))));
                    } catch (Exception ex) {
                        Logger.w(ex);
                    }
                }
                edt_amount.addTextChangedListener(this);
                edt_amount.setSelection(edt_amount.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.btn_ok, R.id.rl_bank, R.id.iv_history})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                mPresenter.back();
                break;
            case R.id.btn_ok:
                ok();
                break;
            case R.id.rl_bank:
                selectBank();
                break;
            case R.id.iv_history:
                new HistoryPresenter((ContainerView) requireActivity()).pushView();
                break;
        }
    }

    private void selectBank() {
        BankBottomDialog baseDialog = new BankBottomDialog(mBankModels, bankModel -> {
            tv_bank.setText(bankModel.getShortName());
            mBankModel = bankModel;
        });
        baseDialog.show(getChildFragmentManager(), "WithDrawFragment");
    }

    private void ok() {
        if (mBankModel == null) {
            Toast.showToast(requireContext(), "Bạn chưa chọn ngân hàng");
            return;
        }
        if (TextUtils.isEmpty(edt_amount.getText())) {
            Toast.showToast(requireContext(), "Bạn chưa nhập Số tiền");
            return;
        }
        if (TextUtils.isEmpty(edt_account_number.getText())) {
            Toast.showToast(requireContext(), "Bạn chưa nhập Số tài khoản");
            return;
        }
        WithdrawRequest request = new WithdrawRequest();


        request.setBankID(mBankModel.getId());
        request.setMerchantID(paynetModel.getMerchantID());
        request.setPaynetID(paynetModel.getId());
        request.setAmount(Integer.parseInt(String.valueOf(edt_amount.getText()).replace(",", "")));
        request.setFee(0);
        request.setTransAmount(Integer.parseInt(String.valueOf(edt_amount.getText()).replace(",", "")));
        request.setMobileNumber(employeeModel.getMobileNumber());
        request.setAccountNumber(Objects.requireNonNull(edt_account_number.getText()).toString());
        request.setFullName(Objects.requireNonNull(edt_full_name.getText()).toString());
        request.setWithdrawCategory(1);
        mPresenter.withdraw(request);
    }

    @Override
    public void showBanks(List<BankModel> bankModels) {
        this.mBankModels.clear();
        this.mBankModels.addAll(bankModels);
    }

    @Override
    public void showSuccess(String retRefNumber) {
        SuccessDialog successDialog = new SuccessDialog(requireContext(), retRefNumber, () -> {
            requireActivity().finish();
        });
        successDialog.setCancelable(false);
        successDialog.show();
    }
}
