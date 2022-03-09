package com.paynetone.counter.functions.history;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.core.base.viper.ViewFragment;
import com.paynetone.counter.R;
import com.paynetone.counter.model.OrderModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HistoryFragment extends ViewFragment<HistoryContract.Presenter> implements HistoryContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.no_data)
    LinearLayout noData;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    HistoryAdapter mAdapter;
    List<OrderModel> mOrderModels;

    public static  HistoryFragment getInstance(){
        return  new HistoryFragment();
    }

    @Override
    public void initLayout() {
        super.initLayout();

        mOrderModels = new ArrayList<>();
        noData.setVisibility(View.GONE);

        recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HistoryAdapter(getContext(), mOrderModels);
        recycle.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        iv_back.setOnClickListener(view -> mPresenter.back());
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mPresenter.orderSearch();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    public void onDisplay() {
        super.onDisplay();

        if(mPresenter != null)
            mPresenter.orderSearch();
    }

    @Override
    public void showOrders(List<OrderModel> historyModels) {
        if (historyModels.size() > 0)
            noData.setVisibility(View.GONE);
        else
            noData.setVisibility(View.VISIBLE);

        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mOrderModels.clear();
        mOrderModels.addAll(historyModels);
        mAdapter.setItems(mOrderModels);
    }
}
