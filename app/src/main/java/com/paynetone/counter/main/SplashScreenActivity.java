package com.paynetone.counter.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;
import com.paynetone.counter.login.LoginActivity;
import com.paynetone.counter.login.regiter.RegisterActivity;
import com.paynetone.counter.model.EmployeeModel;
import com.paynetone.counter.model.ParamModel;
import com.paynetone.counter.model.PaynetModel;
import com.paynetone.counter.model.SimpleResult;
import com.paynetone.counter.network.CommonCallback;
import com.paynetone.counter.network.NetWorkController;
import com.paynetone.counter.utils.Constants;
import com.paynetone.counter.utils.SharedPref;
import com.paynetone.counter.utils.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NetWorkController.getParams(new CommonCallback<SimpleResult>(this) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    SharedPref sharedPref = new SharedPref(getBaseContext());
                    List<ParamModel> paramModels = NetWorkController.getGson().fromJson(response.body().getData(), new TypeToken<List<ParamModel>>() {
                    }.getType());

                    for (ParamModel paramModel : paramModels){
                        if(paramModel.getCode().equals(Constants.KEY_ANDROID_PAYMENT_MODE)){
                            sharedPref.putString(Constants.KEY_ANDROID_PAYMENT_MODE,paramModel.getValue());
                            break;
                        }
                    }

                    EmployeeModel employeeModel = sharedPref.getEmployeeModel();
                    PaynetModel paynetModel = sharedPref.getPaynet();
                    Intent intent;
                    if (employeeModel != null && paynetModel != null) {
                        intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    } else {
                        intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Toast.showToast(getApplicationContext(), response.body().getMessage());
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
            }
        });



//        new Handler().postDelayed(() -> {
//            /* Create an Intent that will start the Menu-Activity. */
//            SharedPref sharedPref = new SharedPref(getBaseContext());
//            EmployeeModel employeeModel = sharedPref.getEmployeeModel();
//            PaynetModel paynetModel = sharedPref.getPaynet();
//            Intent intent;
//            if (employeeModel != null && paynetModel != null) {
//                intent = new Intent(SplashScreenActivity.this, MainActivity.class);
//            } else {
//                intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
//            }
//            startActivity(intent);
//            finish();
//        }, SPLASH_DISPLAY_LENGTH);
//        Intent intent = new Intent(this, RegisterActivity.class);
//        startActivity(intent);
    }
}
