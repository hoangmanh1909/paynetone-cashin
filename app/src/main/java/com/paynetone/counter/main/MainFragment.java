package com.paynetone.counter.main;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;


import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paynetone.counter.R;
import android.view.View;

import com.paynetone.counter.dialog.FilterHistoryPaymentDialog;
import com.paynetone.counter.functions.outward.OutwardActivity;
import com.paynetone.counter.functions.service.ServiceFragment;
import com.paynetone.counter.functions.service.ServicePresenter;
import com.paynetone.counter.functions.withdraw.WithDrawActivity;
import com.paynetone.counter.home.HomeFragment;
import com.paynetone.counter.home.HomePresenter;
import com.paynetone.counter.model.EmployeeModel;
import com.paynetone.counter.model.MerchantBalance;
import com.paynetone.counter.model.PaynetModel;
import com.paynetone.counter.model.TabMainModel;
import com.paynetone.counter.model.request.GetProviderResponse;
import com.paynetone.counter.personal.PersonalActivity;

import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.NumberUtils;
import com.paynetone.counter.utils.SharedPref;
import com.paynetone.counter.widgets.SwipeToHideLayout;
import com.paynetone.counter.widgets.ViewPagerFixed;
import com.paynetone.counter.widgets.banner.BannerLayout;
import com.paynetone.counter.widgets.banner.LocalDataAdapter;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment  extends ViewFragment<MainContract.Presenter> implements MainContract.View, ViewPagerFixed.OnSwipeListener{
    @BindView(R.id.view_pager)
    ViewPagerFixed viewPager;
    @BindView(R.id.bottom_navigation)
    SwipeToHideLayout bottomNavigation;

    private Fragment fragmentHome;
    private Fragment fragmentService;
    FragmentPagerAdapter adapter;

    @BindView(R.id.img_tab_one)
    AppCompatImageView imgTabOne;
    @BindView(R.id.title_tab_one)
    AppCompatTextView titleTabOne;

    @BindView(R.id.img_tab_two)
    AppCompatImageView imgTabTwo;
    @BindView(R.id.title_tab_two)
    AppCompatTextView titleTabTwo;

    @BindView(R.id.tab_layout_one)
    LinearLayout tabLayoutOne;
    @BindView(R.id.tab_layout_two)
    LinearLayout tabLayoutTwo;
    @BindView(R.id.tv_money_waiting)
    AppCompatTextView tvMoneyWaiting;
    @BindView(R.id.tv_money)
    AppCompatTextView tvMoney;

    @BindView(R.id.layout_money_waiting)
    ConstraintLayout layoutMoneyWaiting;
    @BindView(R.id.layout_money)
    ConstraintLayout layoutMoney;

    @BindView(R.id.layout_personal)
    LinearLayout layoutPersonal;
    @BindView(R.id.tv_han_muc)
    AppCompatTextView tvHanMuc;
    @BindView(R.id.collapsing)
    CollapsingToolbarLayout collapsing;
    @BindView(R.id.banner_layout)
    BannerLayout  banner_layout;
    @BindView(R.id.layout_banner)
    ConstraintLayout layout_banner;
    @BindView(R.id.card_bottom)
    CardView card_bottom;

    long amountP = 0;

    SharedPref sharedPref;
    PaynetModel paynetModel;
    EmployeeModel employeeModel;

    List<TabMainModel> tabMainModels;

    public static MainFragment getInstance() {
        return new MainFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void initLayout() {
        super.initLayout();

        sharedPref = new SharedPref(requireActivity());
        paynetModel = sharedPref.getPaynet();
        employeeModel = sharedPref.getEmployeeModel();
        tabMainModels = sharedPref.getTabMain();
        initNaviBottom();

//        new ConfirmPasswordDialog(requireContext()).show(getChildFragmentManager(),"Main");
        try {
            if (sharedPref.isMerchantAdmin()){
                collapsing.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        adapter = new FragmentPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            @Override
            public Fragment getItem(int position) {
                return getFragmentItem(position);
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    imgTabOne.setSelected(true);
                    titleTabOne.setSelected(true);
                    imgTabTwo.setSelected(false);
                    titleTabTwo.setSelected(false);
                }else {
                    imgTabOne.setSelected(false);
                    titleTabOne.setSelected(false);
                    imgTabTwo.setSelected(true);
                    titleTabTwo.setSelected(true);
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {
                //on page scroll state changed
            }
        });

        viewPager.setOffscreenPageLimit(1);
        viewPager.setOnSwipeListener(this);

        tabLayoutOne.setOnClickListener(view -> viewPager.setCurrentItem(0));
        tabLayoutTwo.setOnClickListener(view -> viewPager.setCurrentItem(1));

        imgTabOne.setSelected(true);
        titleTabOne.setSelected(true);
        setupRecyclerViewBanner();

    }

    private Fragment getFragmentItem(int position) {

        switch (position) {
            case 0:
                if (tabMainModels!=null){
                    if (tabMainModels.get(0).getType()==Constants.TYPE_TAB_MAIN_QR){
                        if (fragmentHome == null) {
                            fragmentHome = new HomePresenter((ContainerView) getActivity()).getFragment();
                        }
                        return fragmentHome;
                    }else {
                        if (fragmentService == null) {
                            fragmentService = new ServicePresenter((ContainerView) getActivity()).getFragment();
                        }
                        return fragmentService;
                    }
                }
                if (fragmentHome == null) {
                    fragmentHome = new HomePresenter((ContainerView) getActivity()).getFragment();
                }
                return fragmentHome;
            case 1:
                if (tabMainModels!=null){
                    if (tabMainModels.get(1).getType()== Constants.TYPE_TAB_MAIN_SERVICE){
                        if (fragmentService == null) {
                            fragmentService = new ServicePresenter((ContainerView) getActivity()).getFragment();
                        }
                        return fragmentService;

                    }else {
                        if (fragmentHome == null) {
                            fragmentHome = new HomePresenter((ContainerView) getActivity()).getFragment();
                        }
                        return fragmentHome;
                    }
                }
                if (fragmentService == null) {
                    fragmentService = new ServicePresenter((ContainerView) getActivity()).getFragment();
                }
                return fragmentService;
        }
        return new Fragment();
    }
    private void initNaviBottom(){
        if (tabMainModels!=null){
            if (tabMainModels.get(0).getType()==1){
               imgTabOne.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.bg_ic_qr_selector));
               titleTabOne.setText(getResources().getText(R.string.str_title_tab_qr));
               imgTabTwo.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.bg_ic_service_selector));
               titleTabTwo.setText(getResources().getText(R.string.str_title_tab_service));
            }else {
                imgTabOne.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.bg_ic_service_selector));
                titleTabOne.setText(getResources().getText(R.string.str_title_tab_service));
                imgTabTwo.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.bg_ic_qr_selector));
                titleTabTwo.setText(getResources().getText(R.string.str_title_tab_qr));

            }
        }
    }
    private void setupRecyclerViewBanner() {
        banner_layout.setAdapter(new LocalDataAdapter());
        banner_layout.setAutoPlaying(true);

    }
    @OnClick({R.id.img_personal, R.id.layout_money,R.id.layout_money_waiting})
    public void OnClick(View view){
        if (!isClickAble()) return;
        switch (view.getId()){
            case R.id.img_personal:{
                Intent intent = new Intent(requireContext(), PersonalActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_money:{
                Intent intent = new Intent(requireActivity(), WithDrawActivity.class);
                intent.putExtra(Constants.AMOUNT_OUTWARD, amountP);
                startActivity(intent);
                break;
            }
            case R.id.layout_money_waiting:{
                Intent intent = new Intent(requireActivity(), OutwardActivity.class);
                startActivity(intent);
                break;
            }
        }

    }

    @Override
    public void showBalance(List<MerchantBalance> merchantBalances) {
        for (MerchantBalance merchantBalance : merchantBalances) {
            if (merchantBalance.getAccountType().equals("W")) {
                tvMoneyWaiting.setText(NumberUtils.formatPriceNumber(merchantBalance.getBalance()) + " đ");
            } else if (merchantBalance.getAccountType().equals("P")) {
                amountP = merchantBalance.getBalance();
                tvMoney.setText(NumberUtils.formatPriceNumber(merchantBalance.getBalance()) + " đ");
            }else if (merchantBalance.getAccountType().equals("S")){
                tvHanMuc.setText(NumberUtils.formatPriceNumber(merchantBalance.getBalance()) + " VND");
            }
        }
    }

    @Override
    public void showSuccessProvider(ArrayList<GetProviderResponse> responses) {
        ((HomeFragment) fragmentHome).initAdapter(responses);
        ((ServiceFragment) fragmentService).initAdapter(responses);

    }

    @Override
    public void onDisplay() {
        if (mPresenter != null) mPresenter.getBalance();
    }

    @Override
    public void onHideBottomNavigator() {
        //hide bottom navigation
//        bottomNavigation.hide();
    }

    @Override
    public void onShowBottomNavigator() {
        //show bottom navigator
//       bottomNavigation.show();
    }
}
