package com.paynetone.counter.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paynetone.counter.BuildConfig;
import com.paynetone.counter.model.RequestObject;
import com.paynetone.counter.model.SimpleResult;
import com.paynetone.counter.model.request.BaseRequest;
import com.paynetone.counter.model.request.EmployeeAddNewRequest;
import com.paynetone.counter.model.request.LoginRequest;
import com.paynetone.counter.model.request.MerchantAddNewRequest;
import com.paynetone.counter.model.request.OrderAddRequest;
import com.paynetone.counter.model.request.OrderGetByCodeRequest;
import com.paynetone.counter.model.request.OrderSearchRequest;
import com.paynetone.counter.model.request.UpdatePasswordRequest;
import com.paynetone.counter.model.request.WithdrawRequest;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.RSAUtil;
import com.paynetone.counter.utils.Utils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetWorkController {
    private static volatile PaynetOneApi apiBuilder;
    private static volatile PaynetOneApi apiBuilderImage;

    private NetWorkController() {

    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    private static PaynetOneApi getAPIBuilder() {
        // mPrivateKeySignature = Constants.PRIVATE_KEY;
        // mApiKey = Constants.API_KEY;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(Utils.getUnsafeOkHttpClient(120, 120, "UGF5bmV0T25lOkdhdGV3YXlAMjEyMSFAIw=="))
                .build();
        apiBuilder = retrofit.create(PaynetOneApi.class);
        return apiBuilder;
    }

    private static PaynetOneApi getAPIBuilderImage() {
        if (apiBuilderImage == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.IMAGE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(Utils.getUnsafeOkHttpClient(120, 120,  "UGF5bmV0T25lOkRhdGFGaWxlQDIxMjEhQCM="))
                    .build();
            apiBuilderImage = retrofit.create(PaynetOneApi.class);
        }
        return apiBuilderImage;
    }

    public static void postImage(String filePath, CommonCallback<SimpleResult> callback) {
        try {
            File file = new File(filePath);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("pid_image", "file_pid_image.jpg", reqFile);
            Call<SimpleResult> call = getAPIBuilderImage().postImage(body);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void login(LoginRequest loginRequest, CommonCallback<SimpleResult> callback) {
        try {
            String data = getGson().toJson(loginRequest);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_EMP_LOGIN, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void orderAdd(OrderAddRequest request, CommonCallback<SimpleResult> callback) {
        try {
            String data = getGson().toJson(request);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_ORDER_ADD_NEW, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getByOrderCode(String orderCode, CommonCallback<SimpleResult> callback) {
        try {
            OrderGetByCodeRequest request = new OrderGetByCodeRequest();
            request.setCode(orderCode);
            String data = getGson().toJson(request);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_ORDER_GET_BY_CODE, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void orderSearch(OrderSearchRequest request, CommonCallback<SimpleResult> callback) {
        try {
            String data = getGson().toJson(request);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_ORDER_SEARCH, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getPaynet(BaseRequest request, CommonCallback<SimpleResult> callback) {
        try {
            String data = getGson().toJson(request);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_PAYNET_GET_BY_ID, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getBalance(BaseRequest request, CommonCallback<SimpleResult> callback) {
        try {
            String data = getGson().toJson(request);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_MERCHANT_GET_BALANCE, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getBank(CommonCallback<SimpleResult> callback) {
        try {
            String signature = RSAUtil.signature("");
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_DIC_GET_BANK, "", "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getWallet(CommonCallback<SimpleResult> callback){
        try {
            String signature = RSAUtil.signature("");
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.DIC_GET_WALLET, "", "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getBusinessServices(CommonCallback<SimpleResult> callback) {
        try {
            String signature = RSAUtil.signature("");
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_DIC_BUSINESS_SERVICES, "", "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getProvinces(CommonCallback<SimpleResult> callback) {
        try {
            String signature = RSAUtil.signature("");
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_DIC_GET_PROVINCE, "", "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getParams(CommonCallback<SimpleResult> callback) {
        try {
            String signature = RSAUtil.signature("");
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_DIC_GET_PARAMS, "", "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getDistricts(int provinceID, CommonCallback<SimpleResult> callback) {
        try {
            BaseRequest baseRequest = new BaseRequest();
            baseRequest.setID(provinceID);
            String data = getGson().toJson(baseRequest);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_DIC_GET_DISTRICT, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getWards(int districtID, CommonCallback<SimpleResult> callback) {
        try {
            BaseRequest baseRequest = new BaseRequest();
            baseRequest.setID(districtID);
            String data = getGson().toJson(baseRequest);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_DIC_GET_WARD, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void withdraw(WithdrawRequest request, CommonCallback<SimpleResult> callback) {
        try {
            String data = getGson().toJson(request);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_TRANS_WITHDRAW, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getOutward(int merchantID, CommonCallback<SimpleResult> callback) {
        try {
            BaseRequest request = new BaseRequest();
            request.setMerchantID(merchantID);
            String data = getGson().toJson(request);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_TRANS_GET_OUTWARD, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void withdrawSearch(int merchantID, CommonCallback<SimpleResult> callback) {
        try {
            BaseRequest request = new BaseRequest();
            request.setMerchantID(merchantID);
            String data = getGson().toJson(request);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_TRANS_WITHDRAW_SEARCH, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void employeeAdd(EmployeeAddNewRequest request, CommonCallback<SimpleResult> callback) {
        try {
            String data = getGson().toJson(request);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_EMP_ADD_NEW, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getOTP(String mobile,String isForget, CommonCallback<SimpleResult> callback) {
        try {
            BaseRequest request = new BaseRequest();
            request.setMobileNumber(mobile);
            request.setIsForget(isForget);
            String data = getGson().toJson(request);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_EMP_GET_OTP, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void updatePassword(UpdatePasswordRequest request, CommonCallback<SimpleResult> callback){
        try {
            String data = getGson().toJson(request);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_UPDATE_PASSWORD_BY_OTP, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void addMerchant(MerchantAddNewRequest request, CommonCallback<SimpleResult> callback) {
        try {
            String data = getGson().toJson(request);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_MERCHANT_ADD_NEW, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void editMerchant(MerchantAddNewRequest request, CommonCallback<SimpleResult> callback) {
        try {
            String data = getGson().toJson(request);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_MERCHANT_EDIT, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void getByMobileNumber(String mobile, CommonCallback<SimpleResult> callback) {
        try {
            BaseRequest request = new BaseRequest();
            request.setMobileNumber(mobile);
            String data = getGson().toJson(request);
            String signature = RSAUtil.signature(data);
            RequestObject requestObject = new RequestObject("ANDROID", "", Constants.CMD_MERCHANT_GET_BY_MOBILE_NUMBER, data, "", signature);
            Call<SimpleResult> call = getAPIBuilder().commonService(requestObject);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
