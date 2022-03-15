package com.paynetone.counter.login.regiter.merchant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.core.base.log.Logger;
import com.core.base.viper.ViewFragment;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.textfield.TextInputEditText;
import com.paynetone.counter.BuildConfig;
import com.paynetone.counter.R;
import com.paynetone.counter.dialog.BankBottomDialog;
import com.paynetone.counter.dialog.BaseBottomDialog;
import com.paynetone.counter.interfaces.RegisterPassData;
import com.paynetone.counter.login.regiter.account.AccountFragment;
import com.paynetone.counter.model.BankModel;
import com.paynetone.counter.model.BaseDialogModel;
import com.paynetone.counter.model.DictionaryModel;
import com.paynetone.counter.model.MerchantModel;
import com.paynetone.counter.model.RegisterPassDataModel;
import com.paynetone.counter.model.request.MerchantAddNewRequest;
import com.paynetone.counter.model.response.DictionaryBusinessServiceResponse;
import com.paynetone.counter.utils.BitmapUtils;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.DialogHelper;
import com.paynetone.counter.utils.MediaUtils;
import com.paynetone.counter.utils.Toast;
import com.paynetone.counter.utils.Utils;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MerchantFragment extends ViewFragment<MerchantContract.Presenter> implements MerchantContract.View, BlockingStep {
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.btn_ok)
    Button btn_ok;

    @BindView(R.id.auto_business_service)
    AutoCompleteTextView auto_business_service;
    @BindView(R.id.auto_province)
    AutoCompleteTextView auto_province;
    @BindView(R.id.auto_district)
    AutoCompleteTextView auto_district;
    @BindView(R.id.auto_ward)
    AutoCompleteTextView auto_ward;
    @BindView(R.id.auto_bank)
    AutoCompleteTextView auto_bank;

    @BindView(R.id.edt_mobile_number)
    TextInputEditText edt_mobile_number;
    @BindView(R.id.edt_address)
    TextInputEditText edt_address;
    @BindView(R.id.edt_name)
    TextInputEditText edt_name;
    @BindView(R.id.edt_pid_number)
    TextInputEditText edt_pid_number;
    @BindView(R.id.edt_account_number)
    TextInputEditText edt_account_number;
    @BindView(R.id.edt_branch)
    TextInputEditText edt_branch;

    @BindView(R.id.img_before)
    SimpleDraweeView image_before;
    @BindView(R.id.img_after)
    SimpleDraweeView image_after;

    @BindView(R.id.rl_navigation_header)
    RelativeLayout rl_navigation_header;
    @BindView(R.id.rl_status)
    RelativeLayout rl_status;
    @BindView(R.id.tv_formality)
    TextView tv_formality;
    MerchantModel mMerchantModel;

    StepperLayout.OnNextClickedCallback mCallback;

    List<DictionaryBusinessServiceResponse> mBusinessServices = new ArrayList<>();
    List<BankModel> bankModels = new ArrayList<>();
    List<DictionaryModel> provinces = new ArrayList<>();
    List<DictionaryModel> districts = new ArrayList<>();
    List<DictionaryModel> wards = new ArrayList<>();

    BaseDialogModel mBusinessService;
    BankModel bankModel;
    DictionaryModel province;
    DictionaryModel district;
    DictionaryModel ward;

    boolean IsImageAfter = false;
    String fileImgBefore;
    String fileImgAfter;

    RegisterPassDataModel dataModel;
    String mode = Constants.MERCHANT_MODE_ADD;


    public static MerchantFragment getInstance() {
        return new MerchantFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_merchant;
    }

    @Override
    public void initLayout() {
        super.initLayout();
//        disableTextInput(true);
        rl_navigation_header.setVisibility(View.GONE);
        rl_status.setVisibility(View.GONE);
        btn_ok.setVisibility(View.GONE);
        if (mPresenter != null) {
            mode = mPresenter.getMode();
            if (mode.equals(Constants.MERCHANT_MODE_VIEW) || mode.equals(Constants.MERCHANT_MODE_EDIT)) {
                if (mode.equals(Constants.MERCHANT_MODE_EDIT)) {
                    dataModel = mPresenter.getPassData();
                    edt_mobile_number.setText(dataModel.getMobileNumber());
                    rl_status.setVisibility(View.GONE);
                } else
                    rl_status.setVisibility(View.VISIBLE);
                rl_navigation_header.setVisibility(View.VISIBLE);
                btn_ok.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.auto_business_service, R.id.auto_province, R.id.auto_district, R.id.auto_ward,
            R.id.auto_bank, R.id.img_before, R.id.img_after, R.id.iv_back, R.id.btn_ok})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.auto_business_service:
                selectBusinessService();
                break;
            case R.id.auto_province:
                selectProvince();
                break;
            case R.id.auto_district:
                selectDistrict();
                break;
            case R.id.auto_ward:
                selectWard();
                break;
            case R.id.auto_bank:
                selectBank();
                break;
            case R.id.img_before:
                IsImageAfter = false;
                capturePermission();
                break;
            case R.id.img_after:
                IsImageAfter = true;
                capturePermission();
                break;
            case R.id.iv_back:
                mPresenter.back();
                break;
            case R.id.btn_ok:
                addMerchant(false);
                break;
        }
    }

    private void selectBusinessService() {
        if (mBusinessServices.size() > 0) {
            List<BaseDialogModel> baseDialogModels = new ArrayList<>();
            for (DictionaryBusinessServiceResponse item : mBusinessServices) {
                BaseDialogModel baseDialogModel = new BaseDialogModel();
                baseDialogModel.setText(item.getName());
                baseDialogModel.setValue(item.getId().toString());
                baseDialogModels.add(baseDialogModel);
            }
            BaseBottomDialog baseDialog = new BaseBottomDialog(baseDialogModels, "Chọn Loại dịch vụ hàng hóa", bankModel -> {
                auto_business_service.setText(bankModel.getText());
                mBusinessService = bankModel;
            });
            baseDialog.show(getChildFragmentManager(), "WithDrawFragment");
        } else {
            Toast.showToast(getContext(), "Không tải được danh mục Loại dịch vụ hàng hóa");
        }
    }

    private void selectProvince() {
        if (provinces.size() > 0) {
            List<BaseDialogModel> baseDialogModels = new ArrayList<>();
            for (DictionaryModel item : provinces) {
                BaseDialogModel baseDialogModel = new BaseDialogModel();
                baseDialogModel.setText(item.getName());
                baseDialogModel.setValue(item.getId().toString());
                baseDialogModels.add(baseDialogModel);
            }
            BaseBottomDialog baseDialog = new BaseBottomDialog(baseDialogModels, "Chọn Tỉnh/Thành phố", bankModel -> {
                auto_province.setText(bankModel.getText());
                province = new DictionaryModel();
                province.setId(Integer.parseInt(bankModel.getValue()));
                province.setName(bankModel.getText());
                mPresenter.getDistricts(Integer.parseInt(bankModel.getValue()));
            });
            baseDialog.show(getChildFragmentManager(), "WithDrawFragment");
        } else {
            Toast.showToast(getContext(), "Không tải được danh mục Tỉnh/Thành phố");
        }
    }

    private void selectDistrict() {
        if (districts.size() > 0) {
            List<BaseDialogModel> baseDialogModels = new ArrayList<>();
            for (DictionaryModel item : districts) {
                BaseDialogModel baseDialogModel = new BaseDialogModel();
                baseDialogModel.setText(item.getName());
                baseDialogModel.setValue(item.getId().toString());
                baseDialogModels.add(baseDialogModel);
            }
            BaseBottomDialog baseDialog = new BaseBottomDialog(baseDialogModels, "Chọn Quận/Huyện", bankModel -> {
                auto_district.setText(bankModel.getText());
                district = new DictionaryModel();
                district.setId(Integer.parseInt(bankModel.getValue()));
                district.setName(bankModel.getText());
                mPresenter.getWards(Integer.parseInt(bankModel.getValue()));
            });
            baseDialog.show(getChildFragmentManager(), "WithDrawFragment");
        } else {
            Toast.showToast(getContext(), "Không tải được danh mục Quận/Huyện");
        }
    }

    private void selectWard() {
        if (wards.size() > 0) {
            List<BaseDialogModel> baseDialogModels = new ArrayList<>();
            for (DictionaryModel item : wards) {
                BaseDialogModel baseDialogModel = new BaseDialogModel();
                baseDialogModel.setText(item.getName());
                baseDialogModel.setValue(item.getId().toString());
                baseDialogModels.add(baseDialogModel);
            }
            BaseBottomDialog baseDialog = new BaseBottomDialog(baseDialogModels, "Chọn Xã/Phường", bankModel -> {
                auto_ward.setText(bankModel.getText());
                ward = new DictionaryModel();
                ward.setId(Integer.parseInt(bankModel.getValue()));
                ward.setName(bankModel.getText());
            });
            baseDialog.show(getChildFragmentManager(), "WithDrawFragment");
        } else {
            Toast.showToast(getContext(), "Không tải được danh mục Xã/Phường");
        }
    }

    private void selectBank() {
        if (bankModels.size() > 0) {
            BankBottomDialog baseDialog = new BankBottomDialog(bankModels, bankModel -> {
                auto_bank.setText(bankModel.getShortName());
                this.bankModel = bankModel;
            });
            baseDialog.show(getChildFragmentManager(), "WithDrawFragment");
        } else {
            Toast.showToast(getContext(), "Không tải được danh mục Ngân hàng");
        }
    }

    private void addMerchant(boolean isPass) {
        if (!isPass) {
            if (TextUtils.isEmpty(edt_mobile_number.getText())) {
                Toast.showToast(requireContext(), "Bạn chưa nhập Số điện thoại đăng ký kinh doanh");
                return;
            }
            if (mBusinessService == null) {
                Toast.showToast(requireContext(), "Bạn chưa Chọn Loại dịch vụ hàng hóa");
                return;
            }
//        if (province == null) {
//            return new VerificationError("Bạn chưa Chọn Tỉnh/Thành phố");
//        }
//        if (district == null) {
//            return new VerificationError("Bạn chưa Chọn Quận/Huyện");
//        }
//        if (ward == null) {
//            return new VerificationError("Bạn chưa Chọn Phường/Xã");
//        }
            if (TextUtils.isEmpty(edt_name.getText())) {
                Toast.showToast(requireContext(), "Bạn chưa nhập Họ tên người đại diện");
                return;
            }
            if (TextUtils.isEmpty(edt_pid_number.getText())) {
                Toast.showToast(requireContext(), "Bạn chưa nhập Số giấy tờ tùy thân người đại diện");
                return;
            }
            if (TextUtils.isEmpty(edt_account_number.getText())) {
                Toast.showToast(requireContext(), "Bạn chưa nhập Số tài khoản thanh toán");
                return;
            }
            if (bankModels == null) {
                Toast.showToast(requireContext(), "Bạn chưa Chọn Ngân hàng thanh toán");
                return;
            }
            if (TextUtils.isEmpty(fileImgBefore)) {
                Toast.showToast(requireContext(), "Bạn chưa Chụp ảnh giấy tờ tùy thân mặt trước");
                return;
            }
            if (TextUtils.isEmpty(fileImgAfter)) {
                Toast.showToast(requireContext(), "Bạn chưa Chụp ảnh giấy tờ tùy thân mặt sau");
                return;
            }
        }


        MerchantAddNewRequest req = new MerchantAddNewRequest();
        req.setFormalityType("1");
        req.setBusinessType("3");
        req.setServiceType("1");
        req.setMobileNumber(dataModel.getMobileNumber());
        req.setName(dataModel.getMerchantName());
//        req.setEmail(dataModel.getEmail());
//        req.setProvinceID(province.getId());
//        req.setDistrictID(district.getId());
//        req.setWardID(ward.getId());
        if (!TextUtils.isEmpty(edt_address.getText()))
            req.setAddress(edt_address.getText().toString());
        req.setRepresentativeName(edt_name.getText().toString());
        req.setQROption("2");
        req.setIsSignContract("Y");
        req.setRepresentativePIDNumber(edt_pid_number.getText().toString());
        req.setRepresentativePIDImageBefore(fileImgBefore);
        req.setRepresentativePIDImageAfter(fileImgAfter);
        req.setBusinessMobile(edt_mobile_number.getText().toString());
        req.setBusinessServiceID(Integer.parseInt(mBusinessService.getValue()));
        req.setPaymentAccountBank(bankModel.getId().toString());
        if (!TextUtils.isEmpty(edt_branch.getText()))
            req.setPaymentAccountBranch(edt_branch.getText().toString());
        req.setPaymentAccountName(edt_name.getText().toString());
        req.setPaymentAccountNumber(edt_account_number.getText().toString());
        if (mode.equals(Constants.MERCHANT_MODE_VIEW)) {
            req.setiD(mMerchantModel.getId());
            mPresenter.editMerchant(req);
        } else
            mPresenter.addMerchant(req);
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (TextUtils.isEmpty(edt_mobile_number.getText())) {
            return new VerificationError("Bạn chưa nhập Số điện thoại đăng ký kinh doanh");
        }
        if (mBusinessService == null) {
            return new VerificationError("Bạn chưa Chọn Loại dịch vụ hàng hóa");
        }
//        if (province == null) {
//            return new VerificationError("Bạn chưa Chọn Tỉnh/Thành phố");
//        }
//        if (district == null) {
//            return new VerificationError("Bạn chưa Chọn Quận/Huyện");
//        }
//        if (ward == null) {
//            return new VerificationError("Bạn chưa Chọn Phường/Xã");
//        }
        if (TextUtils.isEmpty(edt_name.getText())) {
            return new VerificationError("Bạn chưa nhập Họ tên người đại diện");
        }
        if (TextUtils.isEmpty(edt_pid_number.getText())) {
            return new VerificationError("Bạn chưa nhập Số giấy tờ tùy thân người đại diện");
        }
        if (TextUtils.isEmpty(fileImgBefore)) {
            return new VerificationError("Bạn chưa Chụp ảnh giấy tờ tùy thân mặt trước");
        }
        if (TextUtils.isEmpty(fileImgAfter)) {
            return new VerificationError("Bạn chưa Chụp ảnh giấy tờ tùy thân mặt sau");
        }
        if (TextUtils.isEmpty(edt_account_number.getText())) {
            return new VerificationError("Bạn chưa nhập Số tài khoản thanh toán");
        }
        if (bankModels == null) {
            return new VerificationError("Bạn chưa Chọn Ngân hàng thanh toán");
        }

        addMerchant(true);
        return null;
    }

    @Override
    public void onSelected() {
        if (mPresenter != null) {
            dataModel = mPresenter.getPassData();
            edt_mobile_number.setText(dataModel.getMobileNumber());
        }
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Toast.showToast(getContext(), error.getErrorMessage());
    }

    @Override
    public void showBusinessService(List<DictionaryBusinessServiceResponse> models) {
        this.mBusinessServices.addAll(models);
    }

    @Override
    public void showProvinces(List<DictionaryModel> dictionaryModels) {
        this.provinces.addAll(dictionaryModels);
    }

    @Override
    public void showDistricts(List<DictionaryModel> dictionaryModels) {
        this.districts.clear();
        this.districts.addAll(dictionaryModels);
    }

    @Override
    public void showWards(List<DictionaryModel> dictionaryModels) {
        this.wards.clear();
        this.wards.addAll(dictionaryModels);
    }

    @Override
    public void showBanks(List<BankModel> bankModels) {
        this.bankModels.addAll(bankModels);
    }

    @Override
    public void showImage(String file) {
        if (!IsImageAfter)
            fileImgBefore = file;
        else
            fileImgAfter = file;
    }

    @Override
    public void showSuccess() {
        if (mCallback != null) {
            mCallback.goToNextStep();
        }
    }

    @Override
    public void showError(String message) {
        Toast.showToast(getContext(), message);
    }

    @Override
    public void showMerchant(MerchantModel model) {
//        disableTextInput(false);
//        if (!TextUtils.isEmpty(model.getBusinessServiceName()))
//            auto_business_service.setText(model.getBusinessServiceName());
        mMerchantModel = model;
        dataModel = new RegisterPassDataModel();
        dataModel.setMerchantName(model.getName());
        dataModel.setMobileNumber(model.getMobileNumber());
        mBusinessService = new BaseDialogModel();
        mBusinessService.setValue(model.getBusinessServiceID().toString());
        mBusinessService.setText(model.getBusinessServiceName());
        auto_business_service.setText(mBusinessService.getText());
//        auto_province.setText(model.getProvinceName());
//        auto_district.setText(model.getDistrictName());
//        auto_ward.setText(model.getWardName());
//        auto_bank.setText(model.getPaymentAccountBankName());
        bankModel = new BankModel();
        for (BankModel bankModel1 : bankModels) {
            if (bankModel1.getId().toString().equals(model.getPaymentAccountBank())) {
                bankModel = bankModel1;
                break;
            }
        }
        auto_bank.setText(bankModel.getShortName());
        edt_name.setText(model.getRepresentativeName());
        edt_account_number.setText(model.getPaymentAccountNumber());
        edt_address.setText(model.getAddress());
        edt_mobile_number.setText(model.getMobileNumber());
        edt_pid_number.setText(model.getRepresentativePIDNumber());
        edt_branch.setText(model.getPaymentAccountBranch());
        fileImgBefore = model.getRepresentativePIDImageBefore();
        fileImgAfter = model.getRepresentativePIDImageAfter();
        image_before.setImageURI(Utils.getUrlImage(model.getRepresentativePIDImageBefore()));
        image_after.setImageURI(Utils.getUrlImage(model.getRepresentativePIDImageAfter()));
        if (mMerchantModel.getFormalityType().equals(Constants.FORMALITY_TYPE_ONLINE)) tv_formality.setText(getResources().getText(R.string.str_formality_online));
        else tv_formality.setText(getResources().getText(R.string.str_formality_offline));
        switch (model.getStatus()) {
            case Constants.WAITING_APPROVAL:
                tv_status.setText(getResources().getText(R.string.str_waiting_approval));
                tv_status.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.order_status_w));
                break;
            case Constants.APPROVED:
                tv_status.setText(getResources().getText(R.string.str_approved));
                tv_status.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.order_status_s));
                break;
            case Constants.REFUSE:
                tv_status.setText(getResources().getText(R.string.str_refuse));
                tv_status.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.order_status_c));
                break;
        }
    }

    private void disableTextInput(boolean value) {
        auto_business_service.setEnabled(value);
        auto_province.setEnabled(value);
        auto_district.setEnabled(value);
        auto_ward.setEnabled(value);
        auto_bank.setEnabled(value);
        image_before.setEnabled(value);
        image_after.setEnabled(value);
        edt_name.setEnabled(value);
        edt_account_number.setEnabled(value);
        edt_address.setEnabled(value);
        edt_mobile_number.setEnabled(value);
        edt_branch.setEnabled(value);
        edt_pid_number.setEnabled(value);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            getActivity();
            if (resultCode == Activity.RESULT_OK) {
                attemptSendMedia(data.getData().getPath());
            }
        }
    }

    @SuppressLint("CheckResult")
    private void attemptSendMedia(String path_media) {
        File file = new File(path_media);
        Uri picUri = Uri.fromFile(new File(path_media));
        if (!IsImageAfter)
            image_before.setImageURI(picUri);
        else
            image_after.setImageURI(picUri);

        Observable.fromCallable(() -> {
            Uri uri = Uri.fromFile(new File(path_media));
            return BitmapUtils.processingBitmap(uri, getViewContext());
        }).subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .map(bitmap ->
                        BitmapUtils.saveImage(bitmap, file.getParent(), "pno" + file.getName(), Bitmap.CompressFormat.JPEG, 50)
                )
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                isSavedImage -> {
                    String path = file.getParent() + File.separator + "pno" + file.getName();

                    mPresenter.postImage(path);
                    if (file.exists())
                        file.delete();
                },
                onError -> Logger.e("error save image")
        );
    }

    private void capturePermission() {
        PermissionUtils.permission(PermissionConstants.CAMERA)
                .rationale(DialogHelper::showRationaleDialog)
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        MediaUtils.captureImage(MerchantFragment.this);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            DialogHelper.showOpenAppSettingDialog();
                        }
                    }
                })
                .request();
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        this.mCallback = callback;
//        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

    }
}
