package com.paynetone.counter.functions.qr;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.paynetone.counter.utils.CurrencyTextWatcher;
import com.paynetone.counter.utils.InputFilterCharacter;
import com.paynetone.counter.utils.InputFilterCharacterNumber;
import com.paynetone.counter.utils.MarginDecoration;
import com.paynetone.counter.utils.NumberUtils;
import com.paynetone.counter.utils.SharedPref;
import com.paynetone.counter.utils.Toast;
import com.paynetone.counter.utils.Utils;

import java.sql.Array;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class QRDynamicFragment extends ViewFragment<QRDynamicContract.Presenter> implements QRDynamicContract.View {

    @BindView(R.id.edt_mobile_number)
    TextInputEditText edt_mobile_number;
    @BindView(R.id.edt_amount)
    EditText edt_amount;
    @BindView(R.id.tv_format_amount)
    AppCompatTextView tv_format_amount;
    @BindView(R.id.tv_error_amount)
    AppCompatTextView tv_error_amount;
    @BindView(R.id.edt_note)
    EditText edt_note;
    @BindView(R.id.recycle)
    RecyclerView recycle;

    SharedPref sharedPref;
    EmployeeModel employeeModel;
    PaynetModel paynetModel;
    OptionAmountAdapter adapter;
    List<OptionAmount> optionAmounts;
    OptionAmount mOptionAmount;

//    @BindView(R.id.rvPayment)
//    RecyclerView rvPayment;
//    OptionPaymentAdapter optionPaymentAdapter;

    @BindView(R.id.img_logo)
    AppCompatImageView imgLogo;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;


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

        if (mPresenter.getProviderResponse().getPaymentType() == Constants.PAYMENT_TYPE_VIETQR){ // không cho phép nhập tiếng việt
            InputFilter[] filters = new InputFilter[2];
            filters[0] = new InputFilterCharacter.LengthFilter(19); //Filter to 19 characters
            filters[1] = new InputFilterCharacter();
            edt_note.setFilters(filters);

        }
        InputFilter[] filters = new InputFilter[2];
        filters[0] = new InputFilterCharacterNumber.LengthFilter(11);
        filters[1] = new InputFilterCharacterNumber();
        edt_amount.setFilters(filters);

        tvTitle.setText(mPresenter.getProviderResponse().getName());
        Glide.with(imgLogo)
                .load(Utils.getUrlImage(mPresenter.getProviderResponse().getIcon()))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imgLogo);

        edt_amount.addTextChangedListener(new CurrencyTextWatcher(edt_amount));
        edt_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0) tv_format_amount.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack3));
                else tv_format_amount.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_400));

                if (s.toString().length() < 6 ) tv_error_amount.setVisibility(View.VISIBLE);
                else tv_error_amount.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

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
                    ((ViewHolder) holder).tv_amount.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    ((ViewHolder) holder).tv_amount.setBackground(ContextCompat.getDrawable(mContext,R.drawable.bg_card_amount_default));
                    ((ViewHolder) holder).tv_amount.setTypeface(Typeface.DEFAULT);
                }
            }
        };
        recycle.setAdapter(adapter);

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
        try {
            if (TextUtils.isEmpty(edt_amount.getText())) {
                Toast.showToast(requireContext(), "Bạn chưa nhập Số tiền");
                return;
            }
            int amount = Integer.parseInt(String.valueOf(edt_amount.getText()).replace(",", ""));
            if (amount<10000){
                Toast.showToast(requireContext(), "Số tiền giao dịch tối thiểu là 10.000đ");
                return;
            }
            if (amount>10000000){
                Toast.showToast(requireContext(), "Số tiền giao dịch tối đa là 10.000.000đ");
                return;
            }
            OrderAddRequest req = new OrderAddRequest();
            if(mPresenter.getProviderResponse().getPaymentType() ==Constants.PAYMENT_TYPE_VIETQR)
                req.setOrderDes(convertToStringEnglish(edt_note.getText().toString()));
            else req.setOrderDes(Objects.requireNonNull(edt_note.getText()).toString());
            req.setProviderCode(mPresenter.getProviderResponse().getCode());
            req.setMobileNumber(Objects.requireNonNull(edt_mobile_number.getText()).toString());
            req.setServiceID(1);
            req.setAmount(Integer.parseInt(String.valueOf(edt_amount.getText()).replace(",", "")));
            req.setFee(0);
            req.setTransAmount(Integer.parseInt(String.valueOf(edt_amount.getText()).replace(",", "")));
            req.setChannel(Constants.CHANNEL);
            req.setEmpID(employeeModel.getId());
            req.setPaynetID(employeeModel.getPaynetID());
            req.setPaymentType(mPresenter.getProviderResponse().getPaymentType());
            req.setTransCategory(1);
            req.setPaymentCate(2);
            req.setMerchantID(paynetModel.getMerchantID());
            req.setProviderAcntID(mPresenter.getProviderResponse().getId());
            mPresenter.orderAdd(req);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private String convertToStringEnglish(String str) { // convert string vietnamese to english
        try {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").toLowerCase(Locale.ROOT).replace("đ", "d");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
