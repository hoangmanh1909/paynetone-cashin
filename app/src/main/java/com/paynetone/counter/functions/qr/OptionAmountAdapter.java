package com.paynetone.counter.functions.qr;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.core.base.adapter.RecyclerBaseAdapter;
import com.core.widget.BaseViewHolder;
import com.paynetone.counter.R;
import com.paynetone.counter.model.HomeModel;
import com.paynetone.counter.model.OptionAmount;

import java.util.List;

import butterknife.BindView;

public class OptionAmountAdapter extends RecyclerBaseAdapter {
    Activity activity;

    public OptionAmountAdapter(Context context, List<OptionAmount> optionAmounts) {
        super(context, optionAmounts);

        activity = (Activity) context;
    }

    @NonNull
    @Override
    public OptionAmountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OptionAmountAdapter.ViewHolder(inflateView(parent, R.layout.item_amount));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder((BaseViewHolder) holder, position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_amount)
        public TextView tv_amount;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindView(Object model, int position) {
            OptionAmount item = (OptionAmount)model;
            tv_amount.setText(item.getText());
        }

    }
}
