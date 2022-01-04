package org.instant.accept.instntsdk.network;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.instant.accept.instntsdk.model.FormCodes;
import org.instant.accept.instntsdk.model.FormSubmitResponse;
import org.instant.accept.instntsdk.model.OTPResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetworkUtil {

    private String serverUrl;

    public void setServerUrl(String serverUrl) {

        this.serverUrl = serverUrl;
    }

    public NetworkUtil() {

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

    private Retrofit getRetrofit(OkHttpClient httpclient, String serverUrl){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create()) //to send string as body
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        return builder.client(httpclient).build();
    }

    private ApiInterface getApiService(String serverUrl) {
        Retrofit retrofit = getRetrofit(getOkHttpClient(), serverUrl);

        return retrofit.create(ApiInterface.class);
    }

    private HttpLoggingInterceptor createHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @SuppressLint("CheckResult")
    public Observable<FormCodes> getFormFields(String formId) {
        ApiInterface apiInterface = getApiService(this.serverUrl);

        return apiInterface.getFormCodes(formId, "json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public Observable<FormSubmitResponse> submit(String url, Map<String, Object> body) {
        ApiInterface apiInterface = getApiService(this.serverUrl);

        return apiInterface.submitForm(url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public Observable<OTPResponse> sendOTP(String mobileNumber) {
        ApiInterface apiInterface = getApiService(this.serverUrl);

        Map<String, String> innerBody = new HashMap<>();
        innerBody.put("phoneNumber", mobileNumber);
        JSONObject jsonObject = new JSONObject(innerBody);
        Map<String, Object> body = new HashMap<>();
        body.put("requestData", jsonObject.toString());
        body.put("isVerify", false);

        String url = this.serverUrl + "otp/phone/send/v1.0";
        return apiInterface.sendOTP(url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public Observable<OTPResponse> verifyOTP(String mobileNumber, String enteredOTP) {
        ApiInterface apiInterface = getApiService(this.serverUrl);

        Map<String, String> innerBody = new HashMap<>();
        innerBody.put("phoneNumber", mobileNumber);
        innerBody.put("otpCode", enteredOTP);

        JSONObject jsonObject = new JSONObject(innerBody);
        Map<String, Object> body = new HashMap<>();
        body.put("requestData", jsonObject.toString());
        body.put("isVerify", true);

        String url = this.serverUrl + "otp/phone/verify/v1.0";
        return apiInterface.sendOTP(url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public Observable<Map<String, Object>> getTransactionID(String formKey) {
        ApiInterface apiInterface = getApiService(this.serverUrl);

        Map<String, Object> body = new HashMap<>();
        body.put("form_key", formKey);
        body.put("hide_form_fields", true);
        body.put("idmetrics_version", "4.5.0.5");
        body.put("format", "json");
        body.put("redirect", false);

        String url = this.serverUrl + "transactions/";
        return apiInterface.getXNID(url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public Map<String, Object> getTransactionID1(String formKey) throws Exception {
        ApiInterface apiInterface = getApiService(this.serverUrl);

        Map<String, Object> body = new HashMap<>();
        body.put("form_key", formKey);
        body.put("hide_form_fields", true);
        body.put("idmetrics_version", "4.5.0.5");
        body.put("format", "json");
        body.put("redirect", false);

        String url = this.serverUrl + "transactions/";
        Call<Map<String, Object>> callSync = apiInterface.getXNID1(url, body);
        callSync.enqueue(new Callback<Map<String, Object>>() {

            @Override
            public void onResponse(Call<Map<String, Object>> call, retrofit2.Response<Map<String, Object>> response) {
                Map<String, Object> myItem = response.body();
                System.out.println("");
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                //Handle failure
                System.out.println("");
            }
        });


//        retrofit2.Response<Map<String, Object>> response = callSync.execute();
//        Map<String, Object> apiResponse = response.body();
        return null;
    }

    @SuppressLint("CheckResult")
    public Observable<Map<String, Object>> getUploadUrl(String instnttxnid, String docSuffix) {
        ApiInterface apiInterface = getApiService(this.serverUrl);

        Map<String, Object> body = new HashMap<>();
        body.put("transaction_attachment_type", "IMAGE");
        body.put("document_type", "DRIVERS_LICENSE");
        body.put("transaction_status", "NEW");
        body.put("doc_suffix", docSuffix);

        String url = RestUrl.BASE_URL + "transactions/" + instnttxnid + "/attachments/";
        return apiInterface.getUploadUrl(url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public void uploadDocument(String fileName, String presignedS3Url, byte[] imageData) {
        ApiInterface apiInterface = getApiService(this.serverUrl);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageData);
        Call<Void> call = apiInterface.uploadDocument(presignedS3Url, requestFile);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                System.out.println("success");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("error");
            }
        });
    }

    @SuppressLint("CheckResult")
    public Observable<Map<String, Object>> verifyDocuments(String documentType, String formKey, String instnttxnid) {
        ApiInterface apiInterface = getApiService(this.serverUrl);

        Map<String, Object> body = new HashMap<>();
        body.put("formKey", formKey);
        body.put("documentType", documentType);
        body.put("instnttxnid", instnttxnid);

        String url = this.serverUrl + "docverify/authenticate/v1.0";
        return apiInterface.verifyDocuments(url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
