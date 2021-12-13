package org.instant.accept.instntsdk.network;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.instant.accept.instntsdk.data.FormCodes;
import org.instant.accept.instntsdk.data.FormSubmitResponse;
import org.instant.accept.instntsdk.data.OTPResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetworkUtil {

    private ApiInterface sandboxApiInterface;
    private ApiInterface productApiInterface;

    public NetworkUtil() {
        sandboxApiInterface = getApiService(true);
        productApiInterface = getApiService(false);
    }

    private OkHttpClient getOkHttpClient(){
        return new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(createHttpLoggingInterceptor())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Accept", "text/plain,application/json;version=1");

                        return chain.proceed(ongoing.build());
                    }
                })
                .build();
    }

    private Retrofit getRetrofit(OkHttpClient httpclient, boolean isSandbox){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(isSandbox? RestUrl.BASE_URL : RestUrl.PRODUCTION_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create()) //to send string as body
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        return builder.client(httpclient).build();
    }

    private ApiInterface getApiService(boolean isSandbox) {
        Retrofit retrofit = getRetrofit(getOkHttpClient(), isSandbox);

        return retrofit.create(ApiInterface.class);
    }

    private HttpLoggingInterceptor createHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @SuppressLint("CheckResult")
    public Observable<FormCodes> getFormFields(String formId, boolean isSandbox) {
        ApiInterface apiInterface = isSandbox? sandboxApiInterface : productApiInterface;

        return apiInterface.getFormCodes(formId, "json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public Observable<FormSubmitResponse> submit(String url, Map<String, Object> body, boolean isSandbox) {
        ApiInterface apiInterface = isSandbox? sandboxApiInterface : productApiInterface;

        return apiInterface.submitForm(url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public Observable<OTPResponse> sendOTP(String mobileNumber, boolean isSandbox) {
        ApiInterface apiInterface = isSandbox? sandboxApiInterface : productApiInterface;

        Map<String, String> innerBody = new HashMap<>();
        innerBody.put("phoneNumber", mobileNumber);
        JSONObject jsonObject = new JSONObject(innerBody);
        Map<String, Object> body = new HashMap<>();
        body.put("requestData", jsonObject.toString());
        body.put("isVerify", false);

        String url = RestUrl.BASE_URL + "otp/phone/send/v1.0";
        return apiInterface.sendOTP(url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public Observable<OTPResponse> verifyOTP(String mobileNumber, String enteredOTP, boolean isSandbox) {
        ApiInterface apiInterface = isSandbox? sandboxApiInterface : productApiInterface;

        Map<String, String> innerBody = new HashMap<>();
        innerBody.put("phoneNumber", mobileNumber);
        innerBody.put("otpCode", enteredOTP);

        JSONObject jsonObject = new JSONObject(innerBody);
        Map<String, Object> body = new HashMap<>();
        body.put("requestData", jsonObject.toString());
        body.put("isVerify", true);

        String url = RestUrl.BASE_URL + "otp/phone/verify/v1.0";
        return apiInterface.sendOTP(url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public Observable<Map<String, Object>> getTransactionID(boolean isSandbox, String formKey) {
        ApiInterface apiInterface = isSandbox? sandboxApiInterface : productApiInterface;

        Map<String, Object> body = new HashMap<>();
        body.put("form_key", formKey);
        body.put("hide_form_fields", true);
        body.put("idmetrics_version", "4.5.0.5");
        body.put("format", "json");
        body.put("redirect", false);

        String url = RestUrl.BASE_URL + "/public/transactions/";
        return apiInterface.getXNID(url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public Observable<Map<String, Object>> getUploadUrl(String instnttxnid, String docSuffix, boolean isSandbox) {
        ApiInterface apiInterface = isSandbox? sandboxApiInterface : productApiInterface;

        Map<String, Object> body = new HashMap<>();
        body.put("transaction_attachment_type", "IMAGE");
        body.put("document_type", "DRIVERS_LICENSE");
        body.put("doc_suffix", docSuffix);
        body.put("instnttxnid", instnttxnid);

        String url = RestUrl.BASE_URL + "/public/transactions/" + instnttxnid + "/attachments/";
        return apiInterface.getUploadUrl(url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public Observable<Map<String, Object>> uploadDocument(String presignedS3Url, byte[] imageData, boolean isSandbox) {
        ApiInterface apiInterface = isSandbox? sandboxApiInterface : productApiInterface;

        return apiInterface.uploadDocument(presignedS3Url, imageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
