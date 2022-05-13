package com.paynetone.counter.utils;

import com.core.common.BuildConfig;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class Utils {

    public static String getUrlImage(String fileName){
        return com.paynetone.counter.BuildConfig.IMAGE_URL + "/Assets/Images/" + fileName;
    }

    public static String getStatusName(String code){
        String name = "Chờ thanh toán";
        switch (code){
            case Constants.STATUS_W:
                name = "Chờ thanh toán";
                break;
            case Constants.STATUS_S:
                name = "Đã thanh toán";
                break;
            case Constants.STATUS_C:
                name = "Hủy";
                break;
        }
        return name;
    }

    public static String getStatusWithdrawName(int code){
        String name = "Từ chối";
        switch (code){
            case 0:
                name = "Đã chuyển tiền";
                break;
            case 1:
                name = "Chờ xử lý";
                break;
            case 2:
                name = "Từ chối";
                break;
            case 3:
                name = "Chờ xác nhận";
                break;
            case 4:
                name ="Chuyển tiền lỗi";
                break;
        }
        return name;
    }

    public static OkHttpClient getUnsafeOkHttpClient(int readTimeOut, int connectTimeOut,String userName) {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

            }};

            // Install the all-trusting trust manager
            final SSLContext tls = SSLContext.getInstance("TLS");
            tls.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = tls
                    .getSocketFactory();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            builder.readTimeout(readTimeOut, TimeUnit.SECONDS)
                    .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(hostnameVerifier);//org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER

            if (BuildConfig.DEBUG) {
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(loggingInterceptor);
            }

            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                   /* String credentials = "lottnet:dms";
                    String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);*/
                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader("Authorization", "Basic " + userName);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            builder.retryOnConnectionFailure(true);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
