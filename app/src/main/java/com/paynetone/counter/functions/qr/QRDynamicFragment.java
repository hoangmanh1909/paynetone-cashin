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
import com.core.utils.AppUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.paynetone.counter.R;
import com.paynetone.counter.functions.payment.PaymentPresenter;
import com.paynetone.counter.model.EmployeeModel;
import com.paynetone.counter.model.OptionAmount;
import com.paynetone.counter.model.PaymentModel;
import com.paynetone.counter.model.PaynetModel;
import com.paynetone.counter.model.request.OrderAddRequest;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.MarginDecoration;
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

    SharedPref sharedPref;
    EmployeeModel employeeModel;
    PaynetModel paynetModel;
    OptionAmountAdapter adapter;
    List<OptionAmount> optionAmounts;
    OptionAmount mOptionAmount;

    @BindView(R.id.rvPayment)
    RecyclerView rvPayment;
    OptionPaymentAdapter optionPaymentAdapter;

    PaymentModel paymentModel;

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
        initAdapter();
    }
    private void initAdapter(){
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

        if (optionPaymentAdapter==null) optionPaymentAdapter = new OptionPaymentAdapter(getContext(), item -> {
            this.paymentModel = item;
        });
        rvPayment.setAdapter(optionPaymentAdapter);
        rvPayment.addItemDecoration(new MarginDecoration(20,2));

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

    @OnClick({R.id.iv_back, R.id.btn_ok,R.id.rootView})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                mPresenter.back();
                break;
            case R.id.btn_ok:
                ok();
                break;
            case R.id.rootView:
                AppUtils.hideKeyboard(v);
                break;
        }
    }

    private void ok() {
        if (TextUtils.isEmpty(edt_amount.getText())) {
            Toast.showToast(requireContext(), "Bạn chưa nhập Số tiền");
            return;
        }

        OrderAddRequest req = new OrderAddRequest();
        if (paymentModel==null)  req.setProviderCode(Constants.PROVIDER_VIETTEL);
        else {
            switch (paymentModel.getId()){
                case Constants.PAYMENT_VIETTEL_PAY:
                    req.setProviderCode(Constants.PROVIDER_VIETTEL);
                    break;
                case Constants.PAYMENT_ZALO_PAY:
                    req.setProviderCode(Constants.PROVIDER_ZALO);
                    break;
                case Constants.PAYMENT_SHOPPE_PAY:
                    req.setProviderCode(Constants.PROVIDER_SHOPPE);
                    break;
                case Constants.PAYMENT_VN_PAY:
                    req.setProviderCode(Constants.PROVIDER_VN_PAY);
                    break;
                case Constants.PAYMENT_MOCA:
                    req.setProviderCode(Constants.PROVIDER_MOCA);
                    break;
                case Constants.PAYMENT_VIETQR:
                    req.setProviderCode(Constants.PROVIDER_VIETQR);
                    break;
            }
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

        if (paymentModel==null) req.setPaymentType(Constants.PAYMENT_TYPE_VIETTEL);
        else {
            switch (paymentModel.getId()){
                case Constants.PAYMENT_VIETTEL_PAY:
                    req.setPaymentType(Constants.PAYMENT_TYPE_VIETTEL);
                    break;
                case Constants.PAYMENT_ZALO_PAY:
                    req.setPaymentType(Constants.PAYMENT_TYPE_ZALO);
                    break;
                case Constants.PAYMENT_SHOPPE_PAY:
                    req.setPaymentType(Constants.PAYMENT_TYPE_SHOPEE);
                    break;
                case Constants.PAYMENT_VN_PAY:
                    req.setPaymentType(Constants.PAYMENT_TYPE_VN_PAY);
                    break;
                case Constants.PAYMENT_MOCA:
                    req.setPaymentType(Constants.PAYMENT_TYPE_MOCA);
                    break;
                case Constants.PAYMENT_VIETQR:
                    req.setPaymentType(Constants.PAYMENT_TYPE_VIETQR);
                    break;
            }
        }
        req.setPaymentCate(2);
        req.setMerchantID(paynetModel.getMerchantID());
        new PaymentPresenter((ContainerView) requireActivity(), req).pushView();
    }


}
