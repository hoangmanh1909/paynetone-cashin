package com.paynetone.counter.functions.qr;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.core.base.log.Logger;
import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.google.android.material.textfield.TextInputEditText;
import com.paynetone.counter.R;
import com.paynetone.counter.functions.payment.PaymentPresenter;
import com.paynetone.counter.model.EmployeeModel;
import com.paynetone.counter.model.OptionAmount;
import com.paynetone.counter.model.PaynetModel;
import com.paynetone.counter.model.request.OrderAddRequest;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.NumberUtils;
import com.paynetone.counter.utils.SharedPref;
import com.paynetone.counter.utils.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class QRDynamicFragment extends ViewFragment<QRDynamicContract.Presenter> implements QRDynamicContract.View {

    @BindView(R.id.edt_mobile_number)
    TextInputEditText edt_mobile_number;
    @BindView(R.id.edt_amount)
    TextInputEditText edt_amount;
    @BindView(R.id.edt_note)
    TextInputEditText edt_note;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.rd_zalopay)
    RadioButton rd_zalopay;
    @BindView(R.id.rd_viettel_pay)
    RadioButton rd_viettel_pay;
    @BindView(R.id.rd_shoppe_pay)
    RadioButton rd_shoppe_pay;

    SharedPref sharedPref;
    EmployeeModel employeeModel;
    PaynetModel paynetModel;
    OptionAmountAdapter adapter;
    List<OptionAmount> optionAmounts;
    OptionAmount mOptionAmount;

    int mPosition;

    public static QRDynamicFragment getInstance() {
        return new QRDynamicFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qr_dynamic;
    }

    @Override
    public void initLayout() {
        super.initLayout();
        optionAmounts = new ArrayList<>();
        sharedPref = new SharedPref(requireContext());
        employeeModel = sharedPref.getEmployeeModel();
        paynetModel = sharedPref.getPaynet();

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
        edt_amount.setText(NumberUtils.formatPriceNumber(10000));
        addOptionAmount();

        recycle.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new OptionAmountAdapter(getContext(), optionAmounts) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                holder.itemView.setOnClickListener(view -> {
                    mPosition = position;
                    mOptionAmount = optionAmounts.get(position);
                    edt_amount.setText(NumberUtils.formatPriceNumber(mOptionAmount.getAmount()));
                    notifyDataSetChanged();
                });
                if (mPosition == position) {
                    ((ViewHolder) holder).tv_amount.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_card_amount));
                    ((ViewHolder) holder).tv_amount.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    ((ViewHolder) holder).tv_amount.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    ((ViewHolder) holder).tv_amount.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                    ((ViewHolder) holder).tv_amount.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    ((ViewHolder) holder).tv_amount.setTypeface(Typeface.DEFAULT);
                }
            }
        };
        recycle.setAdapter(adapter);
        onListenerRadio(rd_shoppe_pay,rd_viettel_pay,rd_zalopay);
    }

    private void addOptionAmount() {
        OptionAmount optionAmount = new OptionAmount();
        optionAmount.setAmount(10000);
        optionAmount.setText(NumberUtils.formatPriceNumber(10000) + "đ");
        optionAmounts.add(optionAmount);

        optionAmount = new OptionAmount();
        optionAmount.setAmount(20000);
        optionAmount.setText(NumberUtils.formatPriceNumber(20000) + "đ");
        optionAmounts.add(optionAmount);

        optionAmount = new OptionAmount();
        optionAmount.setAmount(50000);
        optionAmount.setText(NumberUtils.formatPriceNumber(50000) + "đ");
        optionAmounts.add(optionAmount);

        optionAmount = new OptionAmount();
        optionAmount.setAmount(100000);
        optionAmount.setText(NumberUtils.formatPriceNumber(100000) + "đ");
        optionAmounts.add(optionAmount);

        optionAmount = new OptionAmount();
        optionAmount.setAmount(200000);
        optionAmount.setText(NumberUtils.formatPriceNumber(200000) + "đ");
        optionAmounts.add(optionAmount);

        optionAmount = new OptionAmount();
        optionAmount.setAmount(500000);
        optionAmount.setText(NumberUtils.formatPriceNumber(500000) + "đ");
        optionAmounts.add(optionAmount);
    }

    @OnClick({R.id.iv_back, R.id.btn_ok, R.id.rd_zalopay, R.id.rd_viettel_pay})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                mPresenter.back();
                break;
            case R.id.btn_ok:
                ok();
                break;
//            case R.id.rd_zalopay:
//                rd_viettel_pay.setChecked(!rd_zalopay.isChecked());
//                break;
//            case R.id.rd_viettel_pay:
//                rd_zalopay.setChecked(!rd_viettel_pay.isChecked());
//                break;
        }
    }

    private void ok() {
//        if (TextUtils.isEmpty(edt_mobile_number.getText())) {
//            Toast.showToast(requireContext(), "Bạn chưa nhập Số điện thoại");
//            return;
//        }
        if (TextUtils.isEmpty(edt_amount.getText())) {
            Toast.showToast(requireContext(), "Bạn chưa nhập Số tiền");
            return;
        }

        OrderAddRequest req = new OrderAddRequest();
        if (rd_viettel_pay.isChecked())
            req.setProviderCode(Constants.PROVIDER_VIETTEL);
        else if (rd_zalopay.isChecked())
            req.setProviderCode(Constants.PROVIDER_ZALO);
        else {
            req.setProviderCode(Constants.PROVIDER_SHOPPE);
        }
        req.setMobileNumber(Objects.requireNonNull(edt_mobile_number.getText()).toString());
        req.setServiceID(1);
        req.setAmount(Integer.parseInt(String.valueOf(edt_amount.getText()).replace(",", "")));
        req.setFee(0);
        req.setTransAmount(Integer.parseInt(String.valueOf(edt_amount.getText()).replace(",", "")));
        req.setOrderDes(Objects.requireNonNull(edt_note.getText()).toString());
        req.setChannel(Constants.CHANNEL);
        req.setEmpID(employeeModel.getId());
        req.setPaynetID(employeeModel.getPaynetID());
        req.setTransCategory(1);
        if (rd_shoppe_pay.isChecked()) req.setPaymentType(Constants.PAYMENT_TYPE_SHOPEE);
        else req.setPaymentType(Constants.PAYMENT_TYPE_VIETTEL_ZALO);
        req.setPaymentCate(2);
        req.setMerchantID(paynetModel.getMerchantID());
        new PaymentPresenter((ContainerView) requireActivity(), req).pushView();
    }
    private void onListenerRadio(RadioButton...arrayListRadio){
        final RadioButton[] currentSelected = {rd_viettel_pay};
        for (RadioButton radioButton:arrayListRadio){
            radioButton.setOnClickListener(view->{
                currentSelected[0].setChecked(false);
                currentSelected[0] = radioButton;
                currentSelected[0].setChecked(true);
            });
        }
    }

}
