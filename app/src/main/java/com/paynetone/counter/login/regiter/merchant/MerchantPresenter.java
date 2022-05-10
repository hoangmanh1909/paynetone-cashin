package com.paynetone.counter.login.regiter.merchant;

import android.app.Activity;
import android.content.Context;
import android.media.midi.MidiOutputPort;

import com.core.base.viper.Presenter;
import com.core.base.viper.interfaces.ContainerView;
import com.google.gson.reflect.TypeToken;
import com.paynetone.counter.interfaces.RegisterPassData;
import com.paynetone.counter.model.BankModel;
import com.paynetone.counter.model.DictionaryModel;
import com.paynetone.counter.model.EmployeeModel;
import com.paynetone.counter.model.MerchantModel;
import com.paynetone.counter.model.RegisterPassDataModel;
import com.paynetone.counter.model.SimpleResult;
import com.paynetone.counter.model.request.MerchantAddNewRequest;
import com.paynetone.counter.model.response.DictionaryBusinessServiceResponse;
import com.paynetone.counter.model.response.PostImageResponse;
import com.paynetone.counter.network.CommonCallback;
import com.paynetone.counter.network.NetWorkController;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.SharedPref;
import com.paynetone.counter.utils.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MerchantPresenter extends Presenter<MerchantContract.View, MerchantContract.Interactor>
        implements MerchantContract.Presenter {

    EmployeeModel employeeModel;
    SharedPref sharedPref;
    RegisterPassData registerPassData;
    String mode;
    Activity activity;

    public MerchantPresenter(ContainerView containerView, String mode, RegisterPassData registerPassData) {
        super(containerView);
        this.activity = (Activity) containerView;
        this.mode = mode;
        this.registerPassData = registerPassData;
    }

    @Override
    public void start() {
        sharedPref = new SharedPref(activity);
        employeeModel = sharedPref.getEmployeeModel();

        getBusinessServices();
        getProvinces();
        getBank();


    }

    private void getByMobileNumber() {
        mInteractor.getByMobileNumber(employeeModel.getMobileNumber(), new CommonCallback<SimpleResult>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    MerchantModel model = NetWorkController.getGson().fromJson(response.body().getData(), MerchantModel.class);
                    mView.showMerchant(model);
                } else {
                    mView.showAlertDialog(response.body().getMessage());
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
            }
        });
    }

    private void getBusinessServices() {
        mInteractor.getBusinessServices(new CommonCallback<SimpleResult>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    List<DictionaryBusinessServiceResponse> models = NetWorkController.getGson().fromJson(response.body().getData(),
                            new TypeToken<List<DictionaryBusinessServiceResponse>>() {
                            }.getType());
                    mView.showBusinessService(models);
                } else {
                    mView.showAlertDialog(response.body().getMessage());
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
            }
        });
    }

    private void getBank() {
        mInteractor.getBanks(new CommonCallback<SimpleResult>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    if (mode.equals(Constants.MERCHANT_MODE_VIEW))
                        getByMobileNumber();
                    List<BankModel> models = NetWorkController.getGson().fromJson(response.body().getData(),
                            new TypeToken<List<BankModel>>() {
                            }.getType());
                    mView.showBanks(models);
                } else {
                    mView.showAlertDialog(response.body().getMessage());
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
            }
        });
    }

    private void getProvinces() {
        mInteractor.getProvinces(new CommonCallback<SimpleResult>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    List<DictionaryModel> models = NetWorkController.getGson().fromJson(response.body().getData(),
                            new TypeToken<List<DictionaryModel>>() {
                            }.getType());
                    mView.showProvinces(models);
                } else {
                    mView.showAlertDialog(response.body().getMessage());
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
            }
        });
    }

    @Override
    public MerchantContract.Interactor onCreateInteractor() {
        return new MerchantInteractor(this);
    }

    @Override
    public MerchantContract.View onCreateView() {
        return MerchantFragment.getInstance();
    }

    @Override
    public String getMode() {
        return mode;
    }

    @Override
    public RegisterPassDataModel getPassData() {
        return registerPassData.getData();
    }

    @Override
    public void getDistricts(int provinceID) {
        mInteractor.getDistricts(provinceID, new CommonCallback<SimpleResult>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    List<DictionaryModel> models = NetWorkController.getGson().fromJson(response.body().getData(),
                            new TypeToken<List<DictionaryModel>>() {
                            }.getType());
                    mView.showDistricts(models);
                } else {
                    mView.showAlertDialog(response.body().getMessage());
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
            }
        });
    }

    @Override
    public void getWards(int districtID) {
        mInteractor.getWards(districtID, new CommonCallback<SimpleResult>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    List<DictionaryModel> models = NetWorkController.getGson().fromJson(response.body().getData(),
                            new TypeToken<List<DictionaryModel>>() {
                            }.getType());
                    mView.showWards(models);
                } else {
                    mView.showAlertDialog(response.body().getMessage());
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
            }
        });
    }

    @Override
    public void postImage(String filePath) {
        CommonCallback<SimpleResult> callback = new CommonCallback<SimpleResult>((Context) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);
                mView.hideProgress();

                if ("00".equals(response.body().getErrorCode())) {

                    PostImageResponse fileInfo = NetWorkController.getGson().fromJson(response.body().getData(), PostImageResponse.class);
                    if (!response.body().getData().isEmpty()) {
                        mView.showImage(fileInfo.getFileName());
                    }
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
                mView.hideProgress();
            }
        };

        mInteractor.postImage(filePath, callback);
    }

    @Override
    public void addMerchant(MerchantAddNewRequest request) {
        mView.showProgress();
        mInteractor.addMerchant(request, new CommonCallback<SimpleResult>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    if (mode.equals(Constants.MERCHANT_MODE_EDIT)) {
                        Toast.showToast(activity,"Cập nhật hồ sơ Merchant thành công");
                        mView.gotoSplashWhenUpdateMerchant();
                    } else
                        mView.showSuccess();

                } else {
                    mView.showError(response.body().getMessage());
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
                mView.showError(message);
            }
        });
    }

    @Override
    public void editMerchant(MerchantAddNewRequest request) {
        mView.showProgress();
        mInteractor.editMerchant(request, new CommonCallback<SimpleResult>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    if (mode.equals(Constants.MERCHANT_MODE_EDIT) || mode.equals(Constants.MERCHANT_MODE_VIEW)) {
                        Toast.showToast(activity,"Cập nhật hồ sơ Merchant thành công");
                        back();
                    } else
                        mView.showSuccess();

                } else {
                    mView.showError(response.body().getMessage());
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
                mView.showError(message);
            }
        });
    }
}
