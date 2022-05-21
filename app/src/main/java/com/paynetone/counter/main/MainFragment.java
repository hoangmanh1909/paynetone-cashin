package com.paynetone.counter.main;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.paynetone.counter.R;
import com.paynetone.counter.home.HomePresenter;
import com.paynetone.counter.personal.PersonalPresenter;
import com.paynetone.counter.service.MyFirebaseMessagingService;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

public class MainFragment  extends ViewFragment<MainContract.Presenter> implements MainContract.View {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    private Fragment fragmentHome;
    private Fragment fragmentPersonal;
    FragmentPagerAdapter adapter;

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
                bottomNavigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //on page scroll state changed
            }
        });

        viewPager.setOffscreenPageLimit(1);

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.page_home:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.page_persional:
                    viewPager.setCurrentItem(1);
                    break;
            }
            return true;
        });
    }

    private Fragment getFragmentItem(int position) {
        switch (position) {
            case 0:
                if (fragmentHome == null) {
                    fragmentHome = new HomePresenter((ContainerView) getActivity()).getFragment();
                }
                return fragmentHome;
            case 1:
                if (fragmentPersonal == null) {
                    fragmentPersonal = new PersonalPresenter((ContainerView) getActivity()).getFragment();
                }
                return fragmentPersonal;
        }
        return new Fragment();
    }
}
