package com.paynetone.counter.functions.withdraw;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.core.base.log.Logger;
import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.google.android.material.textfield.TextInputEditText;
import com.paynetone.counter.R;
import com.paynetone.counter.callback.BankDialogCallback;
import com.paynetone.counter.callback.CloseCallback;
import com.paynetone.counter.dialog.BankBottomDialog;
import com.paynetone.counter.dialog.SelectWithDrawBottom;
import com.paynetone.counter.dialog.SelectWithDrawWalletBottom;
import com.paynetone.counter.dialog.SuccessDialog;
import com.paynetone.counter.functions.withdraw.history.HistoryPresenter;
import com.paynetone.counter.home.HomeContract;
import com.paynetone.counter.model.BankModel;
import com.paynetone.counter.model.EmployeeModel;
import com.paynetone.counter.model.MerchantModel;
import com.paynetone.counter.model.PaynetModel;
import com.paynetone.counter.model.SelectWithDraw;
import com.paynetone.counter.model.request.WithdrawRequest;
import com.paynetone.counter.model.response.WalletResponse;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.NumberUtils;
import com.paynetone.counter.utils.PrefConst;
import com.paynetone.counter.utils.SharedPref;
import com.paynetone.counter.utils.Toast;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class WithDrawFragment extends ViewFragment<WithDrawContract.Presenter> implements WithDrawContract.View {

    @BindView(R.id.tv_amount_p)
    TextView tv_amount_p;

    //map view bank
    @BindView(R.id.tv_name_with_draw)
    AppCompatTextView tv_name_with_draw;
    @BindView(R.id.tv_name_bank)
    AppCompatTextView tv_name_bank;
    @BindView(R.id.tv_name_account_number_bank)
    AppCompatTextView tv_name_account_number_bank;
    @BindView(R.id.tv_full_name_personal_bank)
    AppCompatTextView tv_full_name_personal_bank;
    @BindView(R.id.edt_amount)
    TextInputEditText edt_amount;
    @BindView(R.id.layout_bank)
    ConstraintLayout layoutBank;

    //map view wallet
    @BindView(R.id.tv_name_wallet)
    AppCompatTextView tv_name_wallet;
    @BindView(R.id.tv_name_number_phone)
    AppCompatEditText tv_name_number_phone;
    @BindView(R.id.tv_full_name_personal_wallet)
    AppCompatEditText tv_full_name_personal_wallet;
    @BindView(R.id.layout_wallet)
    ConstraintLayout layoutWallet;

    // map view vietlott

    @BindView(R.id.tv_full_name_personal_vietlott)
    AppCompatEditText tv_full_name_personal_vietlott;
    @BindView(R.id.tv_name_post_id_vietlott)
    AppCompatTextView tv_name_post_id_vietlott;
    @BindView(R.id.layout_vietlott)
    ConstraintLayout layoutVietlott;


    long amountOutward;
    List<BankModel> mBankModels;
    BankModel mBankModel;

    SharedPref sharedPref;
    EmployeeModel employeeModel;
    PaynetModel paynetModel;
    private ArrayList<WalletResponse> walletResponses = new ArrayList<>();
    private WalletResponse selectedWallet;
    private SelectWithDraw selectWithDraw = SelectWithDraw.BANK;


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
        mPresenter.getByMobileNumber(employeeModel.getMobileNumber());
        initView();

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

        tv_name_with_draw.setOnClickListener(v -> {
            SelectWithDrawBottom bottom =  SelectWithDrawBottom.getInstance(selectWithDraw,paynetModel.getBusinessType());
            bottom.show(getChildFragmentManager(),"SelectWithDrawBottom");
            bottom.onCallBackListener(selectWithDraw->{
                showOrHideLayoutWithDrawBank(selectWithDraw);
                this.selectWithDraw = selectWithDraw;
            });


        });
        tv_name_wallet.setOnClickListener(v -> {
            if (walletResponses ==null || walletResponses.size()==0){
                mPresenter.getWallet();
            }else {
                showBottomSheetWallet();
            }

        });
    }
    private void initView(){
        try {
//            tv_full_name_personal_bank.setText(employeeModel.getPaymentAccName());
//            tv_name_account_number_bank.setText(employeeModel.getPaymentAccNo());
            tv_name_post_id_vietlott.setText(paynetModel.getPosID());
//            tv_name_bank.setText(employeeModel.getBankName());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @OnClick({R.id.iv_back, R.id.btn_ok, R.id.iv_history})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                mPresenter.back();
                break;
            case R.id.btn_ok:
                ok();
                break;
            case R.id.iv_history:
                new HistoryPresenter((ContainerView) requireActivity()).pushView();
                break;
        }
    }

    private void ok() {
        if (confirmInput()){
            WithdrawRequest request = new WithdrawRequest();
            request.setMerchantID(paynetModel.getMerchantID());
            request.setPaynetID(paynetModel.getId());
            request.setAmount(Integer.parseInt(String.valueOf(edt_amount.getText()).replace(",", "")));
            request.setFee(0);
            request.setTransAmount(Integer.parseInt(String.valueOf(edt_amount.getText()).replace(",", "")));

            switch (selectWithDraw){
                case BANK:
                    request.setBankID(employeeModel.getBankID());
                    request.setMobileNumber(employeeModel.getMobileNumber());
                    request.setAccountNumber(Objects.requireNonNull(tv_name_account_number_bank.getText()).toString());
                    request.setFullName(employeeModel.getPaymentAccName());
                    request.setWithdrawCategory(Constants.WITHDRAW_CATEGORY_BANK);
                    request.setWalletID(0); // default
                    break;
                case E_WALLET:
                    request.setBankID(0);
                    request.setMobileNumber(tv_name_number_phone.getText().toString());
                    request.setAccountNumber("");
                    request.setFullName(Objects.requireNonNull(tv_full_name_personal_wallet.getText()).toString());
                    request.setWithdrawCategory(Constants.WITHDRAW_CATEGORY_WALLET);
                    request.setWalletID(selectedWallet.getId());
                    break;
                case VIETLOTT:
                    request.setBankID(0);
                    request.setFullName(Objects.requireNonNull(tv_full_name_personal_vietlott.getText()).toString());
                    request.setWithdrawCategory(Constants.WITHDRAW_CATEGORY_VIETLOTT);
                    request.setWalletID(0);
                    request.setPosID(paynetModel.getPosID());

            }
            mPresenter.withdraw(request);
        }

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

    @Override
    public void showListWallet(List<WalletResponse> walletResponseList) {
        this.walletResponses.addAll(walletResponseList ==null ? new ArrayList<>():walletResponseList);
        showBottomSheetWallet();
    }

    @Override
    public void showMerchant(MerchantModel model) {
        tv_full_name_personal_bank.setText(model.getPaymentAccountName());
        tv_name_account_number_bank.setText(model.getPaymentAccountNumber());
        tv_name_bank.setText(model.getPaymentAccountBankName());
    }

    private void showBottomSheetWallet(){
        SelectWithDrawWalletBottom bottom =  SelectWithDrawWalletBottom.getInstance(walletResponses);
        bottom.show(getChildFragmentManager(),"SelectWithDrawWalletBottom");
        bottom.onCallBackListener(walletResponse -> {
            tv_name_wallet.setText(walletResponse.getName());
            this.selectedWallet = walletResponse;
        });
    }
    private void showOrHideLayoutWithDrawBank(SelectWithDraw selectWithDraw){
        switch (selectWithDraw){
            case BANK:
                layoutBank.setVisibility(View.VISIBLE);
                layoutWallet.setVisibility(View.GONE);
                layoutVietlott.setVisibility(View.GONE);
                tv_name_with_draw.setText(getString(R.string.str_with_draw_bank));
                break;
            case E_WALLET:
                layoutWallet.setVisibility(View.VISIBLE);
                layoutBank.setVisibility(View.GONE);
                layoutVietlott.setVisibility(View.GONE);
                tv_name_with_draw.setText(getString(R.string.str_with_draw_wallet));
                prepareViewWallet();
                break;
            case VIETLOTT:
                layoutVietlott.setVisibility(View.VISIBLE);
                layoutWallet.setVisibility(View.GONE);
                layoutBank.setVisibility(View.GONE);
                tv_name_with_draw.setText(getString(R.string.str_with_draw_vietlott));
                prepareViewWallet();
                break;


        }
    }
    private Boolean validateSelectWallet() {
        if (selectedWallet==null) {
            Toast.showToast(requireContext(),getResources().getString(R.string.str_not_enter_wallet));
            return false;
        }
        return true;
    }
    private Boolean validatePhoneNumber() {
        String phone = tv_name_number_phone.getText().toString();
        if (phone.isEmpty()) {
            Toast.showToast(requireContext(),getResources().getString(R.string.str_not_enter_phone_number));
            return false;
        }
        return true;
    }
    private Boolean validateReceiveFullNameWallet() {
        String fullName = tv_full_name_personal_wallet.getText().toString();
        if (fullName.isEmpty()) {
            Toast.showToast(requireContext(),getResources().getString(R.string.str_not_enter_receive_full_name));
            return false;
        }
        return true;
    }
    private Boolean validateReceiveFullNameVietlott() {
        String fullName = tv_full_name_personal_vietlott.getText().toString();
        if (fullName.isEmpty()) {
            Toast.showToast(requireContext(),getResources().getString(R.string.str_not_enter_receive_full_name));
            return false;
        }
        return true;
    }
    private Boolean validateAmount() {
        String amount = edt_amount.getText().toString();
        if (amount.isEmpty()) {
            Toast.showToast(requireContext(),getResources().getString(R.string.str_not_enter_amount));
            return false;
        }
        return true;
    }

    private Boolean confirmInput(){
        switch (selectWithDraw){
            case BANK:
                return validateAmount();
            case E_WALLET:
                return  validateSelectWallet() && validatePhoneNumber() && validateReceiveFullNameWallet() && validateAmount();
            case VIETLOTT:
                return validateReceiveFullNameVietlott() && validateAmount();
            default:
                return false;
        }
    }
    private void prepareViewWallet(){
        try {
            tv_name_wallet.setText("");
            tv_name_number_phone.setText("");
            tv_full_name_personal_wallet.setText("");
            edt_amount.setText("");
            clearCheckedWallet();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearCheckedWallet(){
        for (WalletResponse walletResponse:walletResponses){
            walletResponse.setChecked(false);
        }
    }
}
