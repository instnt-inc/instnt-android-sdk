package org.instnt.accept.instntsdk.network;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.instnt.accept.instntsdk.model.FormCodes;
import org.instnt.accept.instntsdk.model.FormSubmitResponse;
import org.instnt.accept.instntsdk.model.OTPResponse;

import org.instnt.accept.instntsdk.utils.CommonUtils;
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
    private String userAgent = System.getProperty("http.agent");

    public void setServerUrl(String serverUrl) {

        Log.i(CommonUtils.LOG_TAG, "Set serverUrl");
        this.serverUrl = serverUrl;
    }

    public NetworkUtil() {}

    private OkHttpClient getOkHttpClient(){
        Log.i(CommonUtils.LOG_TAG, "Calling getOkHttpClient");
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
        Log.i(CommonUtils.LOG_TAG, "Calling getRetrofit");
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
        Log.i(CommonUtils.LOG_TAG, "Calling getApiService");
        Retrofit retrofit = getRetrofit(getOkHttpClient(), serverUrl);

        return retrofit.create(ApiInterface.class);
    }

    private HttpLoggingInterceptor createHttpLoggingInterceptor() {
        Log.i(CommonUtils.LOG_TAG, "Calling createHttpLoggingInterceptor");
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    /**
     * Submit form
     * @param url
     * @param body
     * @return
     */
    @SuppressLint("CheckResult")
    public Observable<FormSubmitResponse> submit(String url, Map<String, Object> body) {
        Log.i(CommonUtils.LOG_TAG, "Calling submit form API");
        ApiInterface apiInterface = getApiService(this.serverUrl);

        return apiInterface.submitForm(this.userAgent, url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Send OTP
     * @param mobileNumber
     * @param instnttxnid
     * @return
     */
    @SuppressLint("CheckResult")
    public Observable<Map<String, Object>> sendOTP(String mobileNumber, String instnttxnid) {
        Log.i(CommonUtils.LOG_TAG, "Calling send OTP API");
        ApiInterface apiInterface = getApiService(this.serverUrl);

        Map<String, String> innerBody = new HashMap<>();
        innerBody.put("phoneNumber", mobileNumber);
        JSONObject jsonObject = new JSONObject(innerBody);
        Map<String, Object> body = new HashMap<>();
        body.put("phone", mobileNumber);

        String url = this.serverUrl + "transactions/" + instnttxnid + "/otp/";
        return apiInterface.sendOTP(this.userAgent, url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Verify OTP
     * @param mobileNumber
     * @param enteredOTP
     * @param instnttxnid
     * @return
     */
    @SuppressLint("CheckResult")
    public Observable<Map<String, Object>> verifyOTP(String mobileNumber, String enteredOTP, String instnttxnid) {
        Log.i(CommonUtils.LOG_TAG, "Calling verify OTP API");
        ApiInterface apiInterface = getApiService(this.serverUrl);

        Map<String, String> innerBody = new HashMap<>();
        innerBody.put("phoneNumber", mobileNumber);
        innerBody.put("otpCode", enteredOTP);

        JSONObject jsonObject = new JSONObject(innerBody);
        Map<String, Object> body = new HashMap<>();
        body.put("phone", mobileNumber);
        body.put("is_verify", true);
        body.put("otp", enteredOTP);

        String url = this.serverUrl + "transactions/" + instnttxnid + "/otp/";
        return apiInterface.sendOTP(this.userAgent, url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Get transaction
     * @param formKey
     * @return
     */
    @SuppressLint("CheckResult")
    public Observable<FormCodes> getTransactionID(String formKey) {

        Log.i(CommonUtils.LOG_TAG, "Calling get transaction API");
        ApiInterface apiInterface = getApiService(this.serverUrl);

        Map<String, Object> body = new HashMap<>();
        body.put("form_key", formKey);
        body.put("hide_form_fields", true);
        body.put("idmetrics_version", "4.5.0.5");
        body.put("format", "json");
        body.put("redirect", false);

        String url = this.serverUrl + "transactions/";
        return apiInterface.getXNID(this.userAgent, url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Get upload URL
     * @param instnttxnid
     * @param docSuffix
     * @return
     */
    @SuppressLint("CheckResult")
    public Observable<Map<String, Object>> getUploadUrl(String instnttxnid, String docSuffix) {

        Log.i(CommonUtils.LOG_TAG, "Calling get upload URL API");
        ApiInterface apiInterface = getApiService(this.serverUrl);

        Map<String, Object> body = new HashMap<>();
        body.put("transaction_attachment_type", "IMAGE");
        body.put("document_type", "DRIVERS_LICENSE");
        body.put("transaction_status", "NEW");
        body.put("doc_suffix", docSuffix);

        String url = this.serverUrl + "transactions/" + instnttxnid + "/attachments/";
        return apiInterface.getUploadUrl(this.userAgent, url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Upload document
     * @param fileName
     * @param presignedS3Url
     * @param imageData
     */
    @SuppressLint("CheckResult")
    public void uploadDocument(String fileName, String presignedS3Url, byte[] imageData) {

        Log.i(CommonUtils.LOG_TAG, "Calling get transaction API");
        ApiInterface apiInterface = getApiService(this.serverUrl);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageData);
        Call<Void> call = apiInterface.uploadDocument(this.userAgent, presignedS3Url, requestFile);
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

    /**
     * Verify documents
     * @param documentType
     * @param formKey
     * @param instnttxnid
     * @return
     */
    @SuppressLint("CheckResult")
    public Observable<Map<String, Object>> verifyDocuments(String documentType, String formKey, String instnttxnid) {
    	
    	Log.i(CommonUtils.LOG_TAG, "Calling verify documents API");
        ApiInterface apiInterface = getApiService(this.serverUrl);

        Map<String, Object> body = new HashMap<>();
        body.put("formKey", formKey);
        body.put("documentType", documentType);
        body.put("instnttxnid", instnttxnid);

        String url = this.serverUrl + "transactions/" + instnttxnid + "/attachments/verify/";
        return apiInterface.verifyDocuments(this.userAgent, url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
