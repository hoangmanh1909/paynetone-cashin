package com.paynetone.counter.home;

import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.core.base.viper.ViewFragment;
import com.paynetone.counter.R;
import com.paynetone.counter.functions.history.HistoryActivity;
import com.paynetone.counter.functions.outward.OutwardActivity;
import com.paynetone.counter.functions.qr.QRDynamicActivity;
import com.paynetone.counter.functions.withdraw.WithDrawActivity;
import com.paynetone.counter.model.HomeModel;
import com.paynetone.counter.model.MerchantBalance;
import com.paynetone.counter.model.PaynetModel;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.NumberUtils;
import com.paynetone.counter.utils.SharedPref;
import com.paynetone.counter.utils.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class HomeFragment extends ViewFragment<HomeContract.Presenter> implements HomeContract.View {

    @BindView(R.id.recycle)
    RecyclerView recyclerView;
    @BindView(R.id.tv_amount_w)
    TextView tv_amount_w;
    @BindView(R.id.tv_amount_p)
    TextView tv_amount_p;
    @BindView(R.id.ll_balance)
    LinearLayout ll_balance;
    @BindView(R.id.rl_withdraw)
    RelativeLayout rl_withdraw;
    @BindView(R.id.rl_outward)
    RelativeLayout rl_outward;

    HomeAdapter adapter;
    List<HomeModel> homeModels = new ArrayList<>();
    SharedPref sharedPref;
    PaynetModel paynetModel;
    long amountP = 0;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initLayout() {
        super.initLayout();

        this.itemHome();
        sharedPref = new SharedPref(requireActivity());
        paynetModel = sharedPref.getPaynet();

        if (paynetModel.getBusinessType() == 3) {
            String mode = sharedPref.getString(Constants.KEY_ANDROID_PAYMENT_MODE,"");
            if(mode.equals(Constants.ANDROID_PAYMENT_MODE_SHOW)){
                ll_balance.setVisibility(View.VISIBLE);
            }
        } else {
            ll_balance.setVisibility(View.GONE);
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new HomeAdapter(getContext(), homeModels,(homeModel, position) -> {
            if (homeModel.getServiceCode().equals(Constants.HOME_QR)) {
                Intent intent = new Intent(requireActivity(), QRDynamicActivity.class);
                startActivity(intent);
            } else if (homeModel.getServiceCode().equals(Constants.HOME_HISTORY)) {
                Intent intent = new Intent(requireActivity(), HistoryActivity.class);
                startActivity(intent);
            }
        }) ;

        recyclerView.setAdapter(adapter);

        rl_withdraw.setOnClickListener(view -> {
//            if (amountP > 0) {
                Intent intent = new Intent(requireActivity(), WithDrawActivity.class);
                intent.putExtra(Constants.AMOUNT_OUTWARD, amountP);
                startActivity(intent);
//            } else {
//                Toast.showToast(requireContext(), "Tài khoản đã đối soát không đủ số dư rút tiền");
//            }
        });
        rl_outward.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), OutwardActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mPresenter != null)
            mPresenter.getBalance();
    }

    private void itemHome() {
        Resources res = getResources();

        HomeModel homeModel = new HomeModel();

        homeModel.setLogo(R.drawable.ic_qr_dong);
        homeModel.setServiceCode(Constants.HOME_QR);
        homeModel.setTitle(res.getString(R.string.home_qr_online));
        homeModels.add(homeModel);

        homeModel = new HomeModel();
        homeModel.setLogo(R.drawable.ic_history);
        homeModel.setServiceCode(Constants.HOME_HISTORY);
        homeModel.setTitle(res.getString(R.string.home_history));
        homeModels.add(homeModel);
    }

    @Override
    public void showBalance(List<MerchantBalance> merchantBalances) {
        for (MerchantBalance merchantBalance : merchantBalances) {
            if (merchantBalance.getAccountType().equals("W")) {
                tv_amount_w.setText(NumberUtils.formatPriceNumber(merchantBalance.getBalance()) + " đ");
            } else if (merchantBalance.getAccountType().equals("P")) {
                amountP = merchantBalance.getBalance();
                tv_amount_p.setText(NumberUtils.formatPriceNumber(merchantBalance.getBalance()) + " đ");
            }
        }
    }
}
