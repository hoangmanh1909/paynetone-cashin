package com.paynetone.counter.functions.payment.qr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.base.viper.ViewFragment;
import com.paynetone.counter.R;
import com.paynetone.counter.dialog.BottomDialog;
import com.paynetone.counter.main.MainActivity;
import com.paynetone.counter.model.request.OrderAddRequest;
import com.paynetone.counter.model.response.OrderAddResponse;
import com.paynetone.counter.model.OrderModel;
import com.paynetone.counter.utils.BitmapUtils;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.NumberUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class QRFragment extends ViewFragment<QRContract.Presenter> implements QRContract.View {
    private final String TAG = "QRFragment";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.img_qr_code)
    ImageView img_qr_code;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_providers_logo)
    TextView tv_providers_logo;
    @BindView(R.id.img_logo)
    ImageView img_logo;
    @BindView(R.id.tv_providers)
    TextView tv_providers;

    OrderAddResponse orderAddResponse;
    OrderAddRequest mOrderAddRequest;

    public static QRFragment getInstance() {
        return new QRFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qr;
    }

    @Override
    public void initLayout() {
        super.initLayout();

        if (mPresenter != null) {
            orderAddResponse = mPresenter.getOrderAddResponse();
            mOrderAddRequest = mPresenter.getOrderAddRequest();
            tvCode.setText(getResources().getString(R.string.str_code_order).concat( orderAddResponse.getOrderCode()));
            tv_amount.setText(NumberUtils.formatPriceNumber(mOrderAddRequest.getAmount()) + "đ");
            if (mOrderAddRequest.getProviderCode().equals(Constants.PROVIDER_ZALO)) {
                img_logo.setImageResource(R.drawable.zalopay);
                tv_providers.setText(getResources().getString(R.string.str_zalo_pay));
                tv_providers_logo.setText(getResources().getString(R.string.str_zalo_pay));
            } else if(mOrderAddRequest.getProviderCode().equals(Constants.PROVIDER_VIETTEL)) {
                img_logo.setImageResource(R.drawable.viettel_money);
                tv_providers.setText(getResources().getString(R.string.str_qr_viettel));
                tv_providers_logo.setText(getResources().getString(R.string.str_viettel_money));
            }else {
                img_logo.setImageResource(R.drawable.ic_shoppe_pay);
                tv_providers.setText(getResources().getString(R.string.str_shoppe_pay));
                tv_providers_logo.setText(getResources().getString(R.string.str_shoppe_pay));
            }
            Bitmap bitmap = BitmapUtils.generateQRBitmap(orderAddResponse.getReturnURL());
            img_qr_code.setImageBitmap(bitmap);
        }
    }

    @OnClick({R.id.iv_back, R.id.btn_ok, R.id.iv_home})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                mPresenter.back();
                break;
            case R.id.btn_ok:
                mPresenter.getByCode();
                break;
            case R.id.iv_home:
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void showOrder(OrderModel order) {
        BottomDialog bottomDialog = new BottomDialog(order);
        bottomDialog.setCancelable(false);
        bottomDialog.show(getChildFragmentManager(), TAG);
    }
}
